package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.GameObject;
import com.friendlyblob.mayhemandhell.client.entities.GameObject.GameObjectType;
import com.friendlyblob.mayhemandhell.client.entities.Item;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.gameworld.Map;
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
		
		// add to inventory
		world.getPlayer().getInventory().addItem(new Item(objectId, itemId, x, y));
	}

}
