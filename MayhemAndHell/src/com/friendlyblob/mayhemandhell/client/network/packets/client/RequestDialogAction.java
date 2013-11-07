package com.friendlyblob.mayhemandhell.client.network.packets.client;

import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;

public class RequestDialogAction extends SendablePacket{

	private int index;
	
	public RequestDialogAction(int index) {
		this.index = index;
	}
	
	@Override
	public void write() {
		writeC(0x06);
		writeD(index);
	}

}
