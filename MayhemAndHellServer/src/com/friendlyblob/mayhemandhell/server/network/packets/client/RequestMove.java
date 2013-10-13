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
		x = readD();
		y = readD();
		return true;
	}

	@Override
	public void run() {
		// TODO Have in mind that player might change speed while moving
		getClient().getPlayer().getAi().setIntention(Intention.MOVE_TO, new ObjectPosition(x, y));
	}

}
