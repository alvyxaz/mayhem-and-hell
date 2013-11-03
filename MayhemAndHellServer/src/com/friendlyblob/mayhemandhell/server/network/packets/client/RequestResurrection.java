package com.friendlyblob.mayhemandhell.server.network.packets.client;

import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.DeathNotification;

public class RequestResurrection extends ClientPacket {

	@Override
	protected boolean read() {
		return true;
	}

	@Override
	public void run() {
		getClient().sendPacket(new DeathNotification(true));
		getClient().getPlayer().resurrect();
	}

}
