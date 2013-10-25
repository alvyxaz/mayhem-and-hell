package com.friendlyblob.mayhemandhell.server.network.packets.server;

import java.util.List;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class CharactersLeft extends ServerPacket{
	private int[] ids;
	
	public CharactersLeft(int id) {
		ids = new int[1];
		ids[0] = id;
	}
	
	public CharactersLeft(List<GameCharacter> characters) {
		if (characters == null) {
			return;
		}
		ids = new int[characters.size()];
		int index = 0;
		for (GameCharacter character : characters) {
			ids[index++] = character.getObjectId();
		}
	}
	
	@Override
	protected void write() {
		writeC(0x05);
		writeD(ids.length);
		for (int i = 0; i < ids.length; i++) {
			writeD(ids[i]);
		}
	}

}
