package com.friendlyblob.mayhemandhell.server.network.packets.client;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.eclipse.jdt.internal.compiler.batch.Main;

import com.friendlyblob.mayhemandhell.server.DatabaseFactory;
import com.friendlyblob.mayhemandhell.server.model.World;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.model.datatables.CharacterTemplateTable;
import com.friendlyblob.mayhemandhell.server.network.GameClient;
import com.friendlyblob.mayhemandhell.server.network.GameClientPacket;
import com.friendlyblob.mayhemandhell.server.network.GameClient.GameClientState;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.DeathNotification;
import com.friendlyblob.mayhemandhell.server.network.packets.server.LoginFailure;
import com.friendlyblob.mayhemandhell.server.network.packets.server.LoginSuccessful;

public class LoginPacket extends ClientPacket{

	private String username;
	private String password;
	
	@Override
	protected boolean read() {
		username = readS();
		password = readS();
		return true;
	}

	@Override
	public void run() {

		try {
			Connection con = DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = SHA1(?)");

			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rset = ps.executeQuery();

			if (rset.next()) {
				getClient().setState(GameClientState.IN_GAME);
//				 TODO fetch player data from database and attach Player object to connection.
//				 TODO Remove random generated ID at player
				Player player = new Player(666, CharacterTemplateTable.getInstance().getTemplate("player"));
				player.setCharId(rset.getInt("char_id"));
				player.setPosition(rset.getInt("last_x"), rset.getInt("last_y"));
				player.setClient(getClient());
				
				getClient().setPlayer(player);
				
				getClient().sendPacket(
						new LoginSuccessful(
								player.getObjectId(),
								(int) player.getPosition().getX(),
								(int) player.getPosition().getY(),
								player.getCharId()
				));
				
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				
				System.out.println("\"" + username +  "\""  + " has joined " + 
						dateFormat.format(cal.getTime()));
				
//				playSound("beep-02.wav");
				
				World.getInstance().addPlayer(getClient().getPlayer());
				
			} else {
				// not valid credentials/user doesn't exist
				getClient().sendPacket(new LoginFailure("Please check your username and/or password"));
			}
			
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static synchronized void playSound(final String url) {
		final File file = new File("./resources/" + url);
		  new Thread(new Runnable() {
		  // The wrapper thread is unnecessary, unless it blocks on the
		  // Clip finishing; see comments.
		    public void run() {
		      try {
		        Clip clip = AudioSystem.getClip();
		        AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
		        clip.open(inputStream);
		        clip.start(); 
		      } catch (Exception e) {
		        e.printStackTrace();
		      }
		    }
		  }).start();
		}

}
