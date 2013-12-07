package com.friendlyblob.mayhemandhell.client.network.packets.client;

import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;

public class RequestBuyItem extends SendablePacket {

	int itemId;
	
	public RequestBuyItem(int itemId) {
		this.itemId = itemId; 
	}
	
	@Override
	public void write() {
		writeC(0x08);
		writeD(itemId);
	}
}
