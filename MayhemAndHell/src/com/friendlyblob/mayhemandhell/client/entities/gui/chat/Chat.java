package com.friendlyblob.mayhemandhell.client.entities.gui.chat;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.entities.gui.GuiElement;
import com.friendlyblob.mayhemandhell.client.entities.gui.chat.ChatMessage.ChatMessageType;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.ClientChatMessage;

/**
 * Manages chat related stuff.
 * 
 * @author Vytautas
 *
 */
public class Chat extends GuiElement implements InputProcessor {
	private static final int HISTORY_SIZE = 30;
	
	private List<ChatMessage> messages;
	
	private final int padding = (int) Assets.defaultFont.getSpaceWidth();
	private final int showMaxLines = 6;
	private final int lineHeight = (int) Assets.defaultFont.getLineHeight() - 2;
	private final int lineWidth = 150;
	
	private int scrollValue;
	private Rectangle scrollBox;
	
	private Rectangle textBox;
	
	private boolean active;
	
	// Temporary message container - contains temporary message 
	// split up into length no longer than lineWidth
	private ArrayList<StringBuilder> tempMsgs;
	
	public Chat() {
		super(GuiPriority.HIGH);
		
		messages = new ArrayList<ChatMessage>();
		tempMsgs = new ArrayList<StringBuilder>();
		tempMsgs.add(new StringBuilder());
		
		scrollBox = new Rectangle(padding + lineWidth, padding + 2*lineHeight - 6, 8, 12);
		textBox = new Rectangle(0, 0, padding + lineWidth, padding + lineHeight);
		
		Gdx.input.setInputProcessor(this);
		visible = false;
	}
	
	/**
	 * Used for direct insertion of message objects.
	 * @param message
	 */
	public void addMessage (ChatMessage message) {
		if (messages.size() == HISTORY_SIZE) {
			messages.remove(0);
		}
		
		messages.add(message);
	}
	
	/**
	 * Used for insertion of unprocessed messages
	 * so they could be split into separate messages (because of width).
	 */
	public void addMessage(String playerName, String msg, ChatMessageType type) {
		int end;
		
		// Get message heading and attach it to the original message
		// then loop until our message is empty: is longer than our header
		// every loop substring message and add the header
		String messageHead = ChatMessage.getHead(playerName, type);
		msg = messageHead + msg;
		while (msg.length() > messageHead.length()) {
			end = Assets.defaultFont.computeVisibleGlyphs(msg, 0, msg.length(), this.lineWidth);
			this.addMessage(new ChatMessage(playerName, msg.substring(0, end), type));
			msg = messageHead + msg.substring(end, msg.length());
		}
	}

	@Override
	public void onRelease(float x, float y) {
		if (textBox.contains(x, y)) {
			active = !active;
			return;
		}
		
		active = false;
	}

	@Override
	public void onTouching(float x, float y) {
		if (scrollBox.contains(x, y)) {
			if (y <= showMaxLines * lineHeight + padding && y >= padding + 2*lineHeight) {
				scrollBox.y = y - scrollBox.height/2;
				// (c-a)/(b-a) 0..1
				float pos = (y - padding - 2*lineHeight) / ((showMaxLines + 2) * lineHeight);
				float result = Math.max(0, (messages.size() - showMaxLines) * pos);
				scrollValue = Math.round(result);
			}
		}
	}

	@Override
	public void establishSize() {
		box.width = padding + lineWidth + padding*2;
		box.height = padding + lineHeight * showMaxLines + padding*2;
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		Color c = spriteBatch.getColor();

		spriteBatch.setColor(0, 0, 0, .6f);
		spriteBatch.draw(Assets.px, box.x, box.y, box.width, box.height);
		
		ChatMessage chatMessage;
		for (int i = 0; i < Math.min(showMaxLines, messages.size()); i++) {
			chatMessage = messages.get(messages.size() - i - 1 - scrollValue);
			
			Assets.defaultFont.setColor(chatMessage.getColour());
			Assets.defaultFont.draw(spriteBatch, chatMessage.toString(), padding, (i+2)*lineHeight + padding);	
		}
		
		Assets.defaultFont.setColor(Color.WHITE);
		
		if (tempMsgs.size() > 0) {
			Assets.defaultFont.draw(spriteBatch, tempMsgs.get(tempMsgs.size() - 1).toString(), padding, Assets.defaultFont.getCapHeight() + padding);
		}
		
		// Draw caret
		if (active) {
			spriteBatch.setColor(Color.WHITE);
			TextBounds bounds = Assets.defaultFont.getBounds(tempMsgs.get(tempMsgs.size() - 1).toString());
			
			spriteBatch.draw(Assets.px, bounds.width + padding, padding, 1, Assets.defaultFont.getCapHeight());
		}
		
		// Draw scrollbar
		spriteBatch.setColor(Color.GRAY);
		spriteBatch.draw(Assets.px, scrollBox.x, scrollBox.y, scrollBox.width, scrollBox.height);
		
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
	            	if (tempMsgs.size() > 0) {
	            		StringBuilder tempBuilder = new StringBuilder();
	            		for (StringBuilder sb : tempMsgs) {
							tempBuilder.append(sb.toString());
						}
	            		
	        			MyGame.connection.sendPacket(new ClientChatMessage(tempBuilder.toString()));
	        		
	        			for (int i = tempMsgs.size() - 1; i > 0; i--) {
							tempMsgs.remove(i);
						}
						tempMsgs.get(0).setLength(0);
						
	        			active = false;
	            	}
	            	break;
	            case Keys.BACKSPACE:
	            	if (tempMsgs.size() > 0) {
	            		StringBuilder sb = tempMsgs.get(tempMsgs.size() - 1);
	            		
	            		if (sb.length() > 0) {
		            		sb.setLength(sb.length() - 1);
	            		} else {
	            			if (tempMsgs.size() > 1) {
		            			tempMsgs.remove(tempMsgs.size() - 1);
	            			}
	            		}
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
        		StringBuilder sb = tempMsgs.get(tempMsgs.size() - 1);
        		
        		// Check if visible glyphs count are less than our current string
        		if (Assets.defaultFont.computeVisibleGlyphs(sb.toString() + character, 0, sb.length() + 1, this.lineWidth) < sb.length() + 1) {
        			StringBuilder newBuilder = new StringBuilder();
        			tempMsgs.add(newBuilder.append(character));
        		} else {
        			sb.append(character);
        		}
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
	
}
