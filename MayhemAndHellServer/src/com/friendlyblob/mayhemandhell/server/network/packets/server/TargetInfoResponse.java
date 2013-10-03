package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.actions.GameActions.GameAction;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class TargetInfoResponse extends ServerPacket {

	public String name;
	public GameAction[] actions;
	
	public TargetInfoResponse(String name, GameAction [] actions) {
		this.name = name;
		this.actions = actions;
	}
	
	@Override
	protected void write() {
		writeC(0x07);
		writeS(name);
		writeD(actions.length);
		for (GameAction action : actions) {
			writeS(action.getName());
		}
	}

}
