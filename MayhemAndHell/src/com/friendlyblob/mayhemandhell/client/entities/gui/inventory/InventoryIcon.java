package com.friendlyblob.mayhemandhell.client.entities.gui.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.entities.gui.GuiElement;
import com.friendlyblob.mayhemandhell.client.entities.gui.GuiElement.GuiPriority;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class InventoryIcon extends GuiElement {

	private int buttonWidth = 24;
	private int buttonHeight = 24;
	
	private BitmapFont font;
	
	private int buttonTopOffset;
	
	public InventoryIcon() {
		super(GuiPriority.HIGH);
		
		font = Assets.defaultFont;
		
		buttonTopOffset = (buttonHeight - (int)font.getLineHeight())/2;
	}

	@Override
	public void onRelease(float x, float y) {
		if (box.contains(box.x+x, box.y+y)) {
			if (this.manager.inventory.visible) {
				this.manager.inventory.hide();
			} else {
				this.manager.inventory.show();
			}
		}
	}

	@Override
	public void onTouching(float x, float y) {

	}

	@Override
	public void establishSize() {
		box.width = buttonWidth;
		box.height = buttonHeight;
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		// Draw background
		spriteBatch.setColor(0.4f, 0.3f, 0.2f, 1);
		spriteBatch.draw(Assets.px, box.x, box.y, box.width, box.height);
		spriteBatch.setColor(Color.WHITE);
		
		// Draw the "I" char
		spriteBatch.setColor(Color.WHITE);
		font.drawWrapped(
				spriteBatch, "I", box.x, box.y + buttonHeight - buttonTopOffset, 
				buttonWidth, HAlignment.CENTER);
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub

	}

}
