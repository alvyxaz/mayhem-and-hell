package com.friendlyblob.mayhemandhell.server.model.items;

import com.friendlyblob.mayhemandhell.server.model.GameObject;

public class Item {
	
	private int itemId;
	
	public static enum ItemType {
		CONSUMABLE,
		EQUIPMENT,
		MATERIAL,
		OTHER
	}
	
	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}

	
}
