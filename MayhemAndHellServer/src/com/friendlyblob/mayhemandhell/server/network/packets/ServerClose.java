package com.friendlyblob.mayhemandhell.server.network.packets;



public class ServerClose extends ServerPacket {
	
	public static final ServerClose STATIC_PACKET = new ServerClose();
	
	protected void write() {
		writeC(0x20);
	}

}
