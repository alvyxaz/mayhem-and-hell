package com.friendlyblob.mayhemandhell.server.model.datatables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;

import javolution.util.FastMap;

import com.friendlyblob.mayhemandhell.server.DatabaseFactory;
import com.friendlyblob.mayhemandhell.server.model.Spawn;
import com.friendlyblob.mayhemandhell.server.model.actors.NpcTemplate;

public class SpawnTable {

	private static SpawnTable instance;
	
	private static final String SELECT_SPAWNS = "SELECT * FROM spawns";
	
	// TODO make sure we need it to be shared.
	private static final Map<Integer, Spawn> spawnTable = new FastMap<Integer, Spawn>().shared();
	
	public static void initialize() {
		instance = new SpawnTable();
	}
	
	public SpawnTable() {
		load();
	}
	
	public void load() {
		try {
			Connection connection = DatabaseFactory.getInstance().getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(SELECT_SPAWNS);
			
			Spawn spawn;
			NpcTemplate npc;
			int npcId;
			
			while(resultSet.next()) {
				
				npcId = resultSet.getInt("npc_id");
				npc = NpcTable.getInstance().getTemplate(npcId);
				
				if (npc == null) {
					System.out.println("[SpawnTable] Error loading npc template with id " + npcId);
					continue;
				}
				
				spawn = new Spawn(npc);
				spawn.setArea(
						resultSet.getInt("start_x"), 
						resultSet.getInt("start_y"), 
						resultSet.getInt("end_x"), 
						resultSet.getInt("end_y"));
				spawn.setRespawnTime(resultSet.getInt("respawn_time"));
				spawn.setZoneId(resultSet.getInt("zone_id"));
				spawn.setMaximumCount(resultSet.getInt("npc_count"));
				spawn.initialize();
			}
			
			connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
