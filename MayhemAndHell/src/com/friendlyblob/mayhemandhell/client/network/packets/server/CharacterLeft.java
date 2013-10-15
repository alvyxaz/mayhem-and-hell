package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class CharacterLeft extends ReceivablePacket {

	private int characterId;
	
	@Override
	public boolean read() {
		characterId = readD();
		return true;
	}

	@Override
	public void run() {
		GameWorld.getInstance().removeCharacter(characterId);
		
		if (GameWorld.getInstance().getPlayer().targetId == characterId) {
			GameWorld.getInstance().game.screenGame.guiManager.targetBar.visible = false;
		}
	}

}
