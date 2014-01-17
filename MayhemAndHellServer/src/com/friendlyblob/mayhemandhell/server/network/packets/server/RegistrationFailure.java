package com.friendlyblob.mayhemandhell.server.network.packets.server;

import java.util.List;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class RegistrationFailure extends ServerPacket {
	
	private String message;
	
	public RegistrationFailure(String message) {
		this.message = message;
	}
	
	@Override
	protected void write() {
		writeC(0x15);
		writeS(message);
	}

}
