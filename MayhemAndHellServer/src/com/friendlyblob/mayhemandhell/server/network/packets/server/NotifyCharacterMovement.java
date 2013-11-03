package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter.MovementData;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;
import com.friendlyblob.mayhemandhell.server.utils.ObjectPosition;

/**
 * Server->Client package with information of character movement
 * @author Alvys
 *
 */
public class NotifyCharacterMovement extends ServerPacket {

	private int objectId;
	private MovementData movement;
	private ObjectPosition position;
	private boolean teleport;
	
	/**
	 * Character movement packet
	 * @param id
	 * @param movement
	 * @param position
	 */
	public NotifyCharacterMovement(GameCharacter character) {
		this(character, false);
	}
	
	public NotifyCharacterMovement(GameCharacter character, boolean teleport) {
		this.objectId = character.getObjectId();
		this.movement = character.getMovement();
		this.position = character.getPosition();
		this.teleport = teleport;
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
		writeD(teleport ? 1 : 0);				// Whether or not teleport
	}

}
