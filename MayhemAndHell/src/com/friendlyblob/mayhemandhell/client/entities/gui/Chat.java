package com.friendlyblob.mayhemandhell.client.entities.gui;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.entities.GameObject.GameObjectType;
import com.friendlyblob.mayhemandhell.client.entities.gui.GuiElement.GuiPriority;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.ClientChatMessage;

/**
 * Manages chat related stuff.
 * 
 * @author Vytautas
 *
 */
public class Chat extends GuiElement implements InputProcessor{
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

	private static final int HISTORY_SIZE = 30;
	
	// TODO: use circular buffer?
	private List<Message> messages;
	
	// Gui related vars
	private final int showMaxLines = 6;
	private final int lineHeight = 10;
	
	private boolean active;
	private StringBuilder tempMsg;
	
	public Chat() {
		super(GuiPriority.HIGH);
		
		messages = new ArrayList<Message>();
		tempMsg = new StringBuilder();
		
		Gdx.input.setInputProcessor(this);
	}
	
	public void addMessage (Message message) {
		if (messages.size() == HISTORY_SIZE) {
			messages.remove(0);
		}
		
		messages.add(message);
	}

	@Override
	public void onRelease(float x, float y) {
		active = !active;
	}

	@Override
	public void onTouching(float x, float y) {

	}

	@Override
	public void establishSize() {
		box.width = 150;
		box.height = lineHeight * (showMaxLines + 1);
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		Color c = spriteBatch.getColor();
		
		spriteBatch.setColor(0, 0, 0, .6f);
		spriteBatch.draw(Assets.px, box.x, box.y, box.width, box.height);
		Assets.defaultFont.setColor(Color.CYAN);
		
		for (int i = 0; i < Math.min(showMaxLines, messages.size()); i++) {
			// Draw wrapped
			Assets.defaultFont.draw(spriteBatch, messages.get(messages.size() - i - 1).message, 10, (i+2)*lineHeight);	
		}
		
		if (tempMsg != null) {
			Assets.defaultFont.draw(spriteBatch, tempMsg, 10, lineHeight);
		}
		
		// Restore regular color
		Assets.defaultFont.setColor(Color.WHITE);
		spriteBatch.setColor(c);
	}

	@Override
	public void update(float deltaTime) {
	}
	
	// ======================================
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        //dLog("This is the new processor");
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        //dLog("This is the new processor");
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        //dLog("This is the new processor");
        return false;
    }

    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        //dLog("This is the new processor");
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        //dLog("This is the new processor");
        return false;
    }

    public boolean keyUp(int keyCode) {
    	if (active) {
            switch (keyCode) {
	            case Keys.ENTER:
	            	if (tempMsg.length() > 0) {
	        			MyGame.connection.sendPacket(new ClientChatMessage(tempMsg.toString(), ChatMessageType.TALK));
	        		
	        			active = false;
	        			tempMsg.setLength(0);
	            	}
	            	break;
	            case Keys.BACKSPACE:
	            	if (tempMsg.length() > 0) {
	            		tempMsg.setLength(tempMsg.length() - 1);
	            	}
	            	break;
            }
    	}

    	
        return false;
    }

    public boolean keyTyped(char character) {

    	if (active) {
    		int charCode = (int) character;
    		// append only printable
        	if (charCode > 31 && charCode < 127) {
        		tempMsg.append(character);
        	}
     	}

		return true;

    }

    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        //dLog("This is the new processor");
        return false;
    }
    // ===============================================
	
	public class Message {
		public int playerId;
		public String message;
		public ChatMessageType type;
		public long timestamp;
		
		public Message(int playerId, String message, ChatMessageType type) {
			this.playerId = playerId;
			this.message = message;
			this.type = type;
			this.timestamp = 0;
		}
	}
	
}
