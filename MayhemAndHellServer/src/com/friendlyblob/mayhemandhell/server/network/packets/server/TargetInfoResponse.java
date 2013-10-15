package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.actions.GameActions.GameAction;
import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class TargetInfoResponse extends ServerPacket {

	public GameAction[] actions;
	public GameObject object;
	
	public TargetInfoResponse(GameObject object, GameAction [] actions) {
		this.object = object;
		this.actions = actions;
	}
	
	@Override
	protected void write() {
		writeC(0x07);
		writeD(object.getObjectId());
		writeS(object.getName());

		// Available actions
		writeD(actions.length);
		for (GameAction action : actions) {
			writeS(action.getName());
		}
		
		// Information about the target
		object.fillInfo(getBuf());
	}

}
