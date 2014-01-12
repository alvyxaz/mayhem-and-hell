package com.friendlyblob.mayhemandhell.server.simple_runnables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import com.friendlyblob.mayhemandhell.server.DatabaseFactory;
import com.friendlyblob.mayhemandhell.server.model.World;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.network.packets.server.TopicChanged;

public class TopicTv implements Runnable {
	
	private final int TOPIC_DURATION = 80 * 1000; 
	
	@Override
	public void run() {
		ConcurrentHashMap<Integer, GameCharacter> players = World.getInstance().getPlayers();
		long lastTick = System.currentTimeMillis();
		long currentTick;
		
		while (true) {
			currentTick = System.currentTimeMillis();
			
			if (currentTick - lastTick > TOPIC_DURATION) {
				try {
					Connection con = DatabaseFactory.getInstance().getConnection();
					PreparedStatement ps = con.prepareStatement("SELECT * FROM topics ORDER BY RAND() LIMIT 0,1");
					ResultSet rset = ps.executeQuery();
					
					if (rset.next()) {
						TopicChanged topicChangedPacket = new TopicChanged(rset.getString("topic"));
												
						for (GameCharacter player : players.values()) {
							player.sendPacket(topicChangedPacket);
						}
					}
					
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				lastTick = currentTick;
			}
		}
	}

}
