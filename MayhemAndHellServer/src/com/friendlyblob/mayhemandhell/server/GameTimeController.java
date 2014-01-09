package com.friendlyblob.mayhemandhell.server;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.MobInstance;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;

import javolution.util.FastMap;

public class GameTimeController extends Thread{
	
	public static final int TICKS_PER_SECOND = 10;
	public static final int MILLIS_IN_TICK = 1000 / TICKS_PER_SECOND;
	public static final float DELTA_TIME = MILLIS_IN_TICK/1000.0f;
	
	private static GameTimeController instance;

	// Collection of all characters that are moving this instance
	private final FastMap<Integer, GameCharacter> movingObjects = new FastMap<Integer, GameCharacter>().shared();
	
	// Collection of all characters that are about to respawn 
	private final FastMap<Integer, MobInstance> respawningMobs = new FastMap<Integer, MobInstance>().shared();
	
	private final long referenceTime;
	
	/**
	 * Initializing by creating an instance for GameTimeController
	 */
	public static void initialize() {
		instance = new GameTimeController();
	}
	
	public GameTimeController() {
		super("GameTimeController");
		referenceTime = System.currentTimeMillis();
		
		super.start();
	}
	
	/**
	 * Returns a calculated number of ticks passed since the server start. This
	 * tick represents a current tick of the game.
	 */
	public final int getGameTicks() {
		return (int) ((System.currentTimeMillis() - referenceTime) / MILLIS_IN_TICK);
	}
	
	/**
	 * Updates position of characters that are currently moving.
	 */
	public final void moveCharacters() {
		GameCharacter character;
		for (FastMap.Entry<Integer, GameCharacter> e = movingObjects.head(), 
				tail = movingObjects.tail(); (e = e.getNext()) != tail;) {
			character = e.getValue();
			
			if (character.isDead()) {
				stopMoving(character);
				continue;
			}
			
			// Update character region if necessary
			character.getZone().updateRegion(character);
			
			if (character.updatePosition(getGameTicks())) {
				stopMoving(character);
				character.onDestinationReached();
			}
		}
	}
	
	/**
	 * Registers mob that is dead to be respawned
	 * @param mob
	 */
	public final void registerRespawningMob(MobInstance mob) {
		if (mob == null) {
			return;
		}
		
		respawningMobs.putIfAbsent(mob.getObjectId(), mob);
	}
	
	public final void stopMoving(GameCharacter character) {
		movingObjects.remove(character.getObjectId());
	}
	
	/**
	 * Registers characters that are moving 
	 */
	public final void registerMovingObject(GameCharacter object) {
		if (object == null) {
			return;
		}
		
		movingObjects.putIfAbsent(object.getObjectId(), object);
	}
	
	public void respawnMobs() {
		MobInstance mob;
		for (FastMap.Entry<Integer, MobInstance> e = respawningMobs.head(), 
				tail = respawningMobs.tail(); (e = e.getNext()) != tail;) {
			mob = e.getValue();
			
			if (mob.updateRespawn(getGameTicks())){
				respawningMobs.remove(e.getKey());
			}
		}
	}
	
	public final void run() {
		long nextTickTime, sleepTime;
		
		while (true) {
			nextTickTime = ((System.currentTimeMillis() / MILLIS_IN_TICK) * MILLIS_IN_TICK) + MILLIS_IN_TICK;
			
			// Heavy stuff goes here
			moveCharacters();
			respawnMobs();
			
			sleepTime = nextTickTime - System.currentTimeMillis();
			
			// Sleeping until next tick
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (final InterruptedException e) {
					
				}
			}
		}
	}
	
	public final void stopTimer() {
		super.interrupt();
	}
	
	public static GameTimeController getInstance() {
		return instance;
	}
	
}
