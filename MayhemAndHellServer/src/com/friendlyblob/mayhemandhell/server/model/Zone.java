package com.friendlyblob.mayhemandhell.server.model;

import java.util.Map;

import javolution.util.FastMap;

import com.friendlyblob.mayhemandhell.server.model.Region.RegionSide;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.CharacterAppeared;
import com.friendlyblob.mayhemandhell.server.network.packets.server.CharacterLeft;
import com.friendlyblob.mayhemandhell.server.network.packets.server.CharactersInRegion;

/**
 * Represents zones that have no connection with each other,
 * like island, ocean or dungeon. If client passes a zone,
 * he will be teleported to another one.
 * 
 * @author Alvys
 *
 */
public class Zone {
	private int zoneId;
	
	private Map<Integer, Player> allPlayers;
	private Map<Integer, GameObject> allObjects;
	
	private Region [][]  regions;
	
	private ZoneTemplate template;
	
	public Zone(ZoneTemplate template) {
		this.template = template;
		allPlayers = new FastMap<Integer, Player>().shared();
		allObjects = new FastMap<Integer, GameObject>().shared();
		
		initializeRegions();
	}
	
	/**
	 * Adding a player to the zone. 
	 * Automatically adds a player to the region.
	 * @param player player to be added
	 */
	public void addPlayer(Player player) {
		allPlayers.put(player.getObjectId(), player);
		player.setZone(this);
		
		updateRegion(player);
	}
	
	/**
	 * Adding an object to the zone.
	 * (Assigns a region for object too)
	 * @param object
	 */
	public void addObject(GameObject object) {
		object.setZone(this);
		object.setRegion(
				regions[getRegionY((int)object.getPosition().getY())][getRegionX((int)object.getPosition().getX())]);
		object.getRegion().addObject(object);
		allObjects.put(object.getObjectId(), object);
	}
	
	/**
	 * Remove an object from zone (including region).
	 * @param object
	 */
	public void removeObject(GameObject object) {
		object.setZone(null);
		object.setRegion(null);
		allObjects.remove(object.getObjectId());
	}
	
	/**
	 * Returns X index of a region from a given coordinate
	 * @param x zone coordinate that gets translated into region X index
	 * @return region X index
	 */
	public int getRegionX(int x) {
		return (x/template.getTileWidth())/template.getRegionWidth();
	}
	
	/**
	 * Returns Y index of a region from a given coordinate
	 * @param y zone coordinate that gets translated into regions Y index
	 * @return region y index
	 */
	public int getRegionY(int y) {
		return (y/(template.getTileHeight()/2))/template.getRegionHeight();
	}
	
	/**
	 * Check whether character has moved out of current region and joined another.
	 */
	public void updateRegion(GameCharacter character) {
		int regionX = getRegionX((int)(character.getPosition().getX()));
		int regionY = getRegionY((int)(character.getPosition().getY()));
		
		if(regionX >= template.getRegionsCountX() || regionX < 0 || regionY >= template.getRegionsCountY() || regionY < 0) {
			return;
		}
		
		Region oldRegion = character.getRegion();
		
		boolean firstAppearance = oldRegion == null;  // oldRegion is null if player just joined
		
		if(regions[regionY][regionX] != oldRegion) {
			if(!firstAppearance) {
				oldRegion.removeCharacter(character);	 	// Remove from old region
				
				// Notify players at farthest side, indicating that this character left visible area
				oldRegion.broadcastToSide(getRegionSideByOffsetX(oldRegion.regionX, regionX, true), 
						new CharacterLeft(character.getObjectId()));
				oldRegion.broadcastToSide(getRegionSideByOffsetY(oldRegion.regionY, regionY, true), 
						new CharacterLeft(character.getObjectId()));
				
				// Notify players at newly visible side, indicating that a new character is now visible
				regions[regionY][regionX].broadcastToSide(getRegionSideByOffsetX(oldRegion.regionX, regionX, false), 
						new CharacterAppeared(character));
				regions[regionY][regionX].broadcastToSide(getRegionSideByOffsetY(oldRegion.regionY, regionY, false), 
						new CharacterAppeared(character));
			}
			
			character.setRegion(regions[regionY][regionX]); 	// Set new region to current
			regions[regionY][regionX].addCharacter(character);	// Add player to current region
			
			// If it's the first appearance, notify nearby regions that a new character is visible
			if (firstAppearance) {
				character.getRegion().broadcastToCloseRegions(new CharacterAppeared(character));
				character.sendPacket(new CharactersInRegion(character.getRegion().getVisibleCharacters()));
			}
		}
	}
	
