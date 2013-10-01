package com.friendlyblob.mayhemandhell.server.model;

import java.lang.reflect.Constructor;

import com.friendlyblob.mayhemandhell.server.factories.IdFactory;
import com.friendlyblob.mayhemandhell.server.model.actors.NpcTemplate;
import com.friendlyblob.mayhemandhell.server.model.actors.instances.NpcInstance;
import com.friendlyblob.mayhemandhell.server.model.datatables.ZoneTable;
import com.friendlyblob.mayhemandhell.server.network.ThreadPoolManager;

/**
 * Represents an area in which certain npc's respawn.
 * @author Alvys
 *
 */
public class Spawn {

	private int startX;
	private int startY;
	private int endX;
	private int endY;
	
	// Number of npc's in spawn area
	private int currentCount;
	private int maximumCount;
	
	private int zoneId;
	
	private NpcTemplate template;
	private Constructor<?> constructor;
	
	private int respawnTime;
	
	public Spawn(NpcTemplate template) throws ClassNotFoundException, NoSuchMethodException, SecurityException {
		this.template = template;
		currentCount = 0;
		
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
		while (currentCount < maximumCount) {
			spawnNew();
		}
	}
	
	/**
	 * Creates a new npc and spawns it. 
	 * Should only be called during initialization.
	 * @return
	 */
	public NpcInstance spawnNew() {
		NpcInstance npc = null;
		
		currentCount++;
		
		try {
			Object[] parameters = {IdFactory.getInstance().getNextId(), template};
			npc = (NpcInstance) constructor.newInstance(parameters);
			return initializeNpcInstance(npc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return npc;
	}
	
	public void removeNpc(NpcInstance npc) {
		if (currentCount < 0) {
			return;
		}
		
		currentCount--;
		
		ThreadPoolManager.getInstance().scheduleGeneral(new SpawnTask(npc), 1000);
	}
	
	public void respawn(NpcInstance npc) {
		npc.refreshId();
		initializeNpcInstance(npc);
		currentCount++;
	}
	
	/**
	 * Restores npc state, prepares it to look just like a new 
	 * npc.
	 * @param npc
	 * @return
	 */
	public NpcInstance initializeNpcInstance(NpcInstance npc) {
		npc.restoreVitals();
		npc.removeEffects();
		npc.revive();
		
		if (npc.getZone() == null) {
			npc.setZone(World.getInstance().getZone(zoneId));
			npc.getZone().addObject(npc);
		}
		
		return null;
	}
	
	public void setArea(int startX, int startY, int endX, int endY) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}
	
	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public int getEndX() {
		return endX;
	}

	public void setEndX(int endX) {
		this.endX = endX;
	}

	public int getEndY() {
		return endY;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}

	public int getMaximumCount() {
		return maximumCount;
	}

	public void setMaximumCount(int maximumCount) {
		this.maximumCount = maximumCount;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public NpcTemplate getTemplate() {
		return template;
	}

	public void setTemplate(NpcTemplate template) {
		this.template = template;
	}

	public Constructor<?> getConstructor() {
		return constructor;
	}

	public void setConstructor(Constructor<?> constructor) {
		this.constructor = constructor;
	}

	public int getRespawnTime() {
		return respawnTime;
	}

	public void setRespawnTime(int respawnTime) {
		this.respawnTime = respawnTime;
	}



	/**
	 * A task that does all of the spawning and respawning work.
	 * @author Alvys
	 *
	 */
	class SpawnTask implements Runnable {

		private NpcInstance npc; // Npc to be respawned
		
		public SpawnTask(NpcInstance npc) {
			this.npc = npc;
		}
		
		@Override
		public void run() {
			respawn(npc);
		}
	}
	
	
	
}
