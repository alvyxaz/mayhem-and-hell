package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.entities.GameObject.GameObjectType;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class ItemPickedUp extends ReceivablePacket {

	int objectId;
	int x;
	int y;
	GameObjectType type;
	int itemId;
	
	@Override
	public boolean read() {
		objectId = readD();
		itemId = readD();
		x = readD();
		y = readD();
		
		return true;
	}

	@Override
	public void run() {
		GameWorld world = GameWorld.getInstance();
		
		// TODO: add to specific slot
		// needs modification of the packets
//		world.getPlayer().getInventory().addItem(new GameObject(objectId, itemId, x, y));
	}

}
