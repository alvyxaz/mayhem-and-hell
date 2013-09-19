package com.friendlyblob.mayhemandhell.server.network.packets.client;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.ActionFailedMessage;
import com.friendlyblob.mayhemandhell.server.network.packets.server.TargetInfoResponse;

public class RequestTarget extends ClientPacket {

	int objectId;
	
	@Override
	protected boolean read() {
		objectId = readD();
		return true;
	}

	@Override
	public void run() {
		
		if (objectId == -1) {
			// If requesting to remove the target
			getClient().getPlayer().removeTarget();
		}
		
		GameObject object = getClient().getPlayer().getRegion().getObject(objectId);
		
		if (getClient().getPlayer().setTarget(object)) {
			// If target successfully set
			getClient().sendPacket(new TargetInfoResponse(object.getName()));
		} else {
			// Notify that action has failed
			getClient().sendPacket(new ActionFailedMessage("Invalid target"));
		}
	}

}
