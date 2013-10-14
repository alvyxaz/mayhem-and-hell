package com.friendlyblob.mayhemandhell.server.ai;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;

public class PlayerAi extends GameCharacterAi {

	public PlayerAi(Player actor) {
		super(actor);
	}
	
	@Override
	public void onEventThink() {
		System.out.println("THINKING " + getIntention());
		if (thinking) {
			return;
		}
		
		thinking = true;
		
		switch(getIntention()) {
		case ATTACK:
			onThinkAttack();
			System.out.println("PlayerAi thinking");
			break;
		}
		
		thinking = false;
	}
	
	public void onThinkAttack() {
		if (attackTarget != null) {
			
			// If we need to get closer
			if (!attackTarget.isInsideRadius(actor, actor.getAttackRange())) {
				actor.moveCharacterTo(attackTarget);
				return;
			}
			
			actor.attack(attackTarget);
		}
	}

}
