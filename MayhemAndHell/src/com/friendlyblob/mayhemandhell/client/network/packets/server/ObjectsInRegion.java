package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.GameObject;
import com.friendlyblob.mayhemandhell.client.entities.GameObject.GameObjectType;
import com.friendlyblob.mayhemandhell.client.entities.Item;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.gameworld.Map;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class ObjectsInRegion extends ReceivablePacket {

	int objectCount;
	
	@Override
	public boolean read() {
		objectCount = readH();
		
		GameWorld world = GameWorld.getInstance();
		
		int playerId = world.getPlayer().objectId;
		
		try {
			for (int i = 0; i < objectCount; i++) {
				int objectId = readD();
				int x = readD();
				int y = readD();
				GameObjectType type = GameObjectType.fromValue(readH());
				
				int sprite = 0, itemId = 0;
				switch (type) {
					case PLAYER:
					case FRIENDLY_NPC:
					case HOSTILE_NPC:
						int speed = readD();
						sprite = readD();
						break;
					case ITEM:
						itemId = readD();
						break;
				}
				
				
				// TODO player id doesn't work
				if(playerId == objectId) {
					continue;
				}
				
				if (world.characterExists(objectId)) {
					// TODO some sort of interpolation
					// world.getCharacter(objectId).moveTo(x, y, speed);
				} else {
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
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		return false;
	}

	@Override
	public void run() {
		
	}

}
