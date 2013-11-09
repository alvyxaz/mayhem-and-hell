package com.friendlyblob.mayhemandhell.client.entities;

public class Inventory {
	private Item[] items;
	
	public Inventory() {
		items = new Item[3*4];
	}
	
	/**
	 * Returns item and array index i
	 * otherwise returns null
	 * @param i - array index
	 * @return
	 */
	public Item getItemAt(int i) {
		return items[i];
	}
	
	/**
	 * Sets item at given index
	 * @param i
	 * @param item 
	 */
	public void setItemAt(int i, Item item) {
		items[i] = item;
	}
	
	/**
	 * Adds item to an empty slot
	 * @param item - item to add
	 * @return true on success
	 * false when no slots available
	 */
	public boolean addItem(Item item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				items[i] = item;
				return true;
			}
		}
		
		return false;
	}
}
