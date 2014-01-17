package com.friendlyblob.mayhemandhell.client.network.packets.client;

import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;

public class LoginPacket extends SendablePacket{

	private String username;
	private String password;
	
	public LoginPacket(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	public void write() {
		writeC(0x02);
		writeS(username);
		writeS(password);
	}

}
