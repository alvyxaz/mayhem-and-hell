package com.friendlyblob.mayhemandhell.server.network.packets.client;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.World;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;

public class SetTarget extends ClientPacket {

	int id;
	
	@Override
	protected boolean read() {
		this.id = readD();
		return true;
	}

	@Override
	public void run() {
		// No need to check if null, because it will still be valid
		GameObject object = World.getInstance().getObject(id);
		getClient().getPlayer().setTarget(object);
	}
	
}
