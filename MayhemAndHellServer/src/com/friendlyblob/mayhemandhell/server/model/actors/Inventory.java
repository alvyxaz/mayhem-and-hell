package com.friendlyblob.mayhemandhell.server.model.actors;

import com.friendlyblob.mayhemandhell.server.model.instances.ItemInstance;

public class Inventory {
	private ItemInstance[] items;
	
	public Inventory() {
		items = new ItemInstance[3*4];
	}
	
	/**
	 * Returns item and array index i
	 * otherwise returns null
	 * @param i - array index
	 * @return
	 */
	public ItemInstance getItemAt(int i) {
		return items[i];
	}
	
	/**
	 * Sets item at given index
	 * @param i
	 * @param item 
	 */
	public void setItemAt(int i, ItemInstance item) {
		items[i] = item;
	}
	
	/**
	 * Adds item to an empty slot
	 * @param item - item to add
	 * @return true on success
	 * false when no slots available
	 */
	public boolean addItem(ItemInstance item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				items[i] = item;
				return true;
			}
		}
		
		return false;
	}
}