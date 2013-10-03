package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class Inventory extends GuiWindow {

	private TextureRegion slotTexture;
	
	private int rows = 3;
	private int columns = 4;
	private int slotSize; // Width and height are the same
	
	private Rectangle [] slots;
	
	public Inventory() {
		super();
		this.setTitle("Inventory");
		slotTexture = Assets.getTextureRegion("gui/ingame/inventory_slot");
		slotSize = slotTexture.getRegionWidth();
		
		setWindowSize((slotSize+1)*columns, slotSize*rows);
		
		slots = new Rectangle[rows*columns];
		for(int i = 0; i < rows*columns; i++) {
			slots[i] = new Rectangle( 
					(i % columns)*(slotSize+1) + contentPaddingLeft ,
					(i / columns)*(slotSize) + contentPaddingTop-1, 
					slotSize, 
					slotSize);
		}
		visible = false;
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
		
		for (int i = 0; i < slots.length; i++) {
			spriteBatch.draw(slotTexture, box.x + slots[i].x, box.y  + slots[i].y);
		}
	}
	
	@Override
	public void establishSize() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onContentRelease(float x, float y) {
		// Checking whether a slot was touched
		for (int i = 0; i < slots.length; i++) {
			if(slots[i].contains(x, y)) {
				onSlotRelease(i);
				return;
			}
		}
	}
	
	public void onSlotRelease(int slotIndex) {
		System.out.println("Slot touched: " + slotIndex);
	}

	@Override
	public void onContentTouching(float x, float y) {
		
	}
	
}
