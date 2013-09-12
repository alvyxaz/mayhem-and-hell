package com.friendlyblob.mayhemandhell.client.network.packets.client;

import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;

public class ClientVersion extends SendablePacket {

	private int version;
	
	public ClientVersion(int version) {
		this.version = version;
	}
	
	@Override
	public void write() {
		writeC(0x01);
		writeD(version); // TODO add a real version
	}
	
}
