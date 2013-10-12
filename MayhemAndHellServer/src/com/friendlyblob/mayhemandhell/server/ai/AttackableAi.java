package com.friendlyblob.mayhemandhell.server.ai;

import java.util.Random;
import java.util.concurrent.Future;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.instances.NpcInstance;
import com.friendlyblob.mayhemandhell.server.network.ThreadPoolManager;
import com.friendlyblob.mayhemandhell.server.utils.ObjectPosition;

public class AttackableAi extends GameCharacterAi implements Runnable {

	private Future<?> aiTask;
	
	// How often ai thinks (calls onEventThink())
	private final static int REFRESH_RATE = 1000;
	
	private static final Random random = new Random();
	
	public AttackableAi(GameCharacter actor) {
		super(actor);
		overrideIntention(Intention.ACTIVE);
		startAiTask();
	}
	
	@Override
	protected void onEventThink() {
		if (thinking) {
			return;
		}
		
		thinking = true;
		
		switch(getIntention()) {
			case ACTIVE:
				onThinkActive();
				break;
			case ATTACK:
				break;
		}
		
		thinking = false;
	}

	/**
	 * Thinking, when Ai is attached, but no particular "command" was given.
	 * Most likely, Ai is waiting for someone to walk close to it or attack it.
	 */
	public void onThinkActive() {
		if (actor.getAiData().isWalkingRandomly()) {
			if (random.nextInt(actor.getAiData().getRandomWalkRate()) == 0) {
				
				// TODO Take care of other types of characters
				if (actor instanceof NpcInstance) {
					// Fetching a newly generated calculation from Spawn
					ObjectPosition destination = ((NpcInstance)actor)
							.getSpawn().getRandomPositionWithinRadius(actor.getPosition(), 
									actor.getAiData().getMaxSingleWalkRadius());
					
					actor.moveCharacterTo((int)destination.getX(), (int)destination.getY());
					setIntention(Intention.MOVE_TO, destination);
				}
				
			}
		}
	}
	
	@Override
	public void run() {
		onEventThink();
	}
	
	public void startAiTask() {
		if (aiTask == null) {
			aiTask = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(this, 1000, REFRESH_RATE);
		}
	}
	
	@Override
	public void prepareToDetach() {
		if (aiTask != null) {
			aiTask.cancel(false);
			aiTask = null;
		}
		super.prepareToDetach();
	}

}
