package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class RegistrationSuccessful extends ServerPacket {
	
	@Override
	protected void write() {
		writeC(0x14);
	}

}
