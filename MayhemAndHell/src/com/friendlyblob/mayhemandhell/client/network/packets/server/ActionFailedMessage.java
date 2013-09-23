package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class ActionFailedMessage extends ReceivablePacket{

	public String message;
	public short errorType;
	
	@Override
	public boolean read() {
		errorType = (short) readC();
		message = readS();
		System.out.println(message);
		return false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
