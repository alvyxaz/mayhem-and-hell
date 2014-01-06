package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class LoginSuccessful extends ServerPacket {

	private int playerId;
	private int x;
	private int y;
	
	public LoginSuccessful (int id, int x, int y) {
		this.x = x;
		this.y = y;
		this.playerId = id;
	}
	
	@Override
	protected void write() {
		writeC(0x02);
		writeD(playerId);
		writeD(x);
		writeD(y);
	}

}
