package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class StartCasting extends ReceivablePacket {

	private int time;
	private String text;
	
	@Override
	public boolean read() {
		time = readD();
		text = readS();
		return true;
	}

	@Override
	public void run() {
		if (time > 0) {
//			GameWorld.getInstance().game.screenGame.guiManager.castingBar.startCasting(text, time);
		} else {
//			GameWorld.getInstance().game.screenGame.guiManager.castingBar.hide();
		}
	}

}
