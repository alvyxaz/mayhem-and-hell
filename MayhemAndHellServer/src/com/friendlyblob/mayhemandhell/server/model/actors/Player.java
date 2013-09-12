package com.friendlyblob.mayhemandhell.server.model.actors;

import com.friendlyblob.mayhemandhell.server.actions.GameActions;
import com.friendlyblob.mayhemandhell.server.actions.GameActions.GameAction;
import com.friendlyblob.mayhemandhell.server.network.GameClient;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class Player extends GameCharacter {
	
	private GameClient client = null;

	public Player() {
		super();
		this.setObjectId((int)(Math.random()*2000)); // TODO put a real id
		this.setType(GameObjectType.PLAYER);
	}

	public void setClient(GameClient client) {
		this.client = client;
	}
	
	public GameClient getClient() {
		return client;
	}
	
	@Override
	public void sendPacket(ServerPacket packet) {
		getClient().sendPacket(packet);
	}
	
	/**
	 * Analyses a target and returns a list of available actions.
	 * TODO If user cannot attack, don't send attack action as available and etc.
	 * @return
	 */
	public GameAction[] getAvailableActions() {
		if (getTarget() != null) {
			switch (getTarget().getType()) {
			case PLAYER:
				return GameActions.onPlayer;
			}
		}
		return null;
	}
	
	/**
	 * Returns whether or not the given action is allowed for this player
	 * @param action
	 * @return
	 */
	public boolean isActionAllowed(GameAction action) {
		return true;
	}
	
}
