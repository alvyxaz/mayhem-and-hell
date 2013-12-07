package com.friendlyblob.mayhemandhell.server.model.datatables;

import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.friendlyblob.mayhemandhell.server.data.ShopDataParser;
import com.friendlyblob.mayhemandhell.server.model.actors.NpcTemplate;
import com.friendlyblob.mayhemandhell.server.model.items.Item;

public class ShopTable {
	private static ShopTable instance;
	
	private List<Item> [] shopLookupTable;
	private Map<Integer, List<Item>> shops;
	
	public static void initialize() {
		instance = new ShopTable();
	}
	
	public ShopTable() {
		load();
	}
	
	public void load() {
		int highest = 0;
		
		ShopDataParser parser = new ShopDataParser();
		parser.load();
		
		shops = parser.getShops();
		
		for (Integer shopId : shops.keySet()) {
			if (highest < shopId) {
				highest = shopId;
			}
		}
		buildLookupTable(highest);
	}
	
	/**
	 * Builds a quick table of shops (for fastest possible access)
	 * @param highest
	 */
	public void buildLookupTable(int highest) {
		shopLookupTable = new FastList[highest+1];

		for (Map.Entry<Integer, List<Item>> shop : shops.entrySet()) {
			shopLookupTable[shop.getKey()] = shop.getValue();
		}
		
	}
	
	public List<Item> getShop(int index) {
		return shopLookupTable[index];
	}
	
	public static ShopTable getInstance() {
		return instance;
	}
	
}
