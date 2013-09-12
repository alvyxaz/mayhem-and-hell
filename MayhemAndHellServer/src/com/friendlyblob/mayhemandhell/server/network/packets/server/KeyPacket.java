package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class KeyPacket extends ServerPacket {

	byte [] key;
	
	public KeyPacket( byte [] key) {
		this.key = key;
	}
	
	@Override
	protected void write() {
		writeC(0x01);
		for(int i = 0; i < 8; i++){
			writeC(key[i]);
		}
	}
	
}
