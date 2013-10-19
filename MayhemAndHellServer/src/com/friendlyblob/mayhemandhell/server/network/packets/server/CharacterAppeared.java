package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class CharacterAppeared extends ServerPacket {

	private GameCharacter character;
	
	public CharacterAppeared(GameCharacter character) {
		this.character = character;
	}
	
	@Override
	protected void write() {
		writeC(0x03);
		writeD(character.getObjectId());
		writeD((int) character.getPosition().getX());
		writeD((int) character.getPosition().getY());
		writeD(character.getSprite());
	}
	

}
