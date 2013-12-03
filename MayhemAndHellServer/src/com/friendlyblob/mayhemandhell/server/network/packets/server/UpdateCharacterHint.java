package com.friendlyblob.mayhemandhell.server.network.packets.server;

import java.util.List;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class UpdateCharacterHint extends ServerPacket {

	private List<GameCharacter> characters;
	private Player player;
	
	public UpdateCharacterHint(List<GameCharacter> characters, Player player) {
		this.characters = characters;
		this.player = player;
	}
	
	@Override
	protected void write() {
		writeC(0x12);
		writeD(characters.size());
		for(GameCharacter character : characters) {
			writeD(character.getObjectId());
			writeC(character.getHint(player).value);
		}
	}

}
