package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.Player;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class NotifyPositionUpdate extends ReceivablePacket {

	private static final int MIN_SPEED = 1; // px per second
	
	private int objectId;
	private int x;
	private int y;
	
	@Override
	public boolean read() {
		objectId = readD();
		x = readD();
		y = readD();
		return true;
	}

	@Override
	public void run() {
		if (objectId != GameWorld.getInstance().getPlayer().objectId) {
			GameCharacter character = GameWorld.getInstance().getCharacter(objectId);
			int movementSpeed = Math.max((int)(character.position.dst(x, y)/Player.POSITION_UPDATE_RATE), MIN_SPEED);
			character.moveTo(x, y, movementSpeed);
		}
	}

}
