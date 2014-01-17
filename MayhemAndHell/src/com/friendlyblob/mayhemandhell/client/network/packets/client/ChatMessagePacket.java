package com.friendlyblob.mayhemandhell.client.network.packets.client;

import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;

public class ChatMessagePacket extends SendablePacket {

	private String msg;
	
	public ChatMessagePacket(String message) {
		this.msg = message;
	}
	
	@Override
	public void write() {
		writeC(0x04);
		writeS(msg);
	}
}
