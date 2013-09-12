package com.friendlyblob.mayhemandhell.server.network.packets;

import org.mmocore.network.ReceivablePacket;

import com.friendlyblob.mayhemandhell.server.network.GameClient;

public abstract class ClientPacket extends ReceivablePacket<GameClient> {

	
	
	/**
	 * Sends a game server packet to the client.
	 * @param gsp the game server packet
	 */
	protected final void sendPacket(ServerPacket sp)
	{
		getClient().sendPacket(sp);
	}

}
