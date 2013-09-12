package com.friendlyblob.mayhemandhell.client.network.packets;

public class DummyPacket extends SendablePacket {

	@Override
	public void write() {
		writeC(0x0C);
		writeD(8);
		writeD(8);
	}

}
