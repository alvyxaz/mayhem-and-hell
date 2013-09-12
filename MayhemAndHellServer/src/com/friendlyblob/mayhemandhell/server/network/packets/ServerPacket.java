package com.friendlyblob.mayhemandhell.server.network.packets;

import java.nio.ByteBuffer;

import org.mmocore.network.SendablePacket;

import com.friendlyblob.mayhemandhell.server.network.GameClient;

public abstract class ServerPacket extends SendablePacket<GameClient>{

	public void runImpl()
	{
		
	}
	
	public ByteBuffer getBuf(){
		return _buf;
	}
}
