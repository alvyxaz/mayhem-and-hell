package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.entities.Player;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;
import com.friendlyblob.mayhemandhell.client.network.packets.client.NotifyReadyToPlay;

public class LoginFailure extends ReceivablePacket{
	
	private String message;
	
	@Override
	public boolean read() {
		this.message = readS();
		
		return true;
	}

	@Override
	public void run() {
		MyGame.getInstance().screenLogin.showErrorMessage(message);
	}

}
