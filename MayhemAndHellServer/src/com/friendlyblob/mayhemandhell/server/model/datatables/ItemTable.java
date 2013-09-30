package com.friendlyblob.mayhemandhell.server.model.datatables;

import java.util.Map;

import javolution.util.FastMap;

import com.friendlyblob.mayhemandhell.server.data.ItemDataParser;
import com.friendlyblob.mayhemandhell.server.model.instances.ItemInstance;
import com.friendlyblob.mayhemandhell.server.model.items.Item;

public class ItemTable {	
	private static ItemTable instance;
	
	// Item templates
	private Item [] itemTemplates;
	private final Map<Integer, Item> items;
	
	public ItemTable() {
		items = new FastMap<Integer, Item>();
		load();
	}
	
	public static void initialize() {
		instance = new ItemTable();
	}
	
	/**
	 * Loads item templates
	 */
	private void load() {
		int highest = 0;
		
		ItemDataParser itemParser = new ItemDataParser();
		itemParser.load();
		
		for (Item item : itemParser.getItems()) {
			if (highest < item.itemId) {
				highest = item.itemId;
			}
			items.put(item.itemId, item);
		}
		
		buildLookupTable(highest);
	}
	
	/**
	 * Builds a quick table of item templates (for fastest possible access)
	 * @param highest
	 */
	public void buildLookupTable(int highest) {
		itemTemplates = new Item[highest+1];

		for (Item item : items.values()) {
			itemTemplates[item.getItemId()] = item;
		}
		
	}
	
	public static ItemTable getInstance() {
		return instance;
	}
	
	public Item getItem(int id) {
		return itemTemplates[id];
	}
}
