package com.friendlyblob.mayhemandhell.server.network.packets.client;

import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.NotifyPositionUpdate;

public class PositionUpdate extends ClientPacket {

	private int x;
	private int y;
	
	@Override
	protected boolean read() {
		x = readD();
		y = readD();
		return true;
	}

	@Override
	public void run() {
		getClient().getPlayer().updatePosition(x, y);
		getClient().getPlayer().getRegion().broadcastToCloseRegions(
				new NotifyPositionUpdate(getClient().getPlayer()));
	}

}
