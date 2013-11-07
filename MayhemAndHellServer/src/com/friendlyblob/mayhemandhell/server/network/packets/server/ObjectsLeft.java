package com.friendlyblob.mayhemandhell.server.network.packets.server;

import java.util.List;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class ObjectsLeft extends ServerPacket{
	private int[] ids;
	
	public ObjectsLeft(int id) {
		ids = new int[1];
		ids[0] = id;
	}
	
	public ObjectsLeft(List<GameObject> objects) {
		if (objects == null) {
			return;
		}
		ids = new int[objects.size()];
		int index = 0;
		for (GameObject object : objects) {
			ids[index++] = object.getObjectId();
		}
	}
	
	@Override
	protected void write() {
		writeC(0x05);
		writeD(ids.length);
		for (int i = 0; i < ids.length; i++) {
			writeD(ids[i]);
		}
	}

}
