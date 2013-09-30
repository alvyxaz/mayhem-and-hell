package com.friendlyblob.mayhemandhell.server.model;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.network.ThreadPoolManager;

public class World {

	private ConcurrentHashMap<Integer, GameCharacter> allPlayers;
	private ConcurrentHashMap<Integer, GameObject> allObjects;

	private TIntObjectHashMap<Zone> allZones;
	
	public World() {
		allPlayers = new ConcurrentHashMap<Integer, GameCharacter>() ;
		allObjects = new ConcurrentHashMap<Integer, GameObject>() ;
		
		// TODO load all zones from somewhere
		allZones = new TIntObjectHashMap<Zone>();
		allZones.put(0 ,new Zone());
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
}
