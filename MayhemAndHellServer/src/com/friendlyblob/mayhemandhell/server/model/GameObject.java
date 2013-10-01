package com.friendlyblob.mayhemandhell.server.model;

import java.util.List;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.friendlyblob.mayhemandhell.server.factories.IdFactory;
import com.friendlyblob.mayhemandhell.server.utils.ObjectPosition;

/**
 * Represents an instance of the object (Player, mob, item in the world and etc.)
 * @author Alvys
 *
 */
public class GameObject {
	
	public static enum GameObjectType {
		OTHER,
		PLAYER,
		MOB,	// Mobile object (Monster, animal, etc.)
		NPC,	// Non mobile object (Most likely a character you can "Talk" to)
		ITEM,
		RESOURCE // Mining, crafting and other resources
	}
	
	protected int objectId;
	private ObjectPosition position;
	
	private Region region;
	private Zone zone;
	
	private GameObject target;
	
	private List<GameObject> targetedBy = new FastList<GameObject>().shared();
	
	private GameObjectType type;
	
	private String name;
	
	public void setType(GameObjectType type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public GameObjectType getType() {
		return type;
	}
	
	public GameObject getTarget() {
		return target;
	}
	
	/**
	 * Removes this object from zone, releases and grabs a new objectId
	 * from IdFactory.
	 */
	public void refreshId() {
		this.zone.removeObject(this);
		IdFactory.getInstance().releaseId(this.objectId);
		this.objectId = IdFactory.getInstance().getNextId();
	}
	
	/**
	 * Sets objects target to a given object 
	 * (Player targeting Player, Player targeting object ant etc.)
	 * @param object Object to set target to
	 * @return true if target set successfully, false if not.
	 */
	public boolean setTarget(GameObject object) {
		if (object == null) {
			return false;
		}
		this.target = object;
		object.addTargetedBy(this);
		return true;
	}
	
	/**
	 * Removes a target from this character, and removes
	 * targetedBy of it's target
	 */
	public void removeTarget() {
		this.target.removeTargetedBy(this);
		this.target = null;
	}
	
	/**
	 * Adds a game object that is targeting current object
	 * @param object GameObject that targets current object
	 */
	private void addTargetedBy(GameObject object) {
		targetedBy.add(object);
	}
	
	/**
	 * Removes a game object that is no longer targeting current object
	 * @param object
	 */
	public void removeTargetedBy(GameObject object) {
		targetedBy.remove(object);
	}
	
	public void setZone(Zone zone) {
		this.zone = zone;
	}
	
	public void clearTargetedBy() {
		for (GameObject object : targetedBy) {
			object.removeTarget();
		}
		targetedBy.clear();
	}
	
	public Zone getZone() {
		return zone;
	}
	
	public void setRegion(Region region) {
		this.region = region;
	}
	
	public Region getRegion() {
		return region;
	}
	
	public GameObject() {
		name = "Unknown";
		position = new ObjectPosition();
	}
	
	public ObjectPosition getPosition() {
		return position;
	}
	
	public void setPosition(int x, int y) {
		position.set(x, y);
	}
	
	public int getObjectId() {
		return objectId;
	}
	
	public void setObjectId(int id) {
		this.objectId = id;
	}
	
}
