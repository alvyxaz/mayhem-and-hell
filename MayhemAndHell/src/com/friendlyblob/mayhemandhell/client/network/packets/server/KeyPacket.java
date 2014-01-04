package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;
import com.friendlyblob.mayhemandhell.client.network.packets.client.LoginPacket;

public class KeyPacket extends ReceivablePacket{

	byte [] key;
	
	@Override
	public boolean read() {
		key = new byte[16];
		for(int i = 0; i < 8; i++){
			key[i] = (byte)readC(); 
		}
		return true;
	}

	@Override
	public void run() {
		
		key[8] = (byte) 0xc8;
		key[9] = (byte) 0x27;
		key[10] = (byte) 0x93;
		key[11] = (byte) 0x01;
		key[12] = (byte) 0xa1;
		key[13] = (byte) 0x6c;
		key[14] = (byte) 0x31;
		key[15] = (byte) 0x97;
		
		connection.getCrypt().setKey(key);
		connection.getCrypt().enable();
		
//		connection.sendPacket(new LoginPacket());
	}

}
