package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class ActionFailedMessage extends ServerPacket {

	public String message;
	public short errorType;
	
	public ActionFailedMessage(String message) {
		this(message, 0);
	}
	
	public ActionFailedMessage(String message, int errorType) {
		this.message = message;
		this.errorType = (short) errorType;
	}
	
	@Override
	protected void write() {
		writeC(0xFF);
		writeC(errorType);
		writeS(message);
	}

}
