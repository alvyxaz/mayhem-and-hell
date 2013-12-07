package com.friendlyblob.mayhemandhell.server.network.packets.server;

import java.util.List;

import com.friendlyblob.mayhemandhell.server.model.items.Item;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class ShowShop extends ServerPacket {
	
	private List<Item> itemList;
	
	public ShowShop(List<Item> itemList) {
		this.itemList = itemList;
	}
	
	@Override
	protected void write() {
		writeC(0x11);
		writeD(itemList.size());
		
		for (Item item : itemList) {
			writeD(item.getItemId());
			writeS(item.getName());
			writeD(item.getStatsSet().getInt("price"));
		}
	}
}
