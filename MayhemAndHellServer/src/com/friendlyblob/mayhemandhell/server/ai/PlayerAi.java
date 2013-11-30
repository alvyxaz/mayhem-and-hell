package com.friendlyblob.mayhemandhell.server.ai;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;

public class PlayerAi extends GameCharacterAi {

	public PlayerAi(Player actor) {
		super(actor);
	}
	
	@Override
	protected void onEventReadyToAct() {
		super.onEventReadyToAct();
	}
	
	@Override
	public void onEventThink() {
		if (thinking) {
			return;
		}
		
		thinking = true;
		
		switch(getIntention()) {
			case ATTACK:
				onThinkAttack();
				break;
		}
		
		thinking = false;
	}
	
	@Override
	protected void onEventAttacked(GameCharacter attacker) {
		
	}
	
	/**
	 * Player Ai analyzes whether it should attack, walk to the target or else.
	 * Following does not change intention.
	 */
	public void onThinkAttack() {
		if (attackTarget != null) {
			// If we need to get closer
			if (!attackTarget.isInsideRadius(actor, actor.getAttackRange())) {
				this.startFollowing(attackTarget, actor.getAttackRange());
				return;
			}
			actor.attack(attackTarget);
		}
	}

}
