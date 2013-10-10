package com.friendlyblob.mayhemandhell.server.ai;

import java.util.concurrent.Future;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.network.ThreadPoolManager;

public class AttackableAi extends GameCharacterAi implements Runnable {

	private Future<?> aiTask;
	
	// How often ai thinks (calls onEventThink())
	private final static int REFRESH_RATE = 1000;
	
	protected AttackableAi(GameCharacter actor) {
		super(actor);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onEventThink() {
		System.out.println("THINKING");
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

}
