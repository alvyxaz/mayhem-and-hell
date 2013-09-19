package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.entities.gui.TargetBar.TargetInfo;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class TargetInfoResponse extends ReceivablePacket {

	private String targetName;
	
	@Override
	public boolean read() {
		targetName = readS();
		return true;
	}

	@Override
	public void run() {
		TargetInfo info = new TargetInfo();
		info.name = targetName;
		GameWorld.getInstance().game.screenGame.guiManager.targetBar.showTarget(info);
	}

}
