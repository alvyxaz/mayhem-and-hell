package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.entities.Player;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;
import com.friendlyblob.mayhemandhell.client.network.packets.client.NotifyReadyToPlay;

public class LoginSuccessful extends ReceivablePacket{

	int playerId;
	int x;
	int y;
	
	@Override
	public boolean read() {
		playerId = readD();
		x = readD();
		y = readD();
		return true;
	}

	@Override
	public void run() {
		// Setting up a player
		Player player = GameWorld.getInstance().player;
		player.objectId = playerId;
		player.setPosition(x, y);
		
		// Notifying server that client is ready to play
		MyGame.connection.sendPacket(new NotifyReadyToPlay());
	}

}
