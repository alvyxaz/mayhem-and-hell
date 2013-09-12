package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;


public class ServerClose extends ServerPacket {
	
	public static final ServerClose STATIC_PACKET = new ServerClose();
	
	private ServerClose() {
	}

	@Override
	protected void write() {
		writeC(0x20);
	}

}
