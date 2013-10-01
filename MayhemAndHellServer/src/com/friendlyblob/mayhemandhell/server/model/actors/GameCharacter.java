package com.friendlyblob.mayhemandhell.server.model.actors;

import com.friendlyblob.mayhemandhell.server.GameTimeController;
import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.stats.BaseStats;
import com.friendlyblob.mayhemandhell.server.model.stats.CharacterStats;
import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;
import com.friendlyblob.mayhemandhell.server.network.GameClient;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.NotifyCharacterMovement;
import com.friendlyblob.mayhemandhell.server.utils.ObjectPosition;

/*
 * Parent of players and npc's
 */
public class GameCharacter extends GameObject{

	private int health;
	
	private int energy;
	private int mana;
	
	private boolean alive;
	
	private CharacterStats stats;
	
	private MovementData movement;
	
	private CharacterTemplate template;
	
	public GameCharacter(int objectId, CharacterTemplate template) {
		this.objectId = objectId;
		this.template = template;
		
		// Initialize stats
		stats = new CharacterStats(this);
	}
	
	/**
	 * Updating character position.
	 * @param gameTicks current game tick
	 * @return true if character has reached its destination
	 */
	public boolean updatePosition (int gameTicks) {
		float prevX = getPosition().getX();
		float prevY = getPosition().getY();
		
		float angle = (float) Math.atan2(movement.destinationY - prevY, movement.destinationX - prevX);
		
		// TODO check if running or walking
		int movementSpeed = getMovementSpeed();
		float distanceCovered = GameTimeController.DELTA_TIME*movementSpeed;
		
		getPosition().offset((float) Math.cos(angle) * distanceCovered, 
				(float) Math.sin(angle) * distanceCovered);
		
		int dX = (int)(movement.destinationX - getPosition().getX());
		int dY = (int)(movement.destinationY - getPosition().getY());
		
		// Check if destination is reached
		if (dX * dX + dY * dY < distanceCovered * distanceCovered) {
			getPosition().set(movement.destinationX, movement.destinationY);
			movement = null;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Registers a new movement destination in <b>movement</b> variable
	 * Called only once when user requests to move a character.
	 * 
	 * Actions:
	 * 	Checking for collisions
	 * 	If character is about to collide with something, move it to collision point
	 * 
	 * @param x requested destination x
	 * @param y requested destination y
	 * @return true if movement is initialized, false if impossible to move
	 */
	public boolean moveCharacterTo(int x, int y) {
		// TODO check boundaries and collisions. If out of bounds - return false
		
		movement = new MovementData();
		movement.destinationX = x;
		movement.destinationY = y;
		movement.movementSpeed = getMovementSpeed();
		movement.timeStamp = GameTimeController.getInstance().getGameTicks();
		
		GameTimeController.getInstance().registerMovingObject(this);
		
		// Notify nearby characters about movement
		getRegion().broadcastToCloseRegions(new NotifyCharacterMovement(getObjectId(), movement, getPosition()));
		
		return true;
	}
	
	public CharacterStats getStats() {
		return stats;
	}
	
	public static class MovementData {
		public float destinationX;
		public float destinationY;
		public int movementSpeed;
		public int timeStamp;
	}
	
	/**
	 * Restores health, mana and energy to maximum
	 */
	public void restoreVitals() {
		this.health = getMaxHealth();
		this.energy = getMaxEnergy();
		this.mana = getMaxMana();
	}
	
	public void revive() {
		this.alive = true;
	}
	
	/**
	 * TODO implement
	 */
	public void removeEffects() {
	}
	
	public MovementData getMovement() {
		return movement;
	}
	
	public int getMovementSpeed() {
		// TODO Check whether running or walking, and return what's necessary
		return getWalkingSpeed();
	}
	
	private int getMaxHealth() {
		return stats.getMaxHealth();
	}
	
	private int getMaxMana() {
		return stats.getMaxMana();
	}
	
	private int getMaxEnergy() {
		return stats.getMaxEnergy();
	}
	
	private int getWalkingSpeed() {
		return stats.getWalkingSpeed();
	}
	

	public void sendPacket(ServerPacket packet) {
		
	}
	
	public CharacterTemplate getTemplate() {
		return template;
	}
	
	/**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Restores health to maximum amount
	 */
	public void restoreHealth() {
		this.health = getMaxHealth();
	}
	
	/**
	 * @param health the health to set
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * @return the energy
	 */
	public int getEnergy() {
		return energy;
	}

	/**
	 * @param energy the energy to set
	 */
	public void setEnergy(int energy) {
		this.energy = energy;
	}

	/**
	 * @return the mana
	 */
	public int getMana() {
		return mana;
	}

	/**
	 * @param mana the mana to set
	 */
	public void setMana(int mana) {
		this.mana = mana;
	}
	
}
