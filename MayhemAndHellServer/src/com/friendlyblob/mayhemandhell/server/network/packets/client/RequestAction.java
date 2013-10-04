package com.friendlyblob.mayhemandhell.server.network.packets.client;

import com.friendlyblob.mayhemandhell.server.actions.GameActions.GameAction;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.ActionFailedMessage;

public class RequestAction extends ClientPacket {
	int actionIndex;
	
	@Override
	protected boolean read() {
		actionIndex = readD();
		return true;
	}

	@Override
	public void run() {
		GameAction[] actions = getClient().getPlayer().getAvailableActions();
		if (actions != null && actionIndex < actions.length && actionIndex >= 0) {
			// TODO find a better way AND place to switch between different actions
			if (actions[actionIndex] == GameAction.ATTACK) {
				getClient().getPlayer().walkBy(getClient().getPlayer().getTarget());
			}
		} else {
			getClient().sendPacket(new ActionFailedMessage("[RequestAction packet] Invalid action"));
		}
	}
	

}
