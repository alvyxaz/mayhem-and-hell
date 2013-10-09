package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class AutoAttack extends ServerPacket {

	private int attackStarterId;
	private int start;
	
	/**
	 * 
	 * @param objectId
	 * @param start true if auto attack is starting, false if ending
	 */
	public AutoAttack(int objectId, boolean start) {
		this.attackStarterId = objectId;
		this.start = start ? 1 : 0;
	}
	
	@Override
	protected void write() {
		writeC(0x09);
		writeD(attackStarterId);
		writeC(start);
	}

}
