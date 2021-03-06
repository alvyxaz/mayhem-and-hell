package com.friendlyblob.mayhemandhell.server.ai;

import java.util.concurrent.Future;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.skills.Castable;
import com.friendlyblob.mayhemandhell.server.network.ThreadPoolManager;
import com.friendlyblob.mayhemandhell.server.network.packets.server.AutoAttack;
import com.friendlyblob.mayhemandhell.server.utils.ObjectPosition;

/**
 * Basic Ai implementation
 * @author Alvys
 *
 */
public abstract class Ai implements Control {

	protected final GameCharacter actor;

	protected Intention intention = Intention.IDLE;
	
	protected volatile boolean moving;
	protected volatile boolean autoAttacking;
	
	private int movingToObjectOffset;
	
	private GameObject target;
	protected GameCharacter attackTarget;
	protected GameCharacter followTarget;
	
	// Skill being cast at the moment
	protected Castable skill;
	
	private static final int FOLLOW_INTERVAL = 1000;
	private static final int ATTACK_FOLLOW_INTERVAL = 500;
	private static final int MAX_FOLLOW_DISTANCE = 100;
	
	protected boolean thinking;
	
	protected Future<?> followTask = null;
	
	protected Ai(GameCharacter actor) {
		this.actor = actor;
	}
	
	public GameCharacter getActor() {
		return actor;
	}
	
	public Intention getIntention() {
		return intention;
	}
	
	public GameCharacter getAttackTarget() {
		return attackTarget;
	}
	
	public void setAttackTarget(GameCharacter target) {
		this.attackTarget = target;
	}
	
	public void overrideIntention(Intention intention) {
		this.intention = intention;
	}
	
	public void prepareToDetach() {
		overrideIntention(Intention.IDLE);
		actor.detachAi();
	}
	
	/**
	 * 
	 * @param intention
	 * @param arg0
	 * @param arg1
	 */
	public final void setIntention(Intention intention, Object arg0, Object arg1) {
		// Every intention, except for follow and attack, needs to break following
		if (intention != Intention.FOLLOW && intention != Intention.ATTACK) {
			stopFollowing();
		}
		
		switch(intention) {
			case IDLE:
				onIntentionIdle();
				break;
			case ACTIVE:
				onIntentionActive();
				break;
			case REST:
				onIntentionRest();
				break;
			case ATTACK:
				onIntentionAttack((GameCharacter) arg0);
				break;
			case CAST:
				onIntentionCast((Castable) arg0, (GameCharacter) arg1);
				break;
			case MOVE_TO:
				onIntentionMoveTo((ObjectPosition) arg0);
				break;
			case FOLLOW:
				onIntentionFollow((GameCharacter) arg0);
				break;
			case PICK_UP:
				onIntentionPickUp((GameObject) arg0);
				break;
			case INTERACT:
				onIntentionInteract((GameObject) arg0);
				break;
		}
	}
	
	public final void notifyEvent(Event event, Object arg0, Object arg1) {
		switch (event) {
			case THINK:
				onEventThink();
			break;
			case ARRIVED:
				onEventArrived();
				break;
			case ATTACKED:
				onEventAttacked((GameCharacter) arg0);
				break;
			case CANCEL:
				onEventCancel();
				break;
			case CONFUSED:
				onEventConfused((GameCharacter) arg0);
				break;
			case DEAD:
				onEventDead();
				break;
			case EVADED:
				onEventEvaded((GameCharacter) arg0);
				break;
			case READY_TO_ACT:
				onEventReadyToAct();
				break;
			case ROOTED:
				onEventRooted((GameCharacter) arg0);
				break;
			case SILENCED:
				onEvenentSilenced((GameCharacter) arg0);
				break;
			case SLEEPING:
				onEventSleeping((GameCharacter) arg0);
				break;
			case STUNNED:
				onEventStunned((GameCharacter) arg0);
				break;
			case USSER_COMMAND:
				onEventUserCommand(arg0, arg1);
				break;
			case AGGRESSION:
				onEventAggression((GameCharacter) arg0, ((Number) arg1).intValue());
				break;
			case AFRAID:
				onEventAfraid((GameCharacter) arg0);
				break;
		}
	}
	
	protected abstract void onEventThink();
	
	protected abstract void onEventArrived();
	
	protected abstract void onEventAttacked(GameCharacter attacker);
	
	protected abstract void onEventCancel();
	
	protected abstract void onEventConfused(GameCharacter attacker);
	
