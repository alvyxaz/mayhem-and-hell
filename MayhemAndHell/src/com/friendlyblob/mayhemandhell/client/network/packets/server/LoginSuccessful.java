package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.entities.Player;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;
import com.friendlyblob.mayhemandhell.client.network.packets.client.NotifyReadyToPlay;
import com.friendlyblob.mayhemandhell.client.screens.GameScreen;

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
		Player player = new Player(playerId, x, y);
		
		GameWorld.getInstance().player = player;
		GameWorld.getInstance().putCharacter(player);
		
		MyGame.getInstance().setScreen(MyGame.getInstance().screenGame);
	}

}
