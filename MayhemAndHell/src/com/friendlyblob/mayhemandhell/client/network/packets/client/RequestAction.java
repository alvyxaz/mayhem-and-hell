package com.friendlyblob.mayhemandhell.client.network.packets.client;

import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;

public class RequestAction extends SendablePacket {

	int actionIndex;
	
	public RequestAction(int actionIndex) {
		this.actionIndex = actionIndex; 
	}
	
	@Override
	public void write() {
		writeC(0x03);
		writeD(actionIndex);
	}

	
}
