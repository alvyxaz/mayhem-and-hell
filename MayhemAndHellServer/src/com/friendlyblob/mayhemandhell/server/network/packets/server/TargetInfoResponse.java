package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class TargetInfoResponse extends ServerPacket {

	public String name;
	
	public TargetInfoResponse(String name) {
		this.name = name;
	}
	
	@Override
	protected void write() {
		writeC(0x07);
		writeS(name);
	}

}
