package com.friendlyblob.mayhemandhell.server.model.actors;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.Future;

import com.friendlyblob.mayhemandhell.server.GameTimeController;
import com.friendlyblob.mayhemandhell.server.ai.AiData;
import com.friendlyblob.mayhemandhell.server.ai.Event;
import com.friendlyblob.mayhemandhell.server.ai.GameCharacterAi;
import com.friendlyblob.mayhemandhell.server.ai.Intention;
import com.friendlyblob.mayhemandhell.server.factories.ItemFactory;
import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.World;
import com.friendlyblob.mayhemandhell.server.model.Zone;
import com.friendlyblob.mayhemandhell.server.model.actors.instances.NpcAttackableInstance;
import com.friendlyblob.mayhemandhell.server.model.actors.instances.NpcInstance;
import com.friendlyblob.mayhemandhell.server.model.datatables.DialogTable;
import com.friendlyblob.mayhemandhell.server.model.datatables.ItemTable;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog;
import com.friendlyblob.mayhemandhell.server.model.instances.ItemInstance;
import com.friendlyblob.mayhemandhell.server.model.items.Item;
import com.friendlyblob.mayhemandhell.server.model.logic.Formulas;
import com.friendlyblob.mayhemandhell.server.model.quests.Quest;
import com.friendlyblob.mayhemandhell.server.model.quests.Quest.QuestEventType;
import com.friendlyblob.mayhemandhell.server.model.quests.QuestState;
import com.friendlyblob.mayhemandhell.server.model.resources.Resource.ResourceSkill;
import com.friendlyblob.mayhemandhell.server.model.skills.Castable;
import com.friendlyblob.mayhemandhell.server.model.stats.CharacterStats;
import com.friendlyblob.mayhemandhell.server.network.ThreadPoolManager;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.Attack;
import com.friendlyblob.mayhemandhell.server.network.packets.server.CharacterStatusUpdate;
import com.friendlyblob.mayhemandhell.server.network.packets.server.DeathNotification;
import com.friendlyblob.mayhemandhell.server.network.packets.server.DialogPageInfo;
import com.friendlyblob.mayhemandhell.server.network.packets.server.EventNotification;
import com.friendlyblob.mayhemandhell.server.network.packets.server.NotifyCharacterMovement;
import com.friendlyblob.mayhemandhell.server.network.packets.server.NotifyMovementStop;
import com.friendlyblob.mayhemandhell.server.network.packets.server.StartCasting;
import com.friendlyblob.mayhemandhell.server.network.packets.server.TargetInfoResponse;
import com.friendlyblob.mayhemandhell.server.network.packets.server.UpdateInventorySlot;
import com.friendlyblob.mayhemandhell.server.utils.ObjectPosition;

/*
 * Parent of players and npc's
 */
public class GameCharacter extends GameObject{

	private int health;
	private int energy;
	private int mana;
	
	protected boolean alive;
	
	protected GameCharacterAi ai;
	private CharacterStats stats;
	private CharacterTemplate template;
	
	private MovementData movement;
	private DestinationReachedTask destinationTask;
	
	private int attackEndTime;
	private boolean attackAborted;
	
