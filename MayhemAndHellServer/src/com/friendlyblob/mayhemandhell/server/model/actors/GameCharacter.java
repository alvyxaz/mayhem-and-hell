package com.friendlyblob.mayhemandhell.server.model.actors;

import com.friendlyblob.mayhemandhell.server.GameTimeController;
import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.World;
import com.friendlyblob.mayhemandhell.server.model.logic.CollisionManager;
import com.friendlyblob.mayhemandhell.server.model.logic.CollisionManager.Point;
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
		
		// Updating destination if following a target
		if (movement.isFollowing()) {
			movement.updateDestination();
			
			if (!movement.isWalkingBy()) {
				return false;
			}
			
		}
		
		// Check if destination is reached
		if (dX * dX + dY * dY < distanceCovered * distanceCovered) {
			getPosition().set(movement.destinationX, movement.destinationY);
			movement = null;
			return true;
		}
		
		return false;
	}

	/**
	 * Registers a new movement destination
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
		
		MovementData movementData = new MovementData();
		movementData.destinationX = x;
		movementData.destinationY = y;
		movementData.movementSpeed = getMovementSpeed();
		movementData.timeStamp = GameTimeController.getInstance().getGameTicks();
		
		moveCharacterTo(movementData);
		
		return true;
	}
	
	public void walkBy(GameObject character) {
		MovementData movementData = new MovementData();
		movementData.movementSpeed = getMovementSpeed();
		movementData.timeStamp = GameTimeController.getInstance().getGameTicks();
		movementData.follow(character, true);
		moveCharacterTo(movementData);
	}
	
	/**
	 * Initializes a movement from a given MovementData
	 * @param movementData
	 * @return
	 */
	public boolean moveCharacterTo(MovementData movementData) {
		this.movement = movementData;
		
		GameTimeController.getInstance().registerMovingObject(this);
		
		// Notify nearby characters about movement
		getRegion().broadcastToCloseRegions(new NotifyCharacterMovement(getObjectId(), movementData, getPosition()));
		
		return true;
	}

	
	public int getTileXAt(int x) {
		return x/16;
	}
	
	public int getTileYAt(int y) {
		return y/16;
	}
	
	public int getTileAt(int x, int y) {
		return 0;
	}
	
	public int sqrDistanceBetween(float f, float g, int endX, int endY) {
		return (int) ((endX - f)*(endX - f) + (endY-g)*(endY-g));
	}	
	
	public int getLinearYAtX(int x, int currentX, int currentY, int targetX, int targetY) {
		System.out.println("LINEAR Y AT X");
		System.out.println("x: " + x + " currentX: " + currentX + " currentY: " + currentY + " targetX: " + targetX + "targetY: " + targetY);
		
		int temp = (targetX-currentX);
		
		if (temp == 0) {
			temp = 1;
		}
		
		return (int)Math.abs((((targetY-currentY)/temp)*(x-currentX) + currentY));
	}
	
	public int getLinearXAtY(int y, int currentX, int currentY, int targetX, int targetY) {
		System.out.println("LINEAR X AT Y");
		System.out.println("y: " + y + " currentX: " + currentX + " currentY: " + currentY + " targetX: " + targetX + "targetY: " + targetY);

		int temp = (targetY-currentY);
		
		if (temp == 0) {
			temp = 1;
		}
		
		return (int)((targetX-currentX)*(y-currentY)/temp + currentX);
	}
	
	public float angleBetween(int currentX, int currentY, int targetX, int targetY) {
		return (float)Math.atan2(targetY-currentY, targetX-currentX);
	}

	public CharacterStats getStats() {
		return stats;
	}
	
	public static class MovementData {
		public float destinationX;
		public float destinationY;
		public int movementSpeed;
		public int timeStamp;

		private boolean walkBy;
		private GameObject targetToFollow;
		
		public void follow(GameObject character, boolean walkBy) {
			this.walkBy = walkBy;
			this.targetToFollow = character;
			destinationX = character.getPosition().getX();
			destinationY = character.getPosition().getY();
		}
		
		public boolean isFollowing() {
			return targetToFollow != null;
		}
		
		public void updateDestination() {
			destinationX = targetToFollow.getPosition().getX();
			destinationY = targetToFollow.getPosition().getY();
		}
		
		/**
		 * Checks whether our intention is to walk by, or follow "forever"
		 * @return true if walking by.
		 */
		public boolean isWalkingBy() {
			return walkBy;
		}
		
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
