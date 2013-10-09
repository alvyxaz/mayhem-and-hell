package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.badlogic.gdx.graphics.Color;
import com.friendlyblob.mayhemandhell.client.entities.gui.Chat;
import com.friendlyblob.mayhemandhell.client.entities.gui.Chat.ChatMessageType;
import com.friendlyblob.mayhemandhell.client.entities.gui.Chat.Message;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;
import com.friendlyblob.mayhemandhell.client.network.packets.client.LoginPacket;

public class ChatMessageNotify extends ReceivablePacket{
	
	int playerId;
	String msg;
	ChatMessageType type;
	
	@Override
	public boolean read() {
		playerId = readD();
		msg = readS();
		type = ChatMessageType.fromValue(readD());
		
		return true;
	}

	@Override
	public void run() {
		Chat chat = connection.game.screenGame.guiManager.chat;
		System.out.println("received message: " + msg);
		
		chat.addMessage(chat.new Message(playerId, msg, type));
	}

}
