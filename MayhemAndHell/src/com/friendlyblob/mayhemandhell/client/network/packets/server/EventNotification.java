package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class EventNotification extends ReceivablePacket {

	@Override
	public boolean read() {
		GameWorld.getInstance().game.screenGame.
			eventNotifications.addRegularNotification(readS());
		return false;
	}

	@Override
	public void run() {
	}

}
