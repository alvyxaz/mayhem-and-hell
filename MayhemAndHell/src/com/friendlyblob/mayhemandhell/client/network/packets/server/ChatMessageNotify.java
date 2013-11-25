package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.badlogic.gdx.graphics.Color;
import com.friendlyblob.mayhemandhell.client.entities.gui.chat.Chat;
import com.friendlyblob.mayhemandhell.client.entities.gui.chat.ChatMessage;
import com.friendlyblob.mayhemandhell.client.entities.gui.chat.ChatMessage.ChatMessageType;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;
import com.friendlyblob.mayhemandhell.client.network.packets.client.LoginPacket;

public class ChatMessageNotify extends ReceivablePacket {
	private String playerName;
	private String msg;
	private ChatMessageType type;
	
	@Override
	public boolean read() {
		playerName = readS();
		msg = readS();
		type = ChatMessageType.fromValue(readD());
		
		return true;
	}

	@Override
	public void run() {
		Chat chat = connection.game.screenGame.guiManager.chat;
		
		chat.addMessage(playerName, msg, type);
	}
}
