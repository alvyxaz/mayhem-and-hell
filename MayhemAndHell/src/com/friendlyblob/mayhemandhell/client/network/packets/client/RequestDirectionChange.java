package com.friendlyblob.mayhemandhell.client.network.packets.client;

import com.friendlyblob.mayhemandhell.client.controls.Input.Direction;
import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;

public class RequestDirectionChange extends SendablePacket {

	private Direction direction;
	
	public RequestDirectionChange(Direction direction) {
		this.direction = direction;
	}
	
	@Override
	public void write() {
		writeC(0x09);
		writeC(direction.ordinal());
	}

}
