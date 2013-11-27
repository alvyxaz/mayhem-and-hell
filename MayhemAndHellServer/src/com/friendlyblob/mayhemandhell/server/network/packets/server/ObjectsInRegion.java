package com.friendlyblob.mayhemandhell.server.network.packets.server;

import java.util.List;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.instances.ItemInstance;
import com.friendlyblob.mayhemandhell.server.model.resources.Resource;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class ObjectsInRegion extends ServerPacket{

	List<GameObject> closeObjects;
	
	public ObjectsInRegion (List<GameObject> list) {
		closeObjects = list;
	}
	
	@Override
	protected void write() {
		writeC(0x04);
		
		writeH(closeObjects.size()); // Player count

		// Writing characters
		for(GameObject object : closeObjects) {
			writeD(object.getObjectId());				// Object id
			writeD((int)object.getPosition().getX());	// X position
			writeD((int)object.getPosition().getY());	// Y position
			writeH(object.getType().value);
			
			switch (object.getType()) {
				case PLAYER:
				case FRIENDLY_NPC:
				case HOSTILE_NPC:
					writeD(((GameCharacter) object).getMovementSpeed());			// Movement speed
					writeD(((GameCharacter) object).getSprite());					// Sprite (animation)
					break;
				case ITEM:
					writeD(((ItemInstance) object).getItemId());
					break;
				case RESOURCE:
					writeS(((Resource) object).getTemplate().getName());
					writeS(((Resource) object).getTemplate().getIcon());
			}
		}
	}

}
