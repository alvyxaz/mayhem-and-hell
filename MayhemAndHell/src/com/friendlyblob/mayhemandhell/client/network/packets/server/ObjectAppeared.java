package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.GameObject.GameObjectType;
import com.friendlyblob.mayhemandhell.client.entities.Item;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class ObjectAppeared extends ReceivablePacket {

	/**
	 * @WARNING Might cause trouble with PING emulation
	 */
	
	int objectId;
	int x;
	int y;
	GameObjectType type;
	int sprite;
	int itemId;
	int hint;
	
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
				hint = readC();
				break;
			case ITEM:
				itemId = readD();
				break;
		case OTHER:
			break;
		case RESOURCE:
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
		
		if (!world.objectExists(objectId)) {
			switch (type) {
				case PLAYER:
				case FRIENDLY_NPC:
				case HOSTILE_NPC:
					GameCharacter character = new GameCharacter(objectId, x, y, sprite);
					
					if (hint > 0) {
						character.setHint(hint);
					}
					
					world.putCharacter(character);
					break;
				case ITEM:
						Item item = new Item(itemId, objectId);
						item.setPosition(x, y);
						
						world.putObject(item);
					break;
			case OTHER:
				break;
			case RESOURCE:
				break;
			}
			
		}
	}

}
