package com.friendlyblob.mayhemandhell.server.network.packets.client;

import com.friendlyblob.mayhemandhell.server.model.World;
import com.friendlyblob.mayhemandhell.server.network.GameClient.GameClientState;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;

public class NotifyReadyToPlay extends ClientPacket {

	@Override
	protected boolean read() {
		return true;
	}

	@Override
	public void run() {
		getClient().setState(GameClientState.IN_GAME);
		World.getInstance().addPlayer(getClient().getPlayer());
	}

}
