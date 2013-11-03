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
import com.friendlyblob.mayhemandhell.server.network.GameClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.DeathNotification;
import com.friendlyblob.mayhemandhell.server.network.packets.server.LoginSuccessful;

public class LoginPacket extends ClientPacket{

	private String login;
	private String password;
	
	@Override
	protected boolean read() {
		login = readS();
		password = readS();
		return true;
	}

	@Override
	public void run() {
		// check login
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] hashDigest = null;
		try {
			hashDigest = md.digest(password.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String hash = "";
		for (int i=0; i < hashDigest.length; i++) {
			hash +=
		        Integer.toString( ( hashDigest[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		try (Connection con = DatabaseFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ? AND password = ?"))
		{
			ps.setString(1, login);
			ps.setString(2, hash);
			ResultSet rset = ps.executeQuery();
			
			while (rset.next()) {
				if (rset.getInt(1) > 0) {
					getClient().setState(GameClient.GameClientState.AUTHORIZED);
					// TODO fetch player data from database and attach Player object to connection.
					// TODO Remove random generated ID at player
					Player player = new Player(666, CharacterTemplateTable.getInstance().getTemplate("player"));
					getClient().setPlayer(player);
					player.setClient(getClient());
					
					getClient().sendPacket(
							new LoginSuccessful(
									player.getObjectId(),
									(int) player.getPosition().getX(),
									(int) player.getPosition().getY()));
					
					if (player.isDead()) {
						player.sendPacket(new DeathNotification());
					}
					
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
