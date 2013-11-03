package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class DeathNotification extends ServerPacket {

	private boolean hideWindow;
	
	public DeathNotification() {
		this(false);
	}
	
	/**
	 * Whether or not resurrection window should be hidden from client 
	 * (happens when player ir resurrected)
	 * @param hideWindow
	 */
	public DeathNotification(boolean hideWindow) {
		this.hideWindow = hideWindow;
	}
	
	@Override
	protected void write() {
		writeC(0x0C);
		if (hideWindow) {
			writeD(1);
		} else {
			writeD(0);
		}
	}

}
