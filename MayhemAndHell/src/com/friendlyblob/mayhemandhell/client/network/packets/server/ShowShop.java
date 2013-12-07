package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.entities.gui.shop.Shop;
import com.friendlyblob.mayhemandhell.client.entities.gui.shop.ShopListItem;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;


public class ShowShop extends ReceivablePacket {

	private ShopListItem [] itemList;
	
	@Override
	public boolean read() {
		int itemCount = readD();
		
		itemList = new ShopListItem[itemCount];
		for (int i = 0; i < itemCount; i++) {
			itemList[i] = new ShopListItem(readD(), readS(), readD());
		}
		
		return true;
	}

	@Override
	public void run() {
		Shop shop = connection.game.screenGame.guiManager.shop;
		
		shop.setItemList(itemList);
		shop.show();
	}
}
