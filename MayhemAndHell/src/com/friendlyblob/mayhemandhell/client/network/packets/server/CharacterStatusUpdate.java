package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.entities.gui.TargetBar.TargetInfo;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class CharacterStatusUpdate extends ReceivablePacket {

	private int objectId;
	private int currentHealth;
	private int maxHealth;
	
	@Override
	public boolean read() {
		objectId = readD();
		currentHealth = readD();
		maxHealth = readD();
		return true;
	}

	@Override
	public void run() {
		
		if (objectId == GameWorld.getInstance().player.objectId ) {
			GameWorld.getInstance().game.screenGame.guiManager.playerStatus.setHealth(currentHealth, maxHealth);
			return;
		}
		
		TargetInfo info = GameWorld.getInstance().game.screenGame.guiManager.targetBar.getTargetInfo();
	
		if (info != null) {
			info.currentHealth = currentHealth;
			info.maxHealth = maxHealth;
			GameWorld.getInstance().game.screenGame.
					guiManager.targetBar.updateHealthPercentage();
		}
		
	}

}
