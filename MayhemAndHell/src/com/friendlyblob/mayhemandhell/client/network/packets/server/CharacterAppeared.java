package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.gameworld.Map;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class CharacterAppeared extends ReceivablePacket {

	int characterId;
	int x;
	int y;
	int sprite;
	
	@Override
	public boolean read() {
		characterId = readD();
		x = readD();
		y = readD();
		sprite = readD();
		
		return true;
	}

	@Override
	public void run() {
		GameWorld world = GameWorld.getInstance();
		
		if (world.player.objectId == characterId) {
			return;
		}
		
		if (!world.characters.containsKey(characterId)) {
			world.putCharacter(new GameCharacter(characterId, x, y, sprite));
		}
	}

}
