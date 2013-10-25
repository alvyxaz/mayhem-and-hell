package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class CharactersLeft extends ReceivablePacket {

	private int[] ids;
	
	@Override
	public boolean read() {
		int count = readD();
		ids = new int[count];
		
		for (int i = 0; i < count; i++) {
			ids[i] = readD();
		}
		return true;
	}

	@Override
	public void run() {
		for (int i = 0; i < ids.length; i++) {
			GameWorld.getInstance().removeCharacter(ids[i]);
			
			// Remove target if character that left is the one that was targeted
			if (GameWorld.getInstance().getPlayer().targetId == ids[i]) {
				GameWorld.getInstance().game.screenGame.guiManager.targetBar.removeTarget();
			}
		}
		
		
	}

}
