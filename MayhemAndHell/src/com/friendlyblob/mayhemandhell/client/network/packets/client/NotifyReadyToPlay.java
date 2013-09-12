package com.friendlyblob.mayhemandhell.client.network.packets.client;

import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;

public class NotifyReadyToPlay extends SendablePacket {

	@Override
	public void write() {
		writeC(0x01);
	}

}
