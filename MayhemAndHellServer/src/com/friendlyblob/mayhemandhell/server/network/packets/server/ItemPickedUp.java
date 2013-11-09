package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.instances.ItemInstance;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class ItemPickedUp extends ServerPacket {
	private ItemInstance item;
	
	public ItemPickedUp(ItemInstance item) {
		this.item = item;
	}
	
	@Override
	protected void write() {
		writeC(0x0E);
		writeD(item.getObjectId());
		writeD(item.getItemId());
		writeD((int) item.getPosition().getX());
		writeD((int) item.getPosition().getY());
	}
}
