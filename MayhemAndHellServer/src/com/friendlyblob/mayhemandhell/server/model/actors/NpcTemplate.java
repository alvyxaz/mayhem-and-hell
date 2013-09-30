package com.friendlyblob.mayhemandhell.server.model.actors;

import java.util.Arrays;
import java.util.Random;

import com.friendlyblob.mayhemandhell.server.model.items.Item;
import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;

/**
 * Represents a basic structure of npc character 
 * (Including hostile mobs and friendly "guys")
 * @author Alvys
 *
 */
public class NpcTemplate extends CharacterTemplate {

	public static Random random = new Random();
	
	public int npcId;
	
	public String name;
	public String type = "Npc";
	
	private ItemDrop[] itemDropList;
	
	public NpcTemplate(StatsSet set) {
		super(set);
		itemDropList = new ItemDrop[0];
	}
	
	/**
	 * Constructor for late initialization
	 */
	public NpcTemplate() {
		this(new StatsSet());
	}

	public int getNpcId() {
		return npcId;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Adds an item to npc's drop list
	 * @param item
	 * @param chance
	 */
	public void addItemDrop(Item item, float chance, int min, int max) {
		ItemDrop [] temp = Arrays.copyOf(itemDropList, itemDropList.length+1);
		temp[temp.length-1] = new ItemDrop(item, chance, min, max);
		itemDropList = temp;
	}
	
	/**
	 * Class represents an instance of one droppable item and 
	 * a chance of dropping it. Takes care of calculating whether item was
	 * dropped or not. No drop chance calculations should be done
	 * outside of this class
	 * @author Alvys
	 *
	 */
	public static class ItemDrop {
		private float chance;
		private Item item;
		private int min;
		private int max;
		
		public ItemDrop(Item item, float chance, int min, int max) {
			this.chance = chance;
			this.item = item;
		}
		
		/**
		 * Check whether a given random number
		 * @return Item if dropped, null if didn't
		 */
		public Item isDropped() {
			if (random.nextFloat()*100 > chance) {
				return item;
			}
			return null;
		}
	};
	
}
