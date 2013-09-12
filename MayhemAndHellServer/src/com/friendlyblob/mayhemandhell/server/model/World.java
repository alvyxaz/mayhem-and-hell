package com.friendlyblob.mayhemandhell.server.model;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.network.ThreadPoolManager;

public class World {

	private ConcurrentHashMap<Integer, GameCharacter> allPlayers;
	private ConcurrentHashMap<Integer, GameObject> allObjects;

	private ArrayList<Zone> allZones;
	
	public World() {
		allPlayers = new ConcurrentHashMap<Integer, GameCharacter>() ;
		allObjects = new ConcurrentHashMap<Integer, GameObject>() ;
		
		// TODO load all zones from somewhere
		allZones = new ArrayList<Zone>();
		allZones.add(new Zone());
		
		// Start thread to synchronize players
		nearbyCharactersBroadcastTask();
	}
	
	public void addPlayer(Player player) {
		allPlayers.put(player.getObjectId(), player);
		allObjects.put(player.getObjectId(), player);
		
		// TODO add player to it's zone, not a random zone
		allZones.get(0).addPlayer(player);
	}
	
	public GameObject getObject(int id) {
		return allObjects.get(id);
	}
	
	public static final class SingletonHolder {
		public final static World INSTANCE = new World();
	}
	
	
	public static World getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	/**
	 * Creates a thread that sends data about nearby
	 * characters to players in nearby regions.
	 */
	public void nearbyCharactersBroadcastTask () {
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Runnable() {

			@Override
			public void run() {
				for(Zone zone : allZones) {
					// TODO remove or do something with it;
				}
			}
			
		}, 0, 5000); // Send all player data every five seconds.
	}
}
