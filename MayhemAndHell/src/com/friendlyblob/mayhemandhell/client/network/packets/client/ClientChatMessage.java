package com.friendlyblob.mayhemandhell.client.network.packets.client;

import com.friendlyblob.mayhemandhell.client.entities.gui.Chat.ChatMessageType;
import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;

public class ClientChatMessage extends SendablePacket {

	String msg;
	ChatMessageType type;
	
	// message type specific variables
	int recipientPlayerId;
	int partyObjectId;
	int guildObjectId;
	
	public ClientChatMessage(String message, ChatMessageType type) {
		this.msg = message;
		this.type = type;
	}
	
	public ClientChatMessage(String message, ChatMessageType type, int recipientPlayerId) {
		this.msg = message;
		this.type = type;
		this.recipientPlayerId = recipientPlayerId;
	}
	
	@Override
	public void write() {
		System.out.println("sending message");
		writeC(0x04);
		writeS(msg);
		writeD(type.value);
		
		switch (type) {
			case WHISPER:
				writeD(recipientPlayerId);
				break;
			case GUILD:
				writeD(guildObjectId);
				break;
			case PARTY:
				writeD(partyObjectId);
				break;
		}
	}
}
