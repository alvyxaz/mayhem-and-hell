package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.instances.ItemInstance;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class ObjectAppeared extends ServerPacket {
	private GameObject object;
	
	public ObjectAppeared(GameObject object) {
		this.object = object;
	}
	
	@Override
	protected void write() {
		writeC(0x03);
		writeD(object.getObjectId());
		writeD((int) object.getPosition().getX());
		writeD((int) object.getPosition().getY());
		writeD(object.getType().value);
		
		// ObjectGameType specific
		switch (object.getType()) {
			case PLAYER:
			case FRIENDLY_NPC:
			case HOSTILE_NPC:
				writeD(((GameCharacter) object).getSprite());
				break;
			case ITEM:
				writeD(((ItemInstance) object).getItemId());
				break;
		}
	}
}
