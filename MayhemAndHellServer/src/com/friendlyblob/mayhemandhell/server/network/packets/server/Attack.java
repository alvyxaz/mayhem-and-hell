package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class Attack extends ServerPacket {

	private boolean critical;
	private int damage;
	private float angle;
	
	private int targetId;
	private int attackerId;
	
	public Attack(int targetId, int attackerId, float angle) {
		this.targetId = targetId;
		this.attackerId = attackerId;
		this.angle = angle;
	}
	
	@Override
	protected void write() {
		writeC(0x0A);
		writeD(targetId);
		writeD(attackerId);
		writeD(damage);
		writeF(angle);
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