	/**
	 * Returns a side to which regions have changed (newly visible side)
	 * @param flip Flips directions
	 * @return RegionSide Return newly visible side, or invisible if flip is set to true.
	 */
	public RegionSide getRegionSideByOffsetX(int oldX, int newX, boolean flip) {
		if (oldX == newX) {
			return RegionSide.NONE;
		}
		
		return (oldX < newX) ^ flip ? RegionSide.RIGHT : RegionSide.LEFT;
	}
	
	/**
	 * Returns a side to which regions have changed (newly visible side)
	 * @param flip Flips directions
	 * @return RegionSide Return newly visible side, or invisible if flip is set to true.
	 */
	public RegionSide getRegionSideByOffsetY(int oldY, int newY, boolean flip) {
		if (oldY == newY) {
			return RegionSide.NONE;
		}
		
		return (oldY < newY) ^ flip ? RegionSide.TOP : RegionSide.BOTTOM;
	}
	
	/**
	 * Removes a player from the zone and region he's in.
	 * Notifies nearby players about character leaving.
	 * 
	 * @param playerId Player's id
	 */
	public void removePlayer(Player player) {
		allPlayers.remove(player.getObjectId());
		player.getRegion().removeCharacter(player);
		player.getRegion().broadcastToCloseRegions(new CharacterLeft(player.getObjectId()));
	}
	
	/**
	 * Sends data about nearby characters to all players
	 */
	public void nearbyCharactersBroadcast() {
		for(int y = 0; y < template.getRegionsCountY(); y++) {
			for(int x = 0; x < template.getRegionsCountX(); x++) {
				regions[y][x].updateNearbyPlayersData();
			}
		}
	}
	
	/**
	 * Fills regions with information about close regions
	 */
	private void initializeRegions() {

		regions = new Region[template.getRegionsCountY()][template.getRegionsCountX()];
		for(int y = 0; y < template.getRegionsCountY(); y++) {
			for(int x = 0; x < template.getRegionsCountX(); x++) {
				regions[y][x] = new Region(x, y);
			}
		}
		
		// Connecting nearby regions
		for(int y = 0; y < template.getRegionsCountY(); y++) {
			for(int x = 0; x < template.getRegionsCountX(); x++) {
				boolean right = false;
				boolean left = false;
				boolean bottom = false;
				boolean top = false;
				
				// If has a neigbour at right
				if (x < template.getRegionsCountX()-1) {
					right = true;
					regions[y][x].addCloseRegion(regions[y][x+1]); 
				}
				
				// If has a neigbour at left
				if (x > 0) {
					left = true; 
					regions[y][x].addCloseRegion(regions[y][x-1]); 
				}
				
				// If has a neigbour at bottom
				if (y >= 1) {
					bottom = true;
					regions[y][x].addCloseRegion(regions[y-1][x]); 
				}
				
				// If has a neigbour at top
				if (y < template.getRegionsCountY()-1) {
					top = true;
					regions[y][x].addCloseRegion(regions[y+1][x]); 
				}
				
				if (top && left) {
					regions[y][x].addCloseRegion(regions[y+1][x-1]); 
				}
				
				if (top && right) {
					regions[y][x].addCloseRegion(regions[y+1][x+1]); 
				}
				
				if (bottom && left) {
					regions[y][x].addCloseRegion(regions[y-1][x-1]); 
				}
				
				if (bottom && right) {
					regions[y][x].addCloseRegion(regions[y-1][x+1]); 
				}
			}
		}
	}
}
