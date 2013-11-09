package com.friendlyblob.mayhemandhell.server.model.instances;

import com.friendlyblob.mayhemandhell.server.model.items.Item;
import com.friendlyblob.mayhemandhell.server.model.items.UsableItem;

public class UsableItemInstance extends ItemInstance {

	private boolean consumable;
	private int uses;
	
	public UsableItemInstance(int objectId, Item template) {
		super(objectId, template);
		// Copy ItemType specific variables to this new instance
		UsableItem item = (UsableItem) template;
		consumable = item.isConsumable();
		uses = item.getUses();
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
