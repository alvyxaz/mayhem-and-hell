package com.friendlyblob.mayhemandhell.client.network.packets.client;

import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;

public class RequestResurrection extends SendablePacket {

	@Override
	public void write() {
		writeC(0x05);
	}
	
}
