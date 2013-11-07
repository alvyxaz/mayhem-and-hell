package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.GameObject;
import com.friendlyblob.mayhemandhell.client.entities.GameObject.GameObjectType;
import com.friendlyblob.mayhemandhell.client.entities.Item;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.gameworld.Map;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class ObjectAppeared extends ReceivablePacket {

	int objectId;
	int x;
	int y;
	GameObjectType type;
	int sprite;
	int itemId;
	
	@Override
	public boolean read() {
		objectId = readD();
		x = readD();
		y = readD();
		type = GameObjectType.fromValue(readD());
		
		switch (type) {
		case PLAYER:
		case FRIENDLY_NPC:
		case HOSTILE_NPC:
			sprite = readD();
			break;
		case ITEM:
			itemId = readD();
			break;
		}

		
		return true;
	}

	@Override
	public void run() {
		GameWorld world = GameWorld.getInstance();
		
		if (world.player.objectId == objectId) {
			return;
		}
		
		if (!world.characters.containsKey(objectId)) {
			switch (type) {
				case PLAYER:
				case FRIENDLY_NPC:
				case HOSTILE_NPC:
						world.putCharacter(new GameCharacter(objectId, x, y, sprite));
					break;
				case ITEM:
						world.putObject(new Item(objectId, itemId, x, y));
					break;
			}
			
		}
	}

}
