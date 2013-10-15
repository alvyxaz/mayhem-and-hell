package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class CharacterStatusUpdate extends ServerPacket {

	private GameCharacter character;
	
	public CharacterStatusUpdate(GameCharacter character) {
		this.character = character;
	}
	
	@Override
	protected void write() {
		writeC(0x0B);
		writeD(character.getObjectId());
		writeD(character.getHealth());
		writeD(character.getMaxHealth());
	}

}
