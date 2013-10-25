package com.friendlyblob.mayhemandhell.server.model.actors;

import com.friendlyblob.mayhemandhell.server.GameTimeController;
import com.friendlyblob.mayhemandhell.server.network.packets.server.CharactersLeft;

/**
 * Represents an enemy unit
 * @author Alvys
 *
 */
public class MobInstance extends GameCharacter {
	
	public MobInstance(int objectId, CharacterTemplate template) {
		super(objectId, template);
	}

	private boolean aggressive;
	
	private int spawnZoneX;
	private int spawnZoneY;
	private int wonderingWidth;
	private int wonderingHeight;

	private int spawnInterval; // Milliseconds
	private int spawnAtTick;
	
	public boolean isAggressive() {
		return aggressive;
	}
	
	public void setAggressive(boolean aggressive) {
		this.aggressive = aggressive;
	}
	
	/**
	 * Updates respawn period and checks whether mob needs
	 * to be respawned
	 * @return true if mob can be respawned
	 */
	public boolean updateRespawn(int gameTicks) {
		if (spawnAtTick <= gameTicks) {
			respawn();
			return true;
		}
		return false;
	}
	
	/**
	 * Respawns a mob and regenerates it's stats
	 */
	public void respawn() {
		this.restoreHealth();
		// Reset position
		this.setPosition(spawnZoneX+(int)(Math.random()*wonderingWidth), 
				spawnZoneY+(int)(Math.random()*wonderingHeight));
		
		// Update region if changed (automatically notifies nearby)
		this.getZone().updateRegion(this);
		
		this.alive = true;
	}
	
	@Override
	public void onDeath() {
		super.onDeath();
		spawnAtTick = GameTimeController.getInstance().getGameTicks() 
				+ spawnInterval/GameTimeController.MILLIS_IN_TICK;
	}
}
