package com.friendlyblob.mayhemandhell.client.entities.gui.inventory;

import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.entities.Item;

public class Inventory {
	private SlotObject[] slots;
	
	public Inventory(int width, int height) {
		slots = new SlotObject[width*height];
	}
	
	/**
	 * Returns item and array index i
	 * otherwise returns null
	 * @param i - array index
	 * @return
	 */
	public SlotObject getItemAt(int i) {
		return slots[i];
	}
	
	/**
	 * Sets item at given index
	 * @param i
	 * @param item 
	 */
	public void setItemAt(int i, SlotObject item) {
		slots[i] = item;
	}
	
	/**
	 * Adds item to an empty slot
	 * @param item - item to add
	 * @return true on success
	 * false when no slots available
	 */
	public boolean addItem(SlotObject item) {
		for (int i = 0; i < slots.length; i++) {
			if (slots[i] == null) {
				slots[i] = item;
				return true;
			}
		}
		
		return false;
	}
	
	public int size() {
		return slots.length;
	}
}
