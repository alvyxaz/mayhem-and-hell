package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RequestAction;

public class ActionsBar extends GuiElement{

	private String[] actions;
	
	private BitmapFont font;
	
	private int buttonWidth = 50;
	private int buttonHeight = 28;
	
	private Rectangle expandButton;
	
	private boolean expanded;
	
	private int buttonTopOffset;
	
	public ActionsBar() {
		super(GuiPriority.HIGH);
		
		font = Assets.defaultFont;
		
		// Debugging purposes. remove
		this.actions = new String[3];
		actions[0] = "Attack";
		actions[1] = "Trade";
		actions[2] = "Dance";
		
		buttonTopOffset = (buttonHeight - (int)font.getLineHeight())/2;
		
		expandButton = new Rectangle(0, 0, 20, buttonHeight);
		
		visible = false;
	}

	@Override
	public void onRelease(float x, float y) {
		if (expandButton.contains(box.x+x, box.y+y)) {
			toggleExpand();
		} else {
			onActionClick((int) y / buttonHeight);
		}
	}
	
	public void onActionClick(int i) {
		MyGame.connection.sendPacket(new RequestAction(i));
	}

	@Override
	public void onTouching(float x, float y) {
		
	}
	
	public void toggleExpand() {
		expanded = !expanded;
		if (expanded) {
			box.height = buttonHeight * actions.length;
		} else {
			box.height = buttonHeight;
		}
	}

	@Override
	public void establishSize() {
		box.width = buttonWidth + expandButton.getWidth();
		box.height = buttonHeight;
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		if (actions.length > 1) {
			spriteBatch.setColor(0.4f, 0.3f, 0.2f, 1);
			spriteBatch.draw(Assets.px, expandButton.x, expandButton.y, expandButton.width, expandButton.height);
		}
		
		if (expanded) {
			for (int i = 0; i < actions.length; i++) {
				spriteBatch.setColor(0.6f, 0.4f, 0.3f, 1);
				spriteBatch.draw(Assets.px, box.x, box.y + (buttonHeight+1)*i, buttonWidth, buttonHeight);
				spriteBatch.setColor(Color.WHITE);
				font.drawWrapped(
						spriteBatch, actions[i], box.x, box.y+ buttonHeight*(i+1) - buttonTopOffset, 
						buttonWidth, HAlignment.CENTER);
			}
		} else {
			spriteBatch.setColor(0.6f, 0.4f, 0.3f, 1);
			spriteBatch.draw(Assets.px, box.x, box.y, buttonWidth, buttonHeight);
			spriteBatch.setColor(Color.WHITE);
			font.drawWrapped(
					spriteBatch, actions[0], box.x, box.y+ buttonHeight - buttonTopOffset, 
					buttonWidth, HAlignment.CENTER);
		}
	}

	public void show(String[] actions) {
		visible = true;
		this.actions = actions; // TODO might be a subject to concurrent exception
	}
	
	@Override
	public void setX(float x) {
		super.setX(x);
		expandButton.x = x + box.width - expandButton.width;
		expanded = false;
	}
	
	@Override
	public void setY(float y) {
		super.setY(y);
		expandButton.y = y;
	}
	
	public void hide() {
		visible = false;
	}
	
	@Override
	public void update(float deltaTime) {
		
	}
	
}
