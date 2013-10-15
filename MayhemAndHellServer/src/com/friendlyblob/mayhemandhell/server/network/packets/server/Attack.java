package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class Attack extends ServerPacket {

	private boolean critical;
	private int damage;
	
	public int targetId;
	
	public Attack(int targetId) {
		this.targetId = targetId;
	}
	
	@Override
	protected void write() {
		writeC(0x0A);
		writeD(targetId);
		writeD(damage);
	}

	public boolean isCritical() {
		return critical;
	}

	public void setCritical(boolean critical) {
		this.critical = critical;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	

}