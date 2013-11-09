package com.friendlyblob.mayhemandhell.server.model.items;

import java.util.Arrays;

import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.model.stats.StatModifier;
import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;

/**
 * Represents an item that can be equiped
 * @author Alvys
 *
 */
public class EquipableItem extends Item {

	/**
	 * Represents an equipment slot. Before modifying,
	 * make sure it won't affect anything in a negative way.
	 * (If adding new slot values, I strongly recommend appending them
	 * to the end.)
	 * @author Alvys
	 *
	 */
	public static enum EquipmentSlot {
		// Armor
		HEAD,
		CHEST,
		BOOTS,
		GLOVES,
		LEGS,
		
		// Weapons
		MAIN_HAND,
		OFF_HAND,
		
		// Etc
		RING_1,
		RING_2,
		
	}
	
	
	private EquipmentSlot slot;
	private StatModifier [] modifiers;
	
	private int durability;
	
	public EquipableItem(int itemId, String name, StatsSet set) {
		super(itemId, name, set);
		modifiers = new StatModifier[0];
	}
	
	public void addModifier(StatModifier modifier) {
		StatModifier [] temp = Arrays.copyOf(modifiers, modifiers.length+1);
		modifiers = temp;
	}
	
	
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

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}
}
