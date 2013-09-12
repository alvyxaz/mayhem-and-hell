package com.friendlyblob.mayhemandhell.server.network.packets.server;

import java.util.List;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class ObjectsInRegion extends ServerPacket {

	private List<GameObject> closeObjects;
	
	public ObjectsInRegion(List<GameObject> objects) {
		this.closeObjects = objects;
	}
	
	@Override
	protected void write() {
		writeC(0x06);
		
		writeH(closeObjects.size()); // Object count
		
		for(GameObject object: closeObjects) {
			// TODO save object data
		}
	}

}
