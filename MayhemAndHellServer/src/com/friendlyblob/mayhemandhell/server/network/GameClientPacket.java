package com.friendlyblob.mayhemandhell.server.network;

import java.nio.BufferUnderflowException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mmocore.network.ReceivablePacket;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.ActionFailed;

public abstract class GameClientPacket extends ReceivablePacket<GameClient>{

	protected static final Logger log = Logger.getLogger(GameClientPacket.class.getName());
	
	@Override
	protected abstract boolean read();
	
	@Override
	public abstract void run();
	
	/**
	 * Sends a game server packet to the client.
	 * @param gsp the game server packet
	 */
	protected final void sendPacket(ServerPacket sp)
	{
		getClient().sendPacket(sp);
	}

	
	/**
	 * Overridden with true value on some packets that should disable spawn protection (RequestItemList and UseItem only)
	 * @return
	 */
	protected boolean triggersOnActionRequest()
	{
		return true;
	}
	

	// TODO get active char
	
	protected final void sendActionFailed(){
		if (getClient() != null) {
			getClient().sendPacket(ActionFailed.STATIC_PACKET);
		}
	}

}
