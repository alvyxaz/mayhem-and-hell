package com.friendlyblob.mayhemandhell.server.model.items;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;

public class Item {
	
	public int itemId;
	public String name;
	public String type;
	public StatsSet set;
	
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
