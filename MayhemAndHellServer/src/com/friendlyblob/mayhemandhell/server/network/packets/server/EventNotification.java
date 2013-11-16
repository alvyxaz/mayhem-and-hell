package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class EventNotification extends ServerPacket {

	private String message;
	
	public EventNotification(String message) {
		this.message = message;
	}
	
	@Override
	protected void write() {
		writeC(0x0F);
		writeS(message);
	}

}
