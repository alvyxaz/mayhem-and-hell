package com.friendlyblob.mayhemandhell.server.model.instances;

import com.friendlyblob.mayhemandhell.server.model.items.EquipableItem;
import com.friendlyblob.mayhemandhell.server.model.items.Item;

public class EquipableItemInstance extends ItemInstance {

	private int durability;
	
	public EquipableItemInstance(int objectId, Item template) {
		super(objectId, template);
		// Copy ItemType specific variables to this new instance
		durability = ((EquipableItem) template).getDurability();
	}

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}
}
