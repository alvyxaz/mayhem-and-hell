package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.entities.gui.TargetBar.TargetInfo;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class TargetInfoResponse extends ReceivablePacket {

	private String targetName;
	private String actions[]; // TODO use a fixed size buffer
	
	@Override
	public boolean read() {
		targetName = readS();
		actions = new String[readD()];
		for (int i = 0; i < actions.length; i++) {
			actions[i] = readS();
		}
		return true;
	}

	@Override
	public void run() {
		TargetInfo info = new TargetInfo();
		info.name = targetName;
		GameWorld.getInstance().game.screenGame.guiManager.targetBar.showTarget(info, actions);
	}

}
