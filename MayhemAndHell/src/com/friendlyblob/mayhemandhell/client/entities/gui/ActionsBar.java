package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class ActionsBar extends GuiElement{

	private String[] actions;
	
	private int buttonWidth = 50;
	private int buttonHeight = 28;
	
	private boolean expanded;
	
	public ActionsBar() {
		super(GuiPriority.HIGH);
		
		this.actions = new String[3];
		actions[0] = "Attack";
		actions[1] = "Trade";
		actions[2] = "Dance";
		
		visible = false;
	}

	@Override
	public void onRelease(float x, float y) {
		
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
		box.width = buttonWidth;
		box.height = buttonHeight;
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		for (int i = 0; i < actions.length; i++) {
			Assets.defaultFont.draw(spriteBatch, actions[i], box.x, box.y+ buttonHeight*(i+1));
		}
//		if (expanded) {
//			for (int i = 0; i < actions.length; i++) {
//				Assets.defaultFont.draw(spriteBatch, actions[i], box.x, box.y+ buttonHeight*(i+1));
//			}
//		} else {
//			Assets.defaultFont.draw(spriteBatch, actions[0], box.x, box.y+ buttonHeight);
//		}
	}

	public void show(String[] actions) {
		visible = true;
		this.actions = actions; // TODO might be a subject to concurrent exception
	}
	
	public void hide() {
		visible = false;
	}
	
	@Override
	public void update(float deltaTime) {
		
	}
	
}
