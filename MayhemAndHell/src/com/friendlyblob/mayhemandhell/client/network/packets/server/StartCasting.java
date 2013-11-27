package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class StartCasting extends ReceivablePacket {

	@Override
	public boolean read() {
		int time = readD();
		String text = readS();
		GameWorld.getInstance().game.screenGame.guiManager.castingBar.startCasting(text, time);
		return false;
	}

	@Override
	public void run() {
		
	}

}
