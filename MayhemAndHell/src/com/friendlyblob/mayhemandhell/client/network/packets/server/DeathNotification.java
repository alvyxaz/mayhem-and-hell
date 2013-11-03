package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class DeathNotification extends ReceivablePacket{

	private boolean hideWindow;
	
	@Override
	public boolean read() {
		hideWindow = readD() == 1 ? true : false;
		return true;
	}

	@Override
	public void run() {
		GameWorld.getInstance().game.screenGame.guiManager.targetBar.removeTarget();
		
		if (hideWindow) {
			GameWorld.getInstance().game.screenGame.guiManager.resurrection.hide();
		} else {
			GameWorld.getInstance().game.screenGame.guiManager.resurrection.display();
		}
	}

}
