package com.friendlyblob.mayhemandhell.server.model.actors;

import java.nio.ByteBuffer;

import com.friendlyblob.mayhemandhell.server.GameTimeController;
import com.friendlyblob.mayhemandhell.server.ai.AiData;
import com.friendlyblob.mayhemandhell.server.ai.Event;
import com.friendlyblob.mayhemandhell.server.ai.GameCharacterAi;
import com.friendlyblob.mayhemandhell.server.ai.Intention;
import com.friendlyblob.mayhemandhell.server.factories.ItemFactory;
import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.World;
import com.friendlyblob.mayhemandhell.server.model.Zone;
import com.friendlyblob.mayhemandhell.server.model.datatables.ItemTable;
import com.friendlyblob.mayhemandhell.server.model.instances.ItemInstance;
import com.friendlyblob.mayhemandhell.server.model.items.Item;
import com.friendlyblob.mayhemandhell.server.model.logic.Formulas;
import com.friendlyblob.mayhemandhell.server.model.quests.Quest;
import com.friendlyblob.mayhemandhell.server.model.quests.Quest.QuestEventType;
import com.friendlyblob.mayhemandhell.server.model.stats.CharacterStats;
import com.friendlyblob.mayhemandhell.server.network.ThreadPoolManager;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.Attack;
import com.friendlyblob.mayhemandhell.server.network.packets.server.CharacterStatusUpdate;
import com.friendlyblob.mayhemandhell.server.network.packets.server.DeathNotification;
import com.friendlyblob.mayhemandhell.server.network.packets.server.EventNotification;
import com.friendlyblob.mayhemandhell.server.network.packets.server.NotifyCharacterMovement;
import com.friendlyblob.mayhemandhell.server.network.packets.server.NotifyMovementStop;
import com.friendlyblob.mayhemandhell.server.network.packets.server.TargetInfoResponse;
import com.friendlyblob.mayhemandhell.server.utils.ObjectPosition;

/*
 * Parent of players and npc's
 */
public class GameCharacter extends GameObject{

	private int health;
	
	private int energy;
	private int mana;
	
	protected boolean alive;
	
	private CharacterStats stats;
	
	private MovementData movement;
	
	private CharacterTemplate template;
	
	protected GameCharacterAi ai;
	
	private int attackEndTime;
	
	public GameCharacter(int objectId, CharacterTemplate template) {
		this.objectId = objectId;
		this.template = template;
		
		// Initialize stats
		stats = new CharacterStats(this);
	}
	
	public synchronized void attachAi() {
		if (ai == null) {
			ai = new GameCharacterAi(this);
		}
	}
	
	/**
	 * Don't call it manually, use prepareToDetachAi first,
	 * which calls this method automatically.
	 */
	public synchronized void detachAi() {
		ai = null;
	}
	
	// TODO Threw a null pointer exception (Region.removeObject -> ..)
	public void prepareToDetachAi() {
		ai.prepareToDetach();
	}
	
	public int getSprite() {
		return template.getSprite();
	}
	
	public GameCharacterAi getAi() {
		GameCharacterAi tempAi = ai;
		
		if (tempAi == null) {
			synchronized(this) {
				if (ai == null) {
					ai = new GameCharacterAi(this);
				}
				return ai;
			}
		}
		
		return tempAi;
	}
	
