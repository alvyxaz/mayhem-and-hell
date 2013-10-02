package com.friendlyblob.mayhemandhell.server.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.CharacterLeft;
import com.friendlyblob.mayhemandhell.server.network.packets.server.CharactersInRegion;

import javolution.util.FastMap;

/**
 * Represents a part of a zone. This class is mainly for optimization purposes.
 * Client should not be able to notice when he's passing from one region to another.
 * @author Alvys
 *
 */
public class Region {

	public enum RegionSide {
		LEFT,
		RIGHT,
		TOP,
		BOTTOM,
		NONE
	}
	
	private FastMap<Integer, GameCharacter> characters;
	private FastMap<Integer, GameObject> allObjects;
	
	private Region [] closeRegions;
	
	public int regionX;
	public int regionY;
	
	public Region(int x, int y) {
		regionX = x;
		regionY = y;
		characters = new FastMap<Integer, GameCharacter>().shared();
		allObjects = new FastMap<Integer, GameObject>().shared();
		closeRegions = new Region [0];
	}
	
	/**
	 * Adds an object to region's objects collection 
	 * @param object
	 */
	public void addObject(GameObject object) {
		allObjects.put(object.getObjectId(), object);
	}
	
	/**
	 * Removes an object from region's objects collection
	 * @param object
	 */
	public void removeObject(GameObject object) {
		allObjects.remove(object.getObjectId());
	}
	
	/**
	 * Removes character from region.
	 * @param character
	 */
	public void removeCharacter(GameCharacter character) {
		characters.remove(character.getObjectId());
		allObjects.remove(character.getObjectId());
	}
	
	/**
	 * Adds a character to the region. Also, sets character's region to current region
	 * @param character
	 */
	public void addCharacter(GameCharacter character) {
		character.setRegion(this);
		characters.put(character.getObjectId(), character);
		allObjects.put(character.getObjectId(), character);
	}
	
	/**
	 * Value, returned by this method, will include characters,
	 * because all of the characters are also stored in allObjects.
	 * @param objectId
	 * @return GameObject in this region, or null if it does not exist
	 */
	public GameObject getObject(int objectId) {
		return allObjects.get(objectId);
	}
	
	/**
	 * Just like getObject, except looks through every nearby region
	 * @param objectId
	 * @return
	 */
	public GameObject getCloseObject(int objectId) {
		GameObject object = null;
		for (Region region : closeRegions) {
			object = region.getObject(objectId);
			if (object != null) {
				return object;
			}
		}
		
		return getObject(objectId);
	}
	
	/**
	 * Sends a packet to characters in all nearby regions, including itself
	 * @param packet
	 */
	public void broadcastToCloseRegions(ServerPacket packet) {
		broadcast(packet); // Broadcast to itself
		
		// Broadcast to close packet
		for(int i = 0; i < closeRegions.length; i++) {
			closeRegions[i].broadcast(packet);
		}
	}
	
	/**
	 * Sends a packet to characters that are in one side of current region;
	 * @param side
	 * @param packet
	 */
	public void broadcastToSide(RegionSide side, ServerPacket packet) {
		if (side == RegionSide.NONE) {
			return;
		}
			
		for(int i = 0; i < closeRegions.length; i++) {
			switch(side) {
				case LEFT:
					if (regionX > closeRegions[i].regionX) {
						closeRegions[i].broadcast(packet);
					}
					break;
				case RIGHT:
					if (regionX < closeRegions[i].regionX) {
						closeRegions[i].broadcast(packet);
					}
					break;
				case TOP:
					if (regionY < closeRegions[i].regionY) {
						closeRegions[i].broadcast(packet);
					}
					break;
				case BOTTOM:
					if (regionY > closeRegions[i].regionY) {
						closeRegions[i].broadcast(packet);
					}
					break;
			}
		}
	}

	/**
	 * Sends data about nearby characters to all players
	 */
	public void updateNearbyPlayersData() {
		// If there's no one to update data far
		if (this.characters.size() == 0 ) return;
		
		List<GameCharacter> visibleCharacters = getVisibleCharacters();
		
		if (characters.size() > 0) {
			CharactersInRegion packet = new CharactersInRegion(visibleCharacters);
			broadcast(packet);
		}
		 
	}
	
	/**
	 * Sends a packet to characters in this region
	 * @param packet
	 */
	public void broadcast(ServerPacket packet) {
		for(GameCharacter character : characters.values()) {
			character.sendPacket(packet);
		}
	}
	
	/**
	 * Returns a list of objects that are in nearby regions
	 * @return visible objects in a List
	 */
	public List<GameObject> getVisibleObjects() {
		List<GameObject> visibleObjects = new ArrayList<GameObject>();
		
		// Current region
		for (GameObject object : allObjects.values()) {
			visibleObjects.add(object);
		}
		
		// Nearby regions
		for(int i = 0; i < closeRegions.length; i++) {
			for(GameObject object : closeRegions[i].getObjects().values()) {
				visibleObjects.add(object);
			}
		}

		return visibleObjects;
	}

	
	/**
	 * Returns a list of characters that are in nearby regions
	 * @return visible characters in a List
	 */
	public List<GameCharacter> getVisibleCharacters() {
		List<GameCharacter> visibleCharacters = new ArrayList<GameCharacter>();
		
		// Current region
		for (GameCharacter character : characters.values()) {
			visibleCharacters.add(character);
		}
		
		// Nearby regions
		for(int i = 0; i < closeRegions.length; i++) {
			for(GameCharacter character : closeRegions[i].getCharacters().values()) {
				visibleCharacters.add(character);
			}
		}

		return visibleCharacters;
	}
	
	/**
	 * TODO might be possible to optimize
	 * @param side
	 * @return
	 */
	public List<GameCharacter> getVisibleCharactersAtSide(RegionSide side) {
		List<GameCharacter> visibleCharacters = new ArrayList<GameCharacter>();
		
		for(int i = 0; i < closeRegions.length; i++) {
			switch(side) {
				case LEFT:
					if (regionX > closeRegions[i].regionX) {
						visibleCharacters.addAll(closeRegions[i].getCharacters().values());
					}
					break;
				case RIGHT:
					if (regionX < closeRegions[i].regionX) {
						visibleCharacters.addAll(closeRegions[i].getCharacters().values());
					}
					break;
				case TOP:
					if (regionY < closeRegions[i].regionY) {
						visibleCharacters.addAll(closeRegions[i].getCharacters().values());
					}
					break;
				case BOTTOM:
					if (regionY > closeRegions[i].regionY) {
						visibleCharacters.addAll(closeRegions[i].getCharacters().values());
					}
					break;
			}
		}
		return visibleCharacters;
	}
	
	/**
	 * Adds a region to closeRegions array
	 * @param region
	 */
	public void addCloseRegion(Region region) {
		Region temp [] = new Region[closeRegions.length+1];
		
		for(int i = 0; i < closeRegions.length; i++) {
			temp[i] = closeRegions[i];
		}
		
		temp[temp.length-1] = region;
		closeRegions = temp;
	}
	
	public FastMap<Integer, GameCharacter> getCharacters() {
		return characters;
	}
	
	public FastMap<Integer, GameObject> getObjects() {
		return allObjects;
	}
}
