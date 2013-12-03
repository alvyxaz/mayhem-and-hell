package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class UpdateCharacterHint extends ReceivablePacket {

	@Override
	public boolean read() {
		int size = readD();
		
		for (int i = 0; i < size; i++) {
			int objectId = readD();
			int hint = readC();
			
			GameCharacter character = GameWorld.getInstance().characters.get(objectId);
			if (character != null) {
				character.setHint(hint);
			}
		}
		
		return false;
	}

	@Override
	public void run() {
		
	}

}
