package com.friendlyblob.mayhemandhell.server.model;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.concurrent.ConcurrentHashMap;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.model.datatables.ZoneTable;

public class World {

	private ConcurrentHashMap<Integer, GameCharacter> allPlayers;
	private ConcurrentHashMap<Integer, GameObject> allObjects;

	private TIntObjectHashMap<Zone> allZones;
	
	public World() {
		allPlayers = new ConcurrentHashMap<Integer, GameCharacter>() ;
		allObjects = new ConcurrentHashMap<Integer, GameObject>() ;
		
		// TODO load all zones from somewhere
		allZones = new TIntObjectHashMap<Zone>();
		
		for (ZoneTemplate zone : ZoneTable.getInstance().getZoneTemplates()) {
			allZones.put(zone.getZoneId(), new Zone(zone));
		}
	}
	
	public void addPlayer(Player player) {
		allPlayers.put(player.getObjectId(), player);
		allObjects.put(player.getObjectId(), player);
		
		// TODO add player to it's zone, not a random zone
		allZones.get(0).addObject(player);
	}
	
	public void removePlayer(Player player) {
		allPlayers.remove(player.getObjectId());
		allObjects.remove(player.getObjectId());
		player.getZone().removeObject(player);
	}
	
	public void removeObject(GameObject object) {
		allObjects.remove(object);
		object.getZone().removeObject(object);
	}
	
	public void addObject(GameObject object) {
		allObjects.put(object.getObjectId(), object);
		
		allZones.get(0).addObject(object);
	}
	
	public GameObject getObject(int id) {
		return allObjects.get(id);
	}
	
	public Zone getZone(int id) {
		return allZones.get(id);
	}
	
	public ConcurrentHashMap<Integer, GameCharacter> getPlayers() {
		return allPlayers;
	}
	
	
	public static final class SingletonHolder {
		public final static World INSTANCE = new World();
	}
	
	public static World getInstance() {
		return SingletonHolder.INSTANCE;
	}
}
