package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter.MovementData;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;
import com.friendlyblob.mayhemandhell.server.utils.ObjectPosition;

public class NotifyCharacterMovement extends ServerPacket {

	private int objectId;
	private MovementData movement;
	private ObjectPosition position;
	
	public NotifyCharacterMovement(int id, MovementData movement, ObjectPosition position) {
		this.objectId = id;
		this.movement = movement;
		this.position = position;
	}
	
	@Override
	protected void write() {
		writeC(0x06);
		writeD(objectId);						// Object ID
		writeD((int) position.getX()); 			// Current X
		writeD((int) position.getY()); 			// Current Y
		writeD((int) movement.destinationX); 	// Destination X
		writeD((int) movement.destinationY); 	// Destination Y
		writeD(movement.movementSpeed);			// Movement Speed
	}

}
