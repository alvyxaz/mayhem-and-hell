package com.friendlyblob.mayhemandhell.server.model.actors;

import com.friendlyblob.mayhemandhell.server.GameTimeController;
import com.friendlyblob.mayhemandhell.server.network.packets.server.CharacterLeft;

/**
 * Represents an enemy unit
 * @author Alvys
 *
 */
public class Mob extends GameCharacter {
	
	public Mob(int objectId, CharacterTemplate template) {
		super(objectId, template);
	}

	private boolean aggressive;
	
	private boolean dead;
	
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
		
		dead = false;
	}
	
	/**
	 * Set's mob's state to dead, adds it to GameTimeController
	 * to be respawned.
	 * TODO Show dead body for a while, before respawning
	 */
	public void markAsDead() {
		dead = true;
		this.removeTarget();
		this.clearTargetedBy();
		
		spawnAtTick = GameTimeController.getInstance().getGameTicks() 
				+ spawnInterval/GameTimeController.MILLIS_IN_TICK;
		GameTimeController.getInstance().registerRespawningMob(this);
		this.getRegion().broadcastToCloseRegions(new CharacterLeft(this.getObjectId()));
	}
}
