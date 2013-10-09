package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.Player;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class NotifyMovementStop extends ReceivablePacket {
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
		Player player = GameWorld.getInstance().player;
		if (player.objectId == objectId) {
			// TODO if setting a position like this with a ping > 200 causes visible
			// glitches, perhaps try to just stop the movement and not set a new position
			// (if difference is not too big).
			player.setPosition(x, y);
		} else {
			GameCharacter character = GameWorld.getInstance().characters.get(objectId);
			if (character != null) {
				character.setPosition(x, y);
			}
		}
	}
	
}
