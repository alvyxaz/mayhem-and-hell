package com.friendlyblob.mayhemandhell.server.ai;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.model.skills.Skill;
import com.friendlyblob.mayhemandhell.server.utils.ObjectPosition;

public class GameCharacterAi extends Ai {

	public GameCharacterAi(GameCharacter actor) {
		super(actor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setIntention(Intention intention) {
		setIntention(intention, null, null);
	}
	
	public void setIntention(Intention intention, Object arg0) {
		setIntention(intention, arg0, null);
	}

	@Override
	public void notifyEvent(Event event) {
		notifyEvent(event, null, null);
	}

	@Override
	protected void onEventThink() {
	}

	@Override
	protected void onEventArrived() {

		// If we were moving before.
		if (getIntention() == Intention.MOVE_TO) {
			setIntention(Intention.ACTIVE);
		}
		
		onEventThink();
	}

	@Override
	protected void onEventAttacked(GameCharacter attacker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onEventCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onEventConfused(GameCharacter attacker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onEventDead() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onEventEvaded(GameCharacter attacker) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Called after character finishes whatever it was that it was doing
	 * (e.g. after the attack was finished, and ready to do another one) 
	 */
	@Override
	protected void onEventReadyToAct() {
		onEventThink();
	}

	@Override
	protected void onEventRooted(GameCharacter attacker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onEvenentSilenced(GameCharacter attacker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onEventSleeping(GameCharacter attacker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onEventStunned(GameCharacter attacker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onEventUserCommand(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onEventAggression(GameCharacter attacker, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onEventAfraid(GameCharacter attacker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onIntentionIdle() {
		overrideIntention(Intention.IDLE);
		setAttackTarget(null);
		stopMoving();
		stopAutoAttack();
	}

	@Override
	protected void onIntentionActive() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onIntentionRest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onIntentionAttack(GameCharacter target) {
		if (target != null) {
			setAttackTarget(target);
			overrideIntention(Intention.ATTACK);
			notifyEvent(Event.THINK);
		}
	}

	@Override
	protected void onIntentionCast(Skill skill, GameCharacter target) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onIntentionMoveTo(ObjectPosition destination) {
		stopAutoAttack();
		actor.moveCharacterTo((int) destination.getX(), (int) destination.getY());
		overrideIntention(Intention.MOVE_TO);
	}

	@Override
	protected void onIntentionFollow(GameCharacter target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onIntentionPickUp(GameObject item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onIntentionInteract(GameObject object) {
		// TODO Auto-generated method stub
		
	}

}
