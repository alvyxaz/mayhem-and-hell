package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RequestDialogAction;

public class Dialog extends GuiWindow {

	private static final int LINK_HEIGHT = 18;
	private static final int WIDTH = 150;
	private static final int HEIGHT = 150;
	private static final int GOODBYE_HEIGHT = 20;
	
	private String text = "Once upon a time, there was a little boy we used to call \"Superman\". One day, he grew up to be a fine warrior. NOT!";
	
	private Rectangle goodbye;
	private Rectangle accept;
	
	private Rectangle[] links;
	private String[] linkTexts;
	private TextureRegion[] linkIcons;
	
	private int pageId;
	
	private boolean showAccept = false;
	
	public Dialog() {
		super();
		visible = false;
		this.setTitle("Dialog");
		
		goodbye = new Rectangle(0, 0, 70, 20);
		accept = new Rectangle(0, 0, 70, 20);
		
		linkTexts = new String[3];
		linkIcons = new TextureRegion[3];
		
		setWindowSize(WIDTH, HEIGHT + GOODBYE_HEIGHT);
	}
	
	@Override
	public void onContentRelease(float x, float y) {
		for (int i = 0; i < links.length; i++) {
			if (links[i].contains(x, y)) {
				MyGame.connection.sendPacket(new RequestDialogAction(i, pageId));
				return;
			}
		}
		
		if (showAccept && accept.contains(x, y)) {
			MyGame.connection.sendPacket(new RequestDialogAction(-2, pageId));
			hide();
			return;
		} else if (goodbye.contains(x, y)) {
			MyGame.connection.sendPacket(new RequestDialogAction(-1, pageId));
			hide();
			return;
		}
	}

	@Override
	public void onContentTouching(float x, float y) {
		
	}
	
	@Override
	public void setX(float x) {
		super.setX(x);
		goodbye.x = (int)(box.width-goodbye.width);
	}
	
	@Override
	public void setY(float y) {
		super.setY(y);
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
		
		// Goodbye & accept
		spriteBatch.setColor(0.6f, 0.2f, 0.2f, 1f);
		spriteBatch.draw(Assets.px, box.x + goodbye.x,  box.y + goodbye.y, 
				goodbye.width, goodbye.height);
		Assets.defaultFont.drawWrapped(spriteBatch, "Goodbye", box.x + goodbye.x, 
				box.y + goodbye.y + goodbye.height-5, goodbye.width, HAlignment.CENTER);
		
		if (showAccept) {
			spriteBatch.setColor(0.4f, 0.6f, 0.15f, 1f);
			spriteBatch.draw(Assets.px, box.x + accept.x,  box.y + accept.y, 
					accept.width, accept.height);
			Assets.defaultFont.drawWrapped(spriteBatch, "Accept", box.x + accept.x, 
					box.y + accept.y + accept.height-5, accept.width, HAlignment.CENTER);
		}
		spriteBatch.setColor(Color.WHITE);
		
	}
	
	public void updateDialog(String name, String text, String[] linkTexts, int[] linkTypes, int pageId) {
		this.setTitle(name + " says:");
		this.text = text;
		this.linkTexts = linkTexts;
		this.pageId = pageId;
		
		links = new Rectangle[linkTexts.length];
		for (int i = 0; i < links.length; i++) {
			links[i] = new Rectangle(contentPaddingLeft, 4 + GOODBYE_HEIGHT + LINK_HEIGHT*(links.length-1-i), 
					WIDTH, 16);
		}
		
		TextureRegion[] tempLinkIcons = new TextureRegion[linkTypes.length];
		
		for (int i = 0; i < linkTypes.length; i++) {
			switch(linkTypes[i]) {
			case 2: 
				tempLinkIcons[i] = Assets.getTextureRegion("gui/ingame/icon_quest");
				break;
			case 3: 
				tempLinkIcons[i] = Assets.getTextureRegion("gui/ingame/icon_quest");
				break;
			case 4:
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
	
	public void setShowAccept(boolean accept) {
		showAccept = accept;
	}

	@Override
	public void establishSize() {
	}
	
}
