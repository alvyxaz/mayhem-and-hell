package com.friendlyblob.mayhemandhell.server.model.datatables;

import java.util.Map;

import javolution.util.FastMap;

import com.friendlyblob.mayhemandhell.server.model.items.Item;
import com.friendlyblob.mayhemandhell.server.model.items.ItemInstance;

public class ItemTable {	
	
	// Item templates
	private Item [] itemTemplates;
	private final Map<Integer, Item> items;
	
	public ItemTable() {
		items = new FastMap<Integer, Item>();
		load();
	}
	
	/**
	 * Loads item templates
	 */
	private void load() {
		int highest = 0;
		
		// TODO load stuff
		buildLookupTable(highest);
	}
	
	/**
	 * Builds a quick table of item templates (for fastest possible access)
	 * @param size
	 */
	public void buildLookupTable(int size) {
		itemTemplates = new Item[size];
		// TODO put items into it.
		
	}
	
	public ItemTable getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public static final class SingletonHolder {
		public static ItemTable INSTANCE = new ItemTable();
	}
}
