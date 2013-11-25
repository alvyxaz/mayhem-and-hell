package com.friendlyblob.mayhemandhell.client.entities.gui.chat;

import com.badlogic.gdx.graphics.Color;

public class ChatMessage {
	public static enum ChatMessageType {
		TALK(0),
		WHISPER(1),
		GUILD(2),
		PARTY(3),
		BROADCAST(4);
		
		public int value;

		ChatMessageType(int value) {
			this.value = value;
		}
		
		public static ChatMessageType fromValue(int value) {
			for (ChatMessageType type : ChatMessageType.values()) {
				if (value == type.value) {
					return type;
				}
			}
			return null;
		}
	}
	
	private static final Color [] colours = {
		Color.LIGHT_GRAY,
		Color.BLUE,
		Color.RED,
		Color.ORANGE,
		Color.YELLOW,
	};
	
	public String playerName;
	public String message;
	public ChatMessageType type;
	public long timestamp;
	
	public ChatMessage(String playerName, String message, ChatMessageType type) {
		this.playerName = playerName;
		this.message = message;
		this.type = type;
		this.timestamp = 0;
	}
	
	public Color getColour() {
		return colours[type.value];
	}
	
	public static String getHead(String playerName, ChatMessageType type) {
		StringBuilder stringBuilder = new StringBuilder();
		
		switch (type) {
			case TALK:
				stringBuilder.append(playerName);
				stringBuilder.append(": ");
				break;
			case WHISPER:
				stringBuilder.append(playerName);
				stringBuilder.append(": ");
				break;
			case GUILD:
				stringBuilder.append("[GUILD] ");
				stringBuilder.append("[" + playerName + "]");
				stringBuilder.append(": ");
				break;
			case PARTY:
				stringBuilder.append("[PARTY] ");
				stringBuilder.append("[" + playerName + "]");
				stringBuilder.append(": ");
				break;
			case BROADCAST:
				stringBuilder.append(playerName);
				stringBuilder.append(": ");
				break;
			default:
		}
		
		return stringBuilder.toString();
	}
	
	@Override
	public String toString() {
		return message;
	}
}
