package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.entities.GameObject.GameObjectType;
import com.friendlyblob.mayhemandhell.client.entities.gui.TargetBar.TargetInfo;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class TargetInfoResponse extends ReceivablePacket {
	private String actions[]; // TODO use a static fixed size buffer
	
	private static TargetInfo info = new TargetInfo();
	
	@Override
	public boolean read() {
		info.cleanup();
		
		info.objectId = readD();
		
		info.name = readS();
		
		actions = new String[readD()];
		for (int i = 0; i < actions.length; i++) {
			actions[i] = readS();
		}
		
		info.objectType = GameObjectType.fromValue(readC());
		info.hasHealth = GameObjectType.hasHealth(info.objectType);
		
		// Health points. Only read if this type has health points
		if (info.hasHealth) {
			info.currentHealth = readD();
			info.maxHealth = readD();
		}
		
		return true;
	}

	@Override
	public void run() {
		GameWorld.getInstance().getPlayer().targetId = info.objectId;
		GameWorld.getInstance().game.screenGame.guiManager.targetBar.showTarget(info, actions);
	}

}
