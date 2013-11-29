package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.model.skills.Castable;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class StartCasting extends ServerPacket {

	private String name;
	private int time;
	
	public StartCasting(Castable skill) {
		this.name = skill.getName();
		this.time = skill.getCastingTime();
	}
	
	@Override
	protected void write() {
		writeC(0X10);
		writeD(time);
		writeS(name);
	}
	
}
