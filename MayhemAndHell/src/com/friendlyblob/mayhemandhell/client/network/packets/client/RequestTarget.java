package com.friendlyblob.mayhemandhell.client.network.packets.client;

import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;


public class RequestTarget extends SendablePacket {

	int objectId;
	
	public RequestTarget(int objectId) {
		this.objectId = objectId;
	}
	
	@Override
	public void write() {
		writeC(0x02);
		writeD(objectId);
	}

}
