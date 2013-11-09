package com.friendlyblob.mayhemandhell.server.model.items;

import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;

public class Item {
	
	private int itemId;
	private String name;
	private ItemType type;
	private StatsSet set;
	
	public static enum ItemType {
		USABLE,
		EQUIPMENT,
		MATERIAL,
		OTHER,
	}
	
	public static enum ExpirationType {
		NONE,
		GAMEPLAY,
		TIMESPAN,
		EXACT,
	}
	
	public Item(int itemId, String name, StatsSet set) {
		this.itemId = itemId;
		this.name = name;
		this.set = set;
	}
	
	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public StatsSet getStatsSet() {
		return set;
	}

	public void setStatsSet(StatsSet set) {
		this.set = set;
	}

	/**
	 * Whether or not item can be stacked 
	 * TODO implement (together with max stack size)
	 * @return
	 */
	public boolean isStackable() {
		return false;
	}
}
