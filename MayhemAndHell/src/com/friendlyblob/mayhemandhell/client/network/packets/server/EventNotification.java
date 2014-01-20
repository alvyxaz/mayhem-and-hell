package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class EventNotification extends ReceivablePacket {

	private String notification;
	
	@Override
	public boolean read() {
		notification = readS();
		return true;
	}

	@Override
	public void run() {
		GameWorld.getInstance().game.screenGame.
		eventNotifications.addRegularNotification(notification);
	}

}
