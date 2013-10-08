package com.friendlyblob.mayhemandhell.server.ai;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;

/**
 * Required methods for a basic AI implementation
 * @author Alvys
 *
 */
public interface Control {
	
	public GameCharacter getActor();
	
	public Intention getIntention();
	
	public GameCharacter getAttackTarget();
	
	public void setIntention(Intention intention);
	
	void notifyEvent(Event event);
	
}
