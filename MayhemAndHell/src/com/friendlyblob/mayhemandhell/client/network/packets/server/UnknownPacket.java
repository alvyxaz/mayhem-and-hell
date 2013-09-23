package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class UnknownPacket extends ReceivablePacket{

	@Override
	public boolean read() {
		System.out.println("UNKNOWN PACKET RECEIVED");
		return false;
	}

	@Override
	public void run() {
	}

}
