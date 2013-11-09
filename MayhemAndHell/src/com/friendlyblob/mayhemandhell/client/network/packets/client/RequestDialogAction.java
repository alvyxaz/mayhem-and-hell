package com.friendlyblob.mayhemandhell.client.network.packets.client;

import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;

public class RequestDialogAction extends SendablePacket{

	private int index;
	private int currentPage;
	
	public RequestDialogAction(int index, int currentPage) {
		this.index = index;
		this.currentPage = currentPage;
	}
	
	@Override
	public void write() {
		writeC(0x06);
		writeD(index);
		writeD(currentPage);
	}

}
