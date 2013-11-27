package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class StartCasting extends ServerPacket {

	private String name;
	private int time;
	
	public StartCasting(String name, int time) {
		this.name = name;
		this.time = time;
	}
	
	@Override
	protected void write() {
		writeC(0X10);
		writeD(time);
		writeS(name);
	}
	
}
