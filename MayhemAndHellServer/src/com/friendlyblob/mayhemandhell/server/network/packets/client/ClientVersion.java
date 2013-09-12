package com.friendlyblob.mayhemandhell.server.network.packets.client;

import com.friendlyblob.mayhemandhell.server.network.GameClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.KeyPacket;

public class ClientVersion extends GameClientPacket{

	int version = -1;
	
	@Override
	protected boolean read() {
		version = readD();
		return true;
	}

	@Override
	public void run() {
		// TODO Check if version is correct
		sendPacket(new KeyPacket(getClient().enableCrypt()));
	}

}
