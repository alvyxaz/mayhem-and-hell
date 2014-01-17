package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.gui.chat.ChatMessage.ChatMessageType;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
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
		try {
			GameCharacter gameCharacter = GameWorld.getInstance().getCharacter(objectId);
			GameWorld.instance.chatBubbles.addRegularNotification(msg, gameCharacter.hitBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
