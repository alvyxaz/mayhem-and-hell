package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.GameObject;
import com.friendlyblob.mayhemandhell.client.entities.GameObject.GameObjectType;
import com.friendlyblob.mayhemandhell.client.entities.Item;
import com.friendlyblob.mayhemandhell.client.entities.Resource;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.gameworld.Map;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
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
				int typeValue = readH();
				GameObjectType type = GameObjectType.fromValue(typeValue);
				
				switch (type) {
					case PLAYER:
					case FRIENDLY_NPC:
					case HOSTILE_NPC:
						int speed = readD();
						int sprite = readD();
						int hint = readC();
						// Ignoring player himself. 
						// Data has to be read anyway (before this if).
						if(playerId == objectId) {
							continue;
						}
						
						if (world.characterExists(objectId)) {
							// TODO some sort of interpolation
							// world.getCharacter(objectId).moveTo(x, y, speed);
						} else {
							GameCharacter character = new GameCharacter(objectId, x, y, sprite);
							
							if (hint > 0) {
								character.setHint(hint);
							}
							
							world.putCharacter(character);
						}
						break;
					case ITEM:
						int itemId = readD();
						
						Item item = new Item(itemId, objectId);
						item.setPosition(x, y);
						
						world.putObject(item);
						break;
					case RESOURCE:
						// Resource data
						String name = readS();
						String icon = readS();
						
						Resource resource = new Resource(objectId, name, icon);
						resource.setPosition(x, y);
						world.putObject(resource);
						break;
				case OTHER:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public void run() {
		
	}

}
