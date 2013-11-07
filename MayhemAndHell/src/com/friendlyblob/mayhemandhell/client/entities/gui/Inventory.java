package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class Inventory extends GuiWindow {

	private TextureRegion slotTexture;
	
	private int rows = 3;
	private int columns = 4;
	private int slotSize; // Width and height are the same
	
	private Rectangle[] slots;
	private SlotObject[] slotObjects;
	
	// Dragging related vars
	private int draggingFrom;
	// Variable to indicate when object is being dragged
	private boolean dragging;
	// Dragging threshold measured in pixels
	private int draggingTreshold = 2;
	// Dragging distance deltas
	private int draggingDx, draggingDy;
	// Current dragging position
	private int draggingAtX, draggingAtY;
	
	public Inventory() {
		super();
		this.setTitle("Inventory");
		slotTexture = Assets.getTextureRegion("gui/ingame/inventory_slot");
		slotSize = slotTexture.getRegionWidth();
		
		setWindowSize((slotSize+1)*columns, slotSize*rows);
		
		slots = new Rectangle[rows*columns];
		slotObjects = new SlotObject[rows*columns];
		for(int i = 0; i < rows*columns; i++) {
			slots[i] = new Rectangle( 
					(i % columns)*(slotSize+1) + contentPaddingLeft ,
					(i / columns)*(slotSize) + contentPaddingTop-1, 
					slotSize, 
					slotSize);
		}
		
		InventoryItem item = new InventoryItem(slotSize)
								.setName("Some sword")
								.setDescription("Really cool sword");
		slotObjects[0] = item;
		
		item = new InventoryItem(slotSize)
		.setName("Another sword")
		.setDescription("Not so cool, but still pretty cool sword");
		slotObjects[5] = item;
		visible = false;
	}
	
	public void show() {
		visible = true;
	}
	
	public void hide() {
		visible = false;
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
		
		for (int i = 0; i < slots.length; i++) {
			spriteBatch.draw(slotTexture, box.x + slots[i].x, box.y  + slots[i].y);
		}
		
		SlotObject item;
		for (int i = 0; i < slots.length; i++) {
			if (slotObjects[i] != null) {
				item = slotObjects[i];

				// Check if dragging, then draw at object's current position
				// else draw at slot's position
				if (dragging && draggingFrom == i) {
					spriteBatch.draw(item.texture, box.x + item.getX(), box.y + item.getY());
				} else {
					spriteBatch.draw(item.texture, box.x + slots[i].x, box.y + slots[i].y);
				}
			}
		}
	}
	
	@Override
	public void establishSize() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onContentRelease(float x, float y) {
		if (dragging) {
			stopDragging(x, y);
		} else {
			for (int i = 0; i < slots.length; i++) {
				if (slots[i].contains(x, y)) {
					
				}
			}
		}
	}
	
	@Override
	public void onContentTouching(float x, float y) {
		if (dragging) {
			slotObjects[draggingFrom].setPosition(x, y);
		} else {
			for (int i = 0; i < slots.length; i++) {
				// If the slot which was touched contains an object
				if (slots[i].contains(x, y) && slotObjects[i] != null) {
					draggingDx += Input.touch[0].dx;
					draggingDy += Input.touch[0].dy;
					
					if (Math.abs(draggingDx) > draggingTreshold || Math.abs(draggingDy) > draggingTreshold ) {
						startDragging(x, y, i);
					}
				}
			}
		}
	}
	
	// Dragging related methods
	public void startDragging(float x, float y, int draggingFrom) {
		draggingDx = 0;
		draggingDy = 0;
		dragging = true;
		draggingAtX = (int)x;
		draggingAtY = (int)y;
		this.draggingFrom = draggingFrom;
		
		slotObjects[this.draggingFrom].setPosition(x, y);
	}
	
	public void stopDragging(float x, float y) {
		dragging = false;
		slotObjects[draggingFrom].setPosition(0, 0);
		
		for (int i = 0; i < slots.length; i++) {
			if (slotObjects[i] == null) {
				if (slots[i].contains(x, y)) {
					slotObjects[i] = slotObjects[draggingFrom];
					slotObjects[draggingFrom] = null;
				}	
			}
		}
	}
}
