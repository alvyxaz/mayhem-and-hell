package com.friendlyblob.mayhemandhell.client.network.packets.client;

import com.badlogic.gdx.math.Vector2;
import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;

public class PositionUpdate extends SendablePacket{

	private Vector2 position;
	
	public PositionUpdate(Vector2 position) {
		this.position = position;
	}
	
	@Override
	public void write() {
		writeC(0X09);
		writeD((int)position.x);
		writeD((int)position.y);
	}

}
