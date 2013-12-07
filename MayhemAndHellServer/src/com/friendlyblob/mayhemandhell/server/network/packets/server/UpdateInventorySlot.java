package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class UpdateInventorySlot extends ServerPacket {

	private int slot;
	private int itemId;
	
	public UpdateInventorySlot(int slot, int itemId) {
		this.slot = slot;
		this.itemId = itemId;
	}
	
	@Override
	protected void write() {
		writeC(0x13);
		writeD(slot);
		writeD(itemId);
	}

}
