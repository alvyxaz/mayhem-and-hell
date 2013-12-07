package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.entities.gui.inventory.InventoryItem;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class UpdateInventorySlot extends ReceivablePacket {

	private int slot;
	private int itemId;
	
	@Override
	public boolean read() {
		slot = readD();
		itemId = readD();
		
		return true;
	}

	@Override
	public void run() {
		InventoryItem inventoryItem = new InventoryItem(itemId);
		
		GameWorld.getInstance().getPlayer().getInventory().setItemAt(slot, inventoryItem);
	}
}
