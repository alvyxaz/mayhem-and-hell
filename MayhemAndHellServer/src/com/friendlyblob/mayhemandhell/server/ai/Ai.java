package com.friendlyblob.mayhemandhell.server.ai;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.skills.Skill;
import com.friendlyblob.mayhemandhell.server.utils.ObjectPosition;

/**
 * Basic Ai implementation
 * @author Alvys
 *
 */
public abstract class Ai implements Control {

	protected final GameCharacter actor;

	protected Intention intention = Intention.IDLE;
	
	protected volatile boolean clientMoving;
	protected volatile boolean clientAutoAttacking;
	
	private GameObject target;
	protected GameCharacter attackTarget;
	protected GameCharacter followTarget;
	
	// Skill being cast at the moment
	protected Skill skill;
	
	private static final int FOLLOW_INTERVAL = 1000;
	private static final int ATTACK_FOLLOW_INTERVAL = 500;
	
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

	/**
	 * 
	 * @param intention
	 * @param arg0
	 * @param arg1
	 */
	public final void setIntention(Intention intention, Object arg0, Object arg1) {
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
				onIntentionCast((Skill) arg0, (GameCharacter) arg1);
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
	
	protected abstract void onIntentionCast(Skill skill, GameCharacter target);
	
	protected abstract void onIntentionMoveTo(ObjectPosition destination );
	
	protected abstract void onIntentionFollow(GameCharacter target);
	
	protected abstract void onIntentionPickUp(GameObject item);
	
	protected abstract void onIntentionInteract(GameObject object);
	
	
}
