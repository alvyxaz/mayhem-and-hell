package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RequestDialogAction;

public class Dialog extends GuiWindow {

	private static final int WIDTH = 150;
	private static final int HEIGHT = 150;

	private String text = "Once upon a time, there was a little boy we used to call \"Superman\". One day, he grew up to be a fine warrior. NOT!";
	
	private Rectangle goodbye;
	
	private Rectangle[] links;
	private String[] linkTexts;
	private TextureRegion[] linkIcons;
	
	public Dialog() {
		super();
		visible = false;
		this.setTitle("Dialog");
		
		goodbye = new Rectangle(0, 0, 70, 20);
		
		links = new Rectangle[3];
		for (int i = 0; i < links.length; i++) {
			links[i] = new Rectangle(contentPaddingLeft, 4 + 18*(links.length-1-i), WIDTH, 16);
		}
		
		linkTexts = new String[3];
		linkTexts[0] = "I'd like to hear more";
		linkTexts[1] = "Where do I sign up";
		linkTexts[2] = "Show me your goods";

		linkIcons = new TextureRegion[3];
		linkIcons[0] = Assets.getTextureRegion("gui/ingame/icon_store");
		linkIcons[1] = Assets.getTextureRegion("gui/ingame/icon_quest");
		linkIcons[2] = Assets.getTextureRegion("gui/ingame/icon_chat");
		
		setWindowSize(WIDTH, HEIGHT);
	}
	
	@Override
	public void onContentRelease(float x, float y) {
		for (int i = 0; i < links.length; i++) {
			if (links[i].contains(x, y)) {
				MyGame.connection.sendPacket(new RequestDialogAction(i));
				return;
			}
		}
	}

	@Override
	public void onContentTouching(float x, float y) {
		
	}
	
	@Override
	public void setX(float x) {
		super.setX(x);
		goodbye.x = x + box.width - goodbye.width -2;
	}
	
	@Override
	public void setY(float y) {
		super.setY(y);
		goodbye.y = y + 2;
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
		
		Assets.defaultFont.drawWrapped(spriteBatch, 
				text, 
				box.x + contentPaddingLeft, 
				box.y + box.height - contentPaddingTop - titleBox.height, 
				WIDTH);
		
		for (int i = 0; i < links.length; i++) {
			// Button bg
			spriteBatch.setColor(0.6f, 0.4f, 0.2f, 1f);
			spriteBatch.draw(Assets.px, 
					box.x + links[i].x, 
					box.y + links[i].y, 
					links[i].width, 
					links[i].height);
			spriteBatch.setColor(Color.WHITE);
			
			Assets.defaultFont.draw(spriteBatch, linkTexts[i], box.x + links[i].x + 15, box.y + links[i].y + 15);
			
			spriteBatch.draw(linkIcons[i], box.x + links[i].x + 1, 
					box.y + links[i].y + 3);
		}
	}
	
	public void updateDialog(String name, String text, String[] linkTexts, int[] linkTypes) {
		this.setTitle(name + " says:");
		this.text = text;
		this.linkTexts = linkTexts;
		
		TextureRegion[] tempLinkIcons = new TextureRegion[linkTypes.length];
		
		for (int i = 0; i < linkTypes.length; i++) {
			switch(linkTypes[i]) {
			case 2: 
				tempLinkIcons[i] = Assets.getTextureRegion("gui/ingame/icon_quest");
				break;
			case 3:
				tempLinkIcons[i] = Assets.getTextureRegion("gui/ingame/icon_store");
				break;
			default:
				tempLinkIcons[i] = Assets.getTextureRegion("gui/ingame/icon_chat");
				break;
			}
		}
		linkIcons = tempLinkIcons;
		show();
	}

	@Override
	public void establishSize() {
	}
	
}