	protected abstract void onEventDead();
	
	protected abstract void onEventEvaded(GameCharacter attacker);
	
	protected abstract void onEventReadyToAct();
	
	protected abstract void onEventRooted(GameCharacter attacker);
	
	protected abstract void onEvenentSilenced(GameCharacter attacker);
	
	protected abstract void onEventSleeping(GameCharacter attacker);
	
	protected abstract void onEventStunned(GameCharacter attacker);
	
	protected abstract void onEventUserCommand(Object arg0, Object arg1);
	
	protected abstract void onEventAggression(GameCharacter attacker, int amount);
	
	protected abstract void onEventAfraid(GameCharacter attacker);
	
	protected abstract void onIntentionIdle();

	protected abstract void onIntentionActive();
	
	protected abstract void onIntentionRest();
	
	protected abstract void onIntentionAttack(GameCharacter target);
	
	protected abstract void onIntentionCast(Castable skill, GameCharacter target);
	
	protected abstract void onIntentionMoveTo(ObjectPosition destination );
	
	protected abstract void onIntentionFollow(GameCharacter target);
	
	protected abstract void onIntentionPickUp(GameObject item);
	
	protected abstract void onIntentionInteract(GameObject object);
	
	protected void stopMoving() {
		if (actor.isMoving()) {
			actor.stopMoving(actor.getPosition());
		}
	}
	
	public boolean isAutoAttacking() {
		return autoAttacking;
	}
	
	public void setAutoAttacking(boolean autoAttacking) {
		this.autoAttacking = autoAttacking; 
	}
	
	public void startAutoAttack() {
		if (!isAutoAttacking()) {
			setAutoAttacking(true);
				// Send a notification to nearby characters, indicating that auto attack was started.
				actor.getRegion().broadcastToCloseRegions(
						new AutoAttack(actor.getObjectId(), true));
				
				setIntention(Intention.ATTACK, actor.getTarget(), null);
		}
	}
	
	public void stopAutoAttack() {
		if (isAutoAttacking()) {
			setAutoAttacking(false);
		}
	}
	
	public void notifyDead() {
		intention = intention.IDLE;
		
		// Stop following, skill casting and etc.
	}
	
	/**
	 * TODO implement some sort of a limit of how many times per
	 * second can we move to an object. Probably the best way to do it
	 * is calculate an approximate game tick after which to update.
	 * @param object
	 * @param offset
	 */
	public void moveToObject(GameObject object, int offset) {
		if (actor.isMovementDisabled()) {
			return;
		}

		moving = true;
		movingToObjectOffset = offset;
		target = object;
		actor.moveCharacterTo(object);
	}
	
	public void startFollowing(GameCharacter target) {
		this.startFollowing(target, FollowTask.DEFAULT_RANGE);
	}
	
	public synchronized void startFollowing(GameCharacter target, int range) {
		if (followTask != null) {
			followTask.cancel(false);
			followTask = null;
		}
		followTarget = target;
		int followInterval = getIntention() == Intention.ATTACK ? ATTACK_FOLLOW_INTERVAL  : FOLLOW_INTERVAL;
		followTask = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new FollowTask(range), 5, followInterval);
	}
	
	public GameCharacter getFollowTarget() {
		return followTarget;
	}
	
	public GameObject getTarget() {
		return target;
	}
	
	public synchronized void stopFollowing() {
		if (followTask != null) {
			// Stop the Follow Task
			followTask.cancel(false);
			followTask = null;
		}
		followTarget = null;
	}
	
	public void stopAiTask() {
		stopFollowing();
	}
	
	public boolean isFollowing() {
		return followTask != null;
	}
	
	private class FollowTask implements Runnable {
		public static final int DEFAULT_RANGE = 10;
		int range;
		
		public FollowTask() {
			range = DEFAULT_RANGE;
		}
		
		public FollowTask(int range) {
			this.range = range;
		}
		
		@Override
		public void run() {
			if (followTask == null) {
				return;
			}
			
			GameCharacter target = followTarget;
			
			// If there's no target
			if (target == null) {
				setIntention(Intention.IDLE);
				return;
			}
			
			if (!actor.isInsideRadius(target, range)) {
				// If target is too far to be followed
				if (!actor.isInsideRadius(target, MAX_FOLLOW_DISTANCE)) {
					setIntention(Intention.IDLE);
					return;
				}

				moveToObject(target, range);
			}
		}
	}
	
}
