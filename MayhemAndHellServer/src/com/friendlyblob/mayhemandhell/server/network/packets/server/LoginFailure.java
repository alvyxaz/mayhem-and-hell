package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class LoginFailure extends ServerPacket {
	
	private String message;
	
	public LoginFailure(String message) {
		this.message = message;
	}
	
	@Override
	protected void write() {
		writeC(0x16);
		writeS(message);
	}
}
