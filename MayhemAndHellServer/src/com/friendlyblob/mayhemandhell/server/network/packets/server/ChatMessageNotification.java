package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class ChatMessageNotification extends ServerPacket{

	private int objectId;
	private String msg;
	private int type;
	
	public ChatMessageNotification(int objectId, String message, int type) {
		this.objectId = objectId;
		this.msg = message;
		this.type = type;
	}

	@Override
	public void write() {
		writeC(0xA0);
		writeD(objectId);
		writeS(msg);
		writeD(type);
	}

}
