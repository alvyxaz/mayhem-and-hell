package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.entities.Player;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;
import com.friendlyblob.mayhemandhell.client.network.packets.client.NotifyReadyToPlay;

public class RegistrationSuccessful extends ReceivablePacket{
	
	@Override
	public boolean read() {

		return true;
	}

	@Override
	public void run() {
		System.out.println("REG SUCCESS! show login screen");
		MyGame.getInstance().setScreen(MyGame.getInstance().screenLogin);
	}

}
