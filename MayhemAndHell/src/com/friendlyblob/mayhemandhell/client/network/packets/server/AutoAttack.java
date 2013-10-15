package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class AutoAttack extends ReceivablePacket {

	@Override
	public boolean read() {
		readD();	// ObjectId
		readC();	// 1 if starting
		return false;
	}

	@Override
	public void run() {
	}

}
