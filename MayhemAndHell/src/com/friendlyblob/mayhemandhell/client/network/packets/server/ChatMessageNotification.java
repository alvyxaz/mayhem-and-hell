package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.gui.chat.ChatMessage.ChatMessageType;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

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
		GameCharacter gameCharacter = GameWorld.getInstance().getCharacter(objectId);

		gameCharacter.chatBubbleNotifications.addRegularNotification(msg);
		
		// TODO: move to assets and preload
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/chat-notification.mp3"));
		sound.play();


	}
}
