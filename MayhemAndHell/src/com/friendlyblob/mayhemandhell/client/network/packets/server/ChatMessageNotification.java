package com.friendlyblob.mayhemandhell.client.network.packets.server;

import java.util.Map;

import com.friendlyblob.mayhemandhell.client.entities.EnvironmentObject;
import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.GameObject;
import com.friendlyblob.mayhemandhell.client.entities.gui.chat.Chat;
import com.friendlyblob.mayhemandhell.client.entities.gui.chat.ChatMessage.ChatMessageType;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;
import com.friendlyblob.mayhemandhell.client.screens.GameScreen;

public class ChatMessageNotification extends ReceivablePacket {
	
	private int objectId;
	private String msg;
	private ChatMessageType type;
	
	@Override
	public boolean read() {
		objectId = readD();
		msg = readS();
		type = ChatMessageType.fromValue(readD());
		
		return true;
	}

	@Override
	public void run() {
//		Chat chat = connection.game.screenGame.guiManager.chat;
//		
//		chat.addMessage(playerName, msg, type);
		GameCharacter gameCharacter = GameWorld.getInstance().getCharacter(objectId);

		gameCharacter.chatBubbleNotifications.addRegularNotification(msg);


	}
}
