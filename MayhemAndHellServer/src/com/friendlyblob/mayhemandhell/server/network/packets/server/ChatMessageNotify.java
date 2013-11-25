package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class ChatMessageNotify extends ServerPacket{

	private String playerName;
	private String msg;
	private int type;
	
	public ChatMessageNotify(String playerName, String message, int type) {
		this.playerName = playerName;
		this.msg = message;
		this.type = type;
	}

	@Override
	public void write() {
		writeC(0xA0);
		writeS(playerName);
		writeS(msg);
		writeD(type);
	}

}
