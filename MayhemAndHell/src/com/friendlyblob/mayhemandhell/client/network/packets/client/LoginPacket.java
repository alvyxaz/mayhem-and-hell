package com.friendlyblob.mayhemandhell.client.network.packets.client;

import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;

public class LoginPacket extends SendablePacket{

	@Override
	public void write() {
		writeC(0x02);
		writeS("loginas");
		writeS("labas");
		// TODO hash password before sending
	}

}
