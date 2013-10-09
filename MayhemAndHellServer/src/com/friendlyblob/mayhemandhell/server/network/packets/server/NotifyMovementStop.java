package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;
import com.friendlyblob.mayhemandhell.server.utils.ObjectPosition;

public class NotifyMovementStop extends ServerPacket {

	private ObjectPosition position;
	private int objectId;
	
	public NotifyMovementStop(GameCharacter character) {
		this.objectId = character.getObjectId();
		this.position = character.getPosition();
	}
	
	@Override
	protected void write() {
		writeC(0x08);
		writeD(objectId);
		writeD((int) position.getX());
		writeD((int) position.getY());
	}

}