	// Casting related
	private boolean casting; 		// Whether character is casting something right now
	private Future<?> castingTask;	// Task of executing a result of casting
	
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
	 * TODO implement fully
	 * @param object
	 * @return true if character is hostile to this entity
	 */
	public boolean isHostile(GameObject object) {
		return object instanceof NpcAttackableInstance;
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
		
		int dX = (int)(movement.destinationX - getPosition().getX());
		int dY = (int)(movement.destinationY - getPosition().getY());
		
		if (movement.type == MovementType.DIRECTION) {
			if (movement.direction == Direction.NONE) {
				stopMoving(new ObjectPosition((int)movement.destinationX, (int)movement.destinationY));
				return true;
			}
			getPosition().offsetByAngle(movement.direction.getAngle(), distanceCovered);
			
			// Revert position if it's collision
			int tile = getZone().getTemplate().tileAtPosition(this.getPosition());
			if (getZone().getTemplate().getTiles()[tile].isCollision()) {
				getPosition().offsetByAngle(movement.direction.getAngle(), -distanceCovered);
				stopMoving(getPosition());
				return true;
			}
			
			return false;
		}
		
		// Moving a character 
		getPosition().offsetByAngle(angle, distanceCovered);
		
		// Check if destination is reached
		if (dX * dX + dY * dY <= distanceCovered * distanceCovered) {
			
			// Checking if moving on path
			if (movement.type == MovementType.PATH) {
				// If we arrived at the last tile
				if(movement.advanceOnPath()) {
					stopMoving(new ObjectPosition((int)movement.destinationX, (int)movement.destinationY));
					getAi().notifyEvent(Event.ARRIVED);
					return true;
				} else {
					// Set destination to another tile
					int nextTile = movement.getNextTile();
					movement.destinationX = getZone().getTemplate().xPositionOfTile(nextTile);
					movement.destinationY = getZone().getTemplate().yPositionOfTile(nextTile);
					
					// Notify nearby characters about movement
					getRegion().broadcastToCloseRegions(new NotifyCharacterMovement(this));
				}
				
			} else {
				stopMoving(new ObjectPosition((int)movement.destinationX, (int)movement.destinationY));
				getAi().notifyEvent(Event.ARRIVED);
				return true;
			}
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
		this.destinationTask = null;
		stopActions();
		
		MovementData movementData = new MovementData(MovementType.DESTINATION);
		movementData.destinationX = x;
		movementData.destinationY = y;
		movementData.movementSpeed = getMovementSpeed();
		movementData.timeStamp = GameTimeController.getInstance().getGameTicks();
		
		// Path finder related
		if (this instanceof Player) {
			int sourceTile = getZone().getTemplate().tileAtPosition(this.getPosition());
			int destinationTile = getZone().getTemplate().tileAtPosition(new ObjectPosition(x, y));
			int[] path = getZone().getTemplate().calculatePathBetween(sourceTile, destinationTile);
			if (path == null) {
				return false;
			}
			movementData.pathMode(path);
			movementData.destinationX = getZone().getTemplate().xPositionOfTile(path[0]);
			movementData.destinationY = getZone().getTemplate().yPositionOfTile(path[0]);
		}
		
		moveCharacterTo(movementData);
		
		return true;
	}
	
	public boolean moveCharacterTo(Direction direction) {
		if (direction == Direction.NONE) {
			stopMoving(getPosition());
			return true;
		}
		this.destinationTask = null;
		stopActions();
		
		int distanceInDirection = direction == Direction.NONE ? 0 : 10000; // Should be relatively big
		
		// Preparing current position
		ObjectPosition position = new ObjectPosition();
		position.set(getPosition().getX(), getPosition().getY());
		position.offsetByAngle(direction.getAngle(), distanceInDirection);
		
		MovementData movementData = new MovementData(MovementType.DESTINATION);
		movementData.destinationX = position.getX();
		movementData.destinationY = position.getY();
		movementData.movementSpeed = getMovementSpeed();
		movementData.timeStamp = GameTimeController.getInstance().getGameTicks();
		movementData.directionMode(direction);
		
		moveCharacterTo(movementData);
		
		return true;
	}
	
	/**
	 * Move character to a target, and execute task after it arrives.
	 * @param object target to move to
	 * @param task task to be executed
	 */
	public void moveCharacterTo(GameObject object, DestinationReachedTask task) {
		moveCharacterTo(object);
		this.destinationTask = task;
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

	/**
	 * Stop all current actions of the character (casting, auto attacking and etc)
	 */
	public void stopActions() {
		getAi().stopAutoAttack();
		abortCast();
	}
	
	public void teleportTo(Zone zone, int x, int y) {
		stopActions();
		
		if (this.getZone() != zone) {
			this.getZone().removeObject(this);
			this.setPosition(x, y);
			zone.addObject(this);
			return;
		}
		
		this.setPosition(x, y);
		zone.updateRegion(this, true);
		this.movement = new MovementData(MovementType.DESTINATION).fillWithCurrent(this);
		getRegion().broadcastToCloseRegions(new NotifyCharacterMovement(this, true));
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
	
	public int getMaxEnergy() {
		return stats.getMaxEnergy();
	}
	
	public int getWalkingSpeed() {
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
		this.abortAttack();
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
	 * Called when character reaches it's destination, but not
	 * every time it stops (Does not get called if you are stopped by other forces,
	 * only when you arrive at the target)
	 */
	public void onDestinationReached() {
		if (destinationTask != null) {
			destinationTask.execute();
			destinationTask = null;
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
		
		if (attackTarget == null || attackTarget.isDead()) {
			getAi().setIntention(Intention.ACTIVE);
			return;
		}
		
		boolean critical = Formulas.landedCriticalPhysical(this);
		int damage = getAttackDamage();
		
		int attackTime = 500;			// How long attack (animation) takes
		int timeBetweenAttacks = 1000;	// Time until next attack
		
		attackEndTime = GameTimeController.getInstance().getGameTicks() + (timeBetweenAttacks + attackTime)/GameTimeController.MILLIS_IN_TICK -1;
		
		Attack attack = new Attack(
				attackTarget.getObjectId(),
				getObjectId(),
				getPosition().angleTo(attackTarget.getPosition()));
		
		attack.setDamage(getAttackDamage());
		
		// Send a packet with damage to nearby characters
		attackTarget.getRegion().broadcastToCloseRegions(attack);
		
		setAttackAborted(false);
		
		ThreadPoolManager.getInstance().scheduleAi(new HitTask(attackTarget, damage), attackTime);
		ThreadPoolManager.getInstance().scheduleAi(new NotifyAiTask(Event.READY_TO_ACT), attackTime + timeBetweenAttacks);
	}
	
	public void cast(Castable skill) {
		// TODO handle picking out a target and etc.
		startCasting(skill, getTarget());
	}
	
	public void startCasting(Castable skill, GameObject target) {
		if (isCasting()) {
			return;
		}
		
		setCasting(true);
		
		// Notify client that casting is started
		this.sendPacket(new StartCasting(skill));
		
		// Create a casting task
		castingTask = ThreadPoolManager.getInstance().scheduleEffect(
				new CastTask(this, target, skill), 
				skill.getCastingTime());
	}
	
	public final void abortCast() {
		if (isCasting()) {
			Future<?> future = castingTask;
			
			if (future != null) {
				future.cancel(true);
			}
			
			setCasting(false);
			this.getRegion().broadcastToCloseRegions(new StartCasting(Castable.STOP_CASTING));
		}
	}
	
	public class CastTask implements Runnable {
		private GameCharacter caster;
		private GameObject target;
		private Castable skill;
		
		public CastTask(GameCharacter caster, GameObject target, Castable skill) {
			this.caster = caster;
			this.target = target;
			this.skill = skill;
		}
		
		@Override
		public void run() {
			skill.execute(caster, target);
			caster.setCasting(false);
		}
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
			if (!isAttackAborted()) {
				executeHit(target, damage);
			}
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
			
			if (this.health <= 0) {
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
	
	public CharacterHint getHint(Player player) {
		List<Quest> startingQuests = this.getTemplate().getQuestEvents(QuestEventType.QUEST_START);
		List<Quest> completingQuests = this.getTemplate().getQuestEvents(QuestEventType.QUEST_COMPLETE);
		
		CharacterHint tempHint = CharacterHint.NONE;
		
		if (completingQuests != null) {
			for(Quest quest : completingQuests) {
				QuestState state = player.getQuestState(quest.getQuestId());
				if(state != null && state.isTurnIn()) {
					return CharacterHint.QUEST_RETURN;
				}
			}
		}
		if (startingQuests != null) {
			for(Quest quest : startingQuests) {
				QuestState state = player.getQuestState(quest.getQuestId());
				if( state == null) {
					return CharacterHint.QUEST_GIVE;
				} else if(!state.isCompleted() && !state.isTurnIn()) {
					tempHint = CharacterHint.QUEST_IN_PROGRESS;
				}
			}
		}
		
		return tempHint;
	}
	
	public enum CharacterHint {
		NONE(0),
		QUEST_GIVE(1),
		QUEST_RETURN(2),
		QUEST_IN_PROGRESS(3),
		QUEST_ATTACK(4);
		
		public final int value;
		
		CharacterHint(int value) {
			this.value = value;
		}
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
	
	public void abortAttack() {
		attackAborted = true;
	}
	
	public void setAttackAborted(boolean attackAborted) {
		this.attackAborted = attackAborted;
	}
	
	public boolean isAttackAborted() {
		return attackAborted;
	}
	
	
	public boolean isCasting() {
		return casting;
	}

	public void setCasting(boolean casting) {
		this.casting = casting;
	}

	/**
	 * Represents a task that is executed right after destination is reached.
	 * @author Alvys
	 *
	 */
	public static class DestinationReachedTask {
		private GameCharacter actor;
		private Intention intention;
		private GameObject target;
		private Object argument;
		
		public DestinationReachedTask(GameCharacter actor, Intention intention, GameObject target, Object argument) {
			this.actor = actor;
			this.intention = intention;
			this.target = target;
			this.argument = argument;
			
			// In case this actor had any other intentions, let's override them.
			actor.getAi().setIntention(Intention.ACTIVE);
		}
		
		public void execute() {
			switch(intention) {
				case CAST:
					if (argument != null) {
						Castable spell = (Castable) argument;
						actor.startCasting(spell, target);
					}
					break;
				case IDLE:
					break;
				case INTERACT:
					if (argument instanceof Dialog) {
						Dialog dialog = (Dialog) argument;
						Player player = (Player) actor;
						
						player.setDialog(dialog);
						player.sendPacket(new DialogPageInfo(target.getName(), 
								player.getDialog().getPage(0), player.getDialog().getPage(0).getLinks(player)));
					}
					break;
				case PICK_UP:
					Player player = (Player) actor;
					ItemInstance item = (ItemInstance) argument;
					
					if (player.getInventory().addItem(item) != -1) {
						World.getInstance().removeObject(item);
					}
					
					int slot = player.getInventory().addItem(item);
					
					if (slot != -1) {
						player.sendPacket(new UpdateInventorySlot(slot, item.getItemId()));
					}
					
					break;
			}
		}
	}
	
	public class MovementData {
		public float destinationX;
		public float destinationY;
		public int movementSpeed;
		public int timeStamp;
		
		public MovementType type;
		
		// Path mode data
		public int[] tilePath;
		private int nextTileIndex;
		
		// Direction mode data
		private Direction direction;
		
		public MovementData(MovementType type) {
			this.type = type;
		}
		
		/**
		 * Advances movement to another tile
		 * @return true if arrived at the final destination
		 */
		public boolean advanceOnPath() {
			nextTileIndex++;
			if (nextTileIndex >= tilePath.length) {
				return true;
			}
			return false;
		}
		
		public int getNextTile() {
			return tilePath[nextTileIndex];
		}
		
		/**
		 * Enables walking through a set of tiles
		 * @param tilePath
		 */
		public void pathMode(int[] tilePath) {
			type = MovementType.PATH;
			this.tilePath = tilePath;
			this.nextTileIndex = 0;
		}
		
		/**
		 * Enables direction
		 * @param direction
		 */
		public void directionMode(Direction direction) {
			type = MovementType.DIRECTION;
			this.direction = direction;
		}
		
		/**
		 * Fills movement data with characters current position.
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
	
	public enum MovementType {
		DESTINATION,	// Moving to an exact destination
		DIRECTION, 		// Moving in a certain direction
		PATH,			// Following a path
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
	
	public static enum Direction {
		NONE(0),
		UP((float)Math.PI/2),
		UP_RIGHT((float)Math.PI/4),
		RIGHT(0),
		DOWN_RIGHT(-(float)Math.PI/4),
		DOWN(-(float)Math.PI/2),
		DOWN_LEFT((-(float)Math.PI/4)*3),
		LEFT(-(float)Math.PI),
		UP_LEFT(((float)Math.PI/4)*3);
		
		Direction(float angle) {
			this.angle = angle;
		}
		
		public float getAngle() {
			return angle;
		}
		
		private float angle;
	}
	
	public synchronized void cleanup() {
		this.ai.stopAiTask();
	}
	
}
