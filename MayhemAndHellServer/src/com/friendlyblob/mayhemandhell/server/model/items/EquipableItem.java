package com.friendlyblob.mayhemandhell.server.model.items;

import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.model.stats.StatModifier;

/**
 * Represents an item that can be equiped
 * @author Alvys
 *
 */
public abstract class EquipableItem extends Item {

	/**
	 * Represents an equipment slot. Before modifying,
	 * make sure it won't affect anything in a negative way.
	 * (If adding new slot values, I strongly recommend appending them
	 * to the end.)
	 * @author Alvys
	 *
	 */
	public static enum EquipmentSlot {
		HEAD,
		CHEST,
		MAIN_HAIND,
		OFF_HAND
	}
	
	private EquipmentSlot slot;
	private StatModifier [] modifiers;
	
	/**
	 * Returns a list of stat modifiers, or null if none exist
	 * @return
	 */
	public StatModifier[] getStatModifiers() {
		return modifiers;
	}
	
	/**
	 * Returns index of a slot where this item has to be put.
	 * Normally, it's an ordinal of EquipmentSlot value. 
	 * @return
	 */
	public int getSlotIndex() {
		return slot.ordinal();
	}
	
	/**
	 * Checks whether a player can equip a given item.
	 * @param player
	 * @return true if player can equp an item
	 */
	public boolean canEquip(Player player) {
		return true;
	}
}
