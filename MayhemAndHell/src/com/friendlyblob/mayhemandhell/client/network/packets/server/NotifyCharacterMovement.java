package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.badlogic.gdx.Gdx;
import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.Player;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class NotifyCharacterMovement extends ReceivablePacket {

	int objectId;
	int currentX;
	int currentY;
	int destinationX;
	int destinationY;
	int movementSpeed;
	boolean teleport;
	
	@Override
	public boolean read() {
		objectId = readD();
		currentX = readD();
		currentY = readD();
		destinationX = readD();
		destinationY = readD();
		movementSpeed = readD();
		teleport = readD() == 1 ? true : false;
		return true;
	}

	@Override
	public void run() {
		Player player = GameWorld.getInstance().player;
		
		if (player.objectId == objectId) {
			player.easeByOffset(
					currentX - player.position.x, 
					currentY - player.position.y, 0.5f);
			if (teleport) {
				player.teleportTo(currentX, currentY);
			} else {
				player.moveTo(destinationX, destinationY, movementSpeed);
			}
		} else {
			GameCharacter character = GameWorld.getInstance().characters.get(objectId);
			if (character != null) {
				if (teleport) {
					character.teleportTo(currentX, currentY);
				} else {
					character.easeByOffset(
							currentX - character.position.x, 
							currentY - character.position.y, 0.5f);
					character.moveTo(destinationX, destinationY, movementSpeed);
				}
			}
		}
	}

}
