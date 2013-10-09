package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class ChatMessageNotify extends ServerPacket{
	
	int playerId;
	String msg;
	int type;
	
	// message type specific vars
	int recipientPlayerId;
	int guildObjectId;
	int partyObjectId;
	
	public ChatMessageNotify(int playerId, String message, int type) {
		this.playerId = playerId;
		this.msg = message;
		this.type = type;
	}

	@Override
	public void write() {
		writeC(0x08);
		writeD(playerId);
		writeS(msg);
		writeD(type);
		
		switch (type) {
			case 1:
				writeD(recipientPlayerId);
				break;
			case 2:
				writeD(guildObjectId);
				break;
			case 3:
				writeD(partyObjectId);
				break;
		}
	}

}
