package com.friendlyblob.mayhemandhell.server.network.packets.client;

import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;

public class EquipItem extends ClientPacket {

	int itemId;
	boolean equip;	// Unequip if false
	
	@Override
	protected boolean read() {
		equip = (readC() == 1)? true : false; // True if 1
		itemId = readD();
		return true;
	}

	@Override
	public void run() {
		if (equip) {
			// Equipping
			if (getClient().getPlayer().equipItem(itemId)) {
				// TODO send some sort of feedback with "success"
			} else {
				// TODO send some sort of feedback with "fail"
			}
		} else {
			// Unequipping
			getClient().getPlayer().unequipItem(itemId);
		}
		
	}

}
