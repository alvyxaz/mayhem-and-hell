package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class CharacterAppeared extends ReceivablePacket {

	int characterId;
	int x;
	int y;
	
	@Override
	public boolean read() {
		characterId = readD();
		x = readD();
		y = readD();
		return true;
	}

	@Override
	public void run() {
		GameWorld world = GameWorld.getInstance();
		
		if (world.player.objectId == characterId) {
			return;
		}
		
		if (!world.characters.containsKey(characterId)) {
			world.characters.put(characterId, new GameCharacter(characterId, x, y) );
		}
	}

}
