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
import com.friendlyblob.mayhemandhell.server.network.packets.server.RegistrationSuccessful;

public class RegisterPacket extends ClientPacket{

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
		
		try {
			Connection con = DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO users(username, password) VALUES(?, SHA1(?))");

			ps.setString(1, username);
			ps.setString(2, password);
			ps.executeUpdate();
			
			// TODO fetch player data from database and attach Player object to connection.
			// TODO Remove random generated ID at player
//			Player player = new Player(666, CharacterTemplateTable.getInstance().getTemplate("player"));
//			getClient().setPlayer(player);
//			player.setClient(getClient());
			
			getClient().sendPacket(new RegistrationSuccessful());
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
