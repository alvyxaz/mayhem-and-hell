package com.friendlyblob.mayhemandhell.server.network.packets;

import java.io.IOException;

import com.friendlyblob.mayhemandhell.server.network.BaseSendablePacket;

public class Dummy extends BaseSendablePacket{

	public Dummy() {
		writeC(0x09);
		writeD(5);
		writeS("Alvys");
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}
	
}
