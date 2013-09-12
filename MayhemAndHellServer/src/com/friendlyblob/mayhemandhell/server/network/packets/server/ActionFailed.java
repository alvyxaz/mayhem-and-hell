package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;


public class ActionFailed extends ServerPacket{
	public static final ActionFailed STATIC_PACKET = new ActionFailed();

	@Override
	protected void write() {
		writeC(0x1f);
	}
}
