package com.friendlyblob.mayhemandhell.server.model;

import java.lang.reflect.Constructor;

import com.friendlyblob.mayhemandhell.server.factories.IdFactory;
import com.friendlyblob.mayhemandhell.server.model.actors.NpcTemplate;
import com.friendlyblob.mayhemandhell.server.model.actors.instances.NpcInstance;

/**
 * Represents an area in which certain npc's respawn.
 * @author Alvys
 *
 */
public class Spawn {

	private int x;
	private int y;
	private int distance;
	
	private int currentNumber;
	private int maximumNumber;
	
	private int zoneId;
	
	private NpcTemplate template;
	private Constructor<?> constructor;
	
	public Spawn(NpcTemplate template) throws ClassNotFoundException, NoSuchMethodException, SecurityException {
		this.template = template;
		currentNumber = 0;
		
		// Creating a generic constructor
		Class<?>[] parameters = {
				int.class,
				template.getClass()
			};
		
		constructor =  Class.forName("com.friendlyblob.mayhemandhell.server.model.actors.instances." 
				+ template.getType() + "Instance").getConstructor(parameters);
	}
	
	/**
	 * Respawn a number of 
	 */
	public void initialize() {
		while (currentNumber < maximumNumber) {
			spawnNew();
		}
	}
	
	public NpcInstance spawnNew() {
		NpcInstance npc = null;
		
		try {
			Object[] parameters = {IdFactory.getInstance().getNextId(), template};
			npc = (NpcInstance) constructor.newInstance(parameters);
			return initializeNpcInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return npc;
	}
	
	public NpcInstance initializeNpcInstance() {
		
		return null;
	}
	
	/**
	 * A task that does all of the spawning and respawning work.
	 * @author Alvys
	 *
	 */
	public static class SpawnTask implements Runnable {

		@Override
		public void run() {
			
		}
	}
	
	
	
}
