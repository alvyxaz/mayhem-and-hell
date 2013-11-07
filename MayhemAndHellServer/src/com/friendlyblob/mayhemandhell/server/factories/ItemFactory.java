package com.friendlyblob.mayhemandhell.server.factories;

import com.friendlyblob.mayhemandhell.server.model.GameObject.GameObjectType;
import com.friendlyblob.mayhemandhell.server.model.instances.ItemInstance;
import com.friendlyblob.mayhemandhell.server.model.items.Item;

public class ItemFactory {

	private static ItemFactory instance;

	public ItemFactory() {
	}
	
	public static ItemFactory getInstance() {
		if (instance == null) {
			instance = new ItemFactory();
		}
		
		return instance;
	}
	
	public ItemInstance createItem(Item template) {
		ItemInstance newItem = new ItemInstance(IdFactory.getInstance().getNextId(), template);
		newItem.setType(GameObjectType.ITEM);
		
		return newItem;
	}
	
}
