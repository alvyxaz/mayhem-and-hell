package com.friendlyblob.mayhemandhell.client.entities.gui.inventory;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.entities.Inventory;
import com.friendlyblob.mayhemandhell.client.entities.Item;
import com.friendlyblob.mayhemandhell.client.entities.gui.GuiWindow;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class InventoryView extends GuiWindow {

	private TextureRegion slotTexture;
	
	private int rows = 3;
	private int columns = 4;
	private int slotSize; // Width and height are the same
	
	private Rectangle[] slots;
	private Inventory inventory; 
	
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
	
	public InventoryView() {
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
	
	public void show() {
		visible = true;
	}
	
	public void hide() {
		visible = false;
	}
	
	public void setDataSource(Inventory inventory) {
		this.inventory = inventory;
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
		
		for (int i = 0; i < slots.length; i++) {
			spriteBatch.draw(slotTexture, box.x + slots[i].x, box.y  + slots[i].y);
		}
		
		Item item;
		for (int i = 0; i < slots.length; i++) {
			if (inventory.getItemAt(i)!= null) {
				item = inventory.getItemAt(i);

				// Check if dragging, then draw at object's current position
				// else draw at slot's position
				if (dragging && draggingFrom == i) {
					spriteBatch.draw(item.getInventoryItem().getTexture(), box.x + item.getInventoryItem().getX(), box.y + item.getInventoryItem().getY());
				} else {
					spriteBatch.draw(item.getInventoryItem().getTexture(), box.x + slots[i].x, box.y + slots[i].y);
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
			inventory.getItemAt(this.draggingFrom).getInventoryItem().setPosition(x, y);
		} else {
			for (int i = 0; i < slots.length; i++) {
				// If the slot which was touched contains an object
				if (slots[i].contains(x, y) && inventory.getItemAt(i) != null) {
					draggingDx += Input.touch[0].dx;
					draggingDy += Input.touch[0].dy;
					
					if (Math.abs(draggingDx) > draggingTreshold || Math.abs(draggingDy) > draggingTreshold ) {
						startDragging(x, y, i);
					}
				}
			}
		}
	}
	
	public void startDragging(float x, float y, int draggingFrom) {
		draggingDx = 0;
		draggingDy = 0;
		dragging = true;
		draggingAtX = (int)x;
		draggingAtY = (int)y;
		this.draggingFrom = draggingFrom;
		
		inventory.getItemAt(this.draggingFrom).getInventoryItem().setPosition(x, y);
	}
	
	public void stopDragging(float x, float y) {
		dragging = false;
		inventory.getItemAt(this.draggingFrom).getInventoryItem().setPosition(0, 0);
		
		for (int i = 0; i < slots.length; i++) {
			if (inventory.getItemAt(i) == null) {
				if (slots[i].contains(x, y)) {
					inventory.setItemAt(i, inventory.getItemAt(this.draggingFrom));
					inventory.setItemAt(this.draggingFrom, null);
				}	
			}
		}
	}
}
