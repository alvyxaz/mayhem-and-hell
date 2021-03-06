package com.friendlyblob.mayhemandhell.server.model.instances;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.model.items.EquipableItem;
import com.friendlyblob.mayhemandhell.server.model.items.Item;
import com.friendlyblob.mayhemandhell.server.model.items.Item.ItemType;
import com.friendlyblob.mayhemandhell.server.model.items.UsableItem;
import com.friendlyblob.mayhemandhell.server.model.stats.StatModifier;

public class ItemInstance extends GameObject {

	private Item template;
	
	public ItemInstance(int objectId, Item template) {
		super(objectId);
		this.template = template;
		
		this.setType(GameObjectType.ITEM);
	}
	
	public int getItemId() {
		return template.getItemId();
	}

	public boolean isEquipable() {
		return template instanceof EquipableItem;
	}
	
	@Override
	public String getName() {
		return template.getName();
	}
	
	/**
	 * Returns a slot index. Will throw an error if template is not
	 * an instance of EquipableItem. Skipping the check for performance.
	 * @return
	 */
	public int getSlotIndex() {
		return ((EquipableItem)template).getSlotIndex();
	}
	
	/**
	 * Whether or not user can equip this item.
	 * @param player
	 * @return
	 */
	public boolean canEquip(Player player) {
		return ((EquipableItem)template).canEquip(player);
	}
	
	public StatModifier[] getStatModifiers() {
		return ((EquipableItem)template).getStatModifiers();
	}
	
	/**
	 * Whether user can use this item.
	 * @return
	 */
	public boolean canBeUsed() {
		switch (template.getType()) {
			case USABLE:
				UsableItem item = (UsableItem) template;
				
				if (item.isConsumable() && item.getUses() > 0) {
					return true;
				}
				break;
			case EQUIPMENT:
				if (((EquipableItem) template).getDurability() > 0) {
					return true;
				}
				break;
		}
		
		return false;
	}
}
