package com.friendlyblob.mayhemandhell.client.network.packets.client;

import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;

public class RegistrationPacket extends SendablePacket{

	private String username;
	private String password;
	private String passwordRepeated;
	private int charId;
	
	public RegistrationPacket(String username, String password,
			String passwordRepeated, int charId) {
		this.username = username;
		this.password = password;
		this.passwordRepeated = passwordRepeated;
		this.charId = charId;
	}
	
	@Override
	public void write() {
		writeC(0x03);
		writeS(username);
		writeS(password);
		writeS(passwordRepeated);
		writeH(charId);
	}

}
