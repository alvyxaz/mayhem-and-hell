package com.friendlyblob.mayhemandhell.server.model.items;

import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;

public class UsableItem extends Item{

	private boolean consumable;
	private int uses;
	
	public UsableItem(int itemId, String name, StatsSet set) {
		super(itemId, name, set);
		// TODO Auto-generated constructor stub
	}

	public boolean isConsumable() {
		return consumable;
	}

	public void setConsumable(boolean consumable) {
		this.consumable = consumable;
	}

	public int getUses() {
		return uses;
	}

	public void setUses(int uses) {
		this.uses = uses;
	}
}
