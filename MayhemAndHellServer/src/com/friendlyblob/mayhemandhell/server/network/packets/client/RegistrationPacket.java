package com.friendlyblob.mayhemandhell.server.network.packets.client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.friendlyblob.mayhemandhell.server.DatabaseFactory;
import com.friendlyblob.mayhemandhell.server.model.World;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.model.datatables.CharacterTemplateTable;
import com.friendlyblob.mayhemandhell.server.network.GameClient;
import com.friendlyblob.mayhemandhell.server.network.GameClient.GameClientState;
import com.friendlyblob.mayhemandhell.server.network.GameClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.DeathNotification;
import com.friendlyblob.mayhemandhell.server.network.packets.server.LoginSuccessful;
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
		
		// try finding whether such username taken
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
			PreparedStatement insertionQuery = con.prepareStatement("INSERT INTO users(username, password) VALUES(?, SHA1(?))");
	
			insertionQuery.setString(1, username);
			insertionQuery.setString(2, password);
			insertionQuery.executeUpdate();
			
			// TODO fetch player data from database and attach Player object to connection.
			// TODO Remove random generated ID at player
//				Player player = new Player(666, CharacterTemplateTable.getInstance().getTemplate("player"));
//				getClient().setPlayer(player);
//				player.setClient(getClient());
			
			getClient().sendPacket(new RegistrationSuccessful());
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
