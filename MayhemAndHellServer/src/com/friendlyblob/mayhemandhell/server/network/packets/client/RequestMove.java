package com.friendlyblob.mayhemandhell.server.network.packets.client;

import com.friendlyblob.mayhemandhell.server.ai.Intention;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.utils.ObjectPosition;

public class RequestMove extends ClientPacket{

	private int x;
	private int y;
	
	@Override
	protected boolean read() {
		try {
			x = readD();
			y = readD();
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	@Override
	public void run() {
		if (!getClient().getPlayer().isDead()) {
			getClient().getPlayer().getAi().setIntention(Intention.MOVE_TO, new ObjectPosition(x, y));
		}
	}

}
