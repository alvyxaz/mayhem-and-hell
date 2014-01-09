package com.friendlyblob.mayhemandhell.server.network.packets.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.friendlyblob.mayhemandhell.server.DatabaseFactory;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.RegistrationFailure;
import com.friendlyblob.mayhemandhell.server.network.packets.server.RegistrationSuccessful;

public class RegistrationPacket extends ClientPacket{

	private String username;
	private String password;
	private String passwordRepeated;
	private int charId;

	@Override
	protected boolean read() {
		username = readS();
		password = readS();
		passwordRepeated = readS();
		charId = readH();
		
		return true;
	}

	@Override
	public void run() {
				
		if (!username.matches("\\w{5,15}")) {
			getClient().sendPacket(new RegistrationFailure("Username should consist only of letters, numbers and _"));
			return;
		}
		
		if (!password.matches("\\w{5,15}")) {
			getClient().sendPacket(new RegistrationFailure("Password should consist only of letters, numbers and '_'"));
			return;
		}
		
		if (!password.equals(passwordRepeated)) {
			getClient().sendPacket(new RegistrationFailure("Both passwords should be equal"));
			return;
		}
		
		// try finding whether such username is taken
		try {
			Connection con = DatabaseFactory.getInstance().getConnection();
			PreparedStatement selectionQuery = con.prepareStatement("SELECT * FROM users WHERE username = ?");

			selectionQuery.setString(1, username);
			ResultSet rset = selectionQuery.executeQuery();
			
			if (rset.next()) {
				getClient().sendPacket(new RegistrationFailure("Sorry, but this username is already taken"));
				return;
			}
		
			// Everything's fine, insert new user
			PreparedStatement insertionQuery = con.prepareStatement("INSERT INTO users(username, password, char_id, last_x, last_y) VALUES(?, SHA1(?), ?, 100, 100)");
	
			insertionQuery.setString(1, username);
			insertionQuery.setString(2, password);
			// TODO check whether character id is valid
			insertionQuery.setInt(3, charId);
			insertionQuery.executeUpdate();
			
			getClient().sendPacket(new RegistrationSuccessful());
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