	/**
	 * Updating character position.
	 * @param gameTicks current game tick
	 * @return true if character has reached its destination
	 */
	public boolean updatePosition (int gameTicks) {

		// Movement might be set to null at some point to
		// indicate that character has stopped.
		if (movement == null) {
			return true;
		}
		
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
		if (dX * dX + dY * dY <= distanceCovered * distanceCovered) {
			getPosition().set(movement.destinationX, movement.destinationY);
			movement = null;
			
			getAi().notifyEvent(Event.ARRIVED);
			
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
	
	public void teleportTo(Zone zone, int x, int y) {
		if (this.getZone() != zone) {
			this.getZone().removeObject(this);
			this.setPosition(x, y);
			zone.addObject(this);
			return;
		}
		
		this.setPosition(x, y);
		zone.updateRegion(this, true);
		this.movement = new MovementData().fillWithCurrent(this);
		getRegion().broadcastToCloseRegions(new NotifyCharacterMovement(this, true));
	}
	
	public void moveCharacterTo(GameObject object) {
		int x = (int)object.getPosition().getX();
		int y = (int)object.getPosition().getY();
		
		// Horizontal movement
		if (getPosition().getX() < object.getPosition().getX()) {
			// Target is at the right side
			x -=  (object.getWidth() + getWidth())/2; // Remove half of targets and half of our width
		} else if (getPosition().getX() > object.getPosition().getX() + object.getWidth()){
			// Target is at the left side
			x += (object.getWidth() + getWidth())/2; // Add half of targets and half of our width
		} else {
			// If we're within a target (x wise), no need to change current position
			x = (int) getPosition().getX();
		}
		
		// Vertical movement
		if (getPosition().getY() < object.getPosition().getY()) {
			y -= (object.getHeight() + getHeight())/2;
		} else if (getPosition().getY() > object.getPosition().getY() + object.getHeight()) {
			y += (object.getHeight() + getHeight())/2;
		} else {
			// If we're within a target (y wise), no need to change current position
			y = (int) getPosition().getY();
		}
		
		moveCharacterTo(x, y);
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
		getRegion().broadcastToCloseRegions(new NotifyCharacterMovement(this));
		
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
		
		/**
		 * Fills moevement data with characters current position.
		 * @param character
		 * @return
		 */
		public MovementData fillWithCurrent(GameCharacter character) {
			destinationX = (int) character.getPosition().getX();
			destinationY = (int) character.getPosition().getY();
			movementSpeed = 0;
			timeStamp = 0;
			return this;
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
	
	public int getMaxHealth() {
		return stats.getMaxHealth();
	}
	
	public int getMaxMana() {
		return stats.getMaxMana();
	}
	
	private int getMaxEnergy() {
		return stats.getMaxEnergy();
	}
	
	private int getWalkingSpeed() {
		return stats.getWalkingSpeed();
	}
	
	/**
	 * Sends a packet to every player who is targeting this object
	 * @param packet
	 */
	public void sendPacketToTargetedBy(ServerPacket packet) {
		for (GameObject object : getTargetedBy()) {
			if (object instanceof Player) {
				((Player) object).sendPacket(packet);
			}
		}
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
		onHealthChange();
	}
	
	/**
	 * @param health the health to set
	 */
	public void setHealth(int health) {
		this.health = Math.min(health, getMaxHealth());
		onHealthChange();
	}
	
	/**
	 * Get's called when character dies
	 */
	public void onDeath(GameCharacter attacker) {
		this.alive = false;
		this.ai.setIntention(Intention.IDLE);
		this.ai.stopAiTask();
		this.ai.stopAutoAttack();
		this.ai.stopFollowing();
		this.stopMoving(null);
		
		this.removeTarget();
		this.clearTargetedBy();
		
		if (isPlayer()) {
			this.sendPacket(new DeathNotification());
		}
		
		if (getType() == GameObjectType.HOSTILE_NPC) {
			// else drop loot
			Item it = ItemTable.getInstance().getItem(1);
			ItemInstance ii = ItemFactory.getInstance().createEquipableItem(it);
			ii.setPosition(getPosition());
			
			World.getInstance().getZone(0).addObject(ii);		
		}
		
		// Notify quest engine about npc being killed
		if (attacker instanceof Player) {
			if (getTemplate().getQuestEvents(QuestEventType.ON_KILL) != null) {
				for(Quest quest : getTemplate().getQuestEvents(QuestEventType.ON_KILL)) {
					String notification = quest.onKill(this, (Player)attacker);
					if (notification != null) {
						((Player)attacker).sendPacket(new EventNotification(notification));
					}
				}
			}
		}
	}
	
	/**
	 * Get's called every time after health is changed
	 */
	public void onHealthChange() {
		// Notify players who are targeting this entity
		// with new target info
		for (GameObject object : getTargetedBy()) {
			if (object instanceof Player) {
				((Player)object).sendPacket(new TargetInfoResponse(this, ((Player)object).getAvailableActions()));
			}
		}
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
	
	@Override
	public void fillInfo(ByteBuffer buffer) {
		super.fillInfo(buffer);
		buffer.putInt(this.getHealth());
		buffer.putInt(this.getMaxHealth());
	}
	
	/**
	 * Checks whether character is currently moving
	 * @return
	 */
	public boolean isMoving() {
		return movement != null;
	}
	
	/**
	 * Launches a basic physical attack (sword, bow, etc.)
	 * @param attackTarget
	 */
	public void attack(GameCharacter attackTarget) {
		if (isAttackingDisabled()) {
			return;
		}
		
		if (attackTarget.isDead() || attackTarget == null) {
			getAi().setIntention(Intention.ACTIVE);
			return;
		}
		
		boolean critical = Formulas.landedCriticalPhysical(this);
		int damage = getAttackDamage();
		
		int attackTime = 500;			// How long attack (animation) takes
		int timeBetweenAttacks = 1000;	// Time until next attack
		
		attackEndTime = GameTimeController.getInstance().getGameTicks() + (timeBetweenAttacks + attackTime)/GameTimeController.MILLIS_IN_TICK -1;
		
		Attack attack = new Attack(attackTarget.getObjectId());
		
		attack.setDamage(getAttackDamage());
		
		// Send a packet with damage to nearby characters
		attackTarget.getRegion().broadcastToCloseRegions(attack);
		
		ThreadPoolManager.getInstance().scheduleAi(new HitTask(attackTarget, damage), attackTime);
		
		ThreadPoolManager.getInstance().scheduleAi(new NotifyAiTask(Event.READY_TO_ACT), attackTime + timeBetweenAttacks);
	}
	
	/**
	 * Executes a hit to the target.
	 * @param hitTarget
	 * @param damage
	 */
	public void executeHit(GameCharacter hitTarget, int damage) {
		// Don't make the hit if I'm dead
		if (!isDead()) {
			hitTarget.addDamage(this, damage);
		}
	}
	
	/**
	 * Represents a hit task which applies a hit to the target.
	 * This is the task that actually removes health from target that was hit.
	 * @author Alvys
	 *
	 */
	public class HitTask implements Runnable {

		private GameCharacter target;
		private int damage;
		
		public HitTask(GameCharacter target, int damage) {
			this.target = target;
			this.damage = damage;
		}
		
		@Override
		public void run() {
			executeHit(target, damage);
		}
		
	}
	
	public void addDamage(GameCharacter attacker, int damage) {
		if (!isDead()) {
			this.health -= damage;
			this.getAi().notifyEvent(Event.ATTACKED, attacker, null);
			
			CharacterStatusUpdate packet = new CharacterStatusUpdate(this);
			
			// Send packet to everyone who can see targets health
			this.sendPacketToTargetedBy(packet);
			
			// If target is player, send update to him too.
			if (isPlayer()) {
				this.sendPacket(packet);
			}
			
			if (this.health < 0) {
				this.health = 0;
				onDeath(attacker);
			}
		}
	}
	
	public boolean isDead() {
		return !alive;
	}
	
	/**
	 * Stops server side movement and broadcasts a packet
	 * indicating that character has stopped.
	 * @param position
	 */
	public void stopMoving(ObjectPosition position) {
		movement = null;
		if (position != null) {
			this.getPosition().set(position.getX(), position.getY());
		}
		GameTimeController.getInstance().stopMoving(this);
		this.getRegion().broadcastToCloseRegions(new NotifyMovementStop(this));
	}
	
	/**
	 * TODO implement
	 * @return
	 */
	public boolean isMovementDisabled() {
		return false;
	}
	
	public AiData getAiData() {
		return template.getAiData();
	}
	
	public boolean isFollowing() {
		return getAi().isFollowing();
	}
	
	/**
	 * TODO implement
	 * @return
	 */
	public int getAttackRange() {
		return 10;
	}
	
	/**
	 * TODO implement
	 * @return
	 */
	public int getAttackDamage() {
		return getTemplate().getBaseStrength();
	}

	public boolean isPlayer() {
		return this instanceof Player;
	}
	
	/**
	 * Checks if delay between attacks hasn't expired
	 * @return
	 */
	public boolean isAttacking() {
		return attackEndTime > GameTimeController.getInstance().getGameTicks();
	}
	
	/**
	 * Checks whether attacking is disabled (Character rooted,
	 * stunned, etc.) TODO implement fully
	 * @return
	 */
	public boolean isAttackingDisabled() {
		return isAttacking();
	}
	
	/**
	 * Represents a scheduled notification
	 * @author Alvys
	 *
	 */
	public class NotifyAiTask implements Runnable {

		private Event event;
		
		public NotifyAiTask(Event event) {
			this.event = event;
		}
		
		@Override
		public void run() {
			getAi().notifyEvent(event);
		}
		
	}
	
	public synchronized void cleanup() {
		this.ai.stopAiTask();
	}
	
}
