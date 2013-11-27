package com.friendlyblob.mayhemandhell.server.model.items;

import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;

public class Material extends Item{

	public Material(int itemId, String name, StatsSet set) {
		super(itemId, name, set);
		this.setType(ItemType.MATERIAL);
	}

}
