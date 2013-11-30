package com.friendlyblob.mayhemandhell.server.model.resources;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.items.Item;
import com.friendlyblob.mayhemandhell.server.model.skills.Castable;

public class Resource extends GameObject {
	private ResourceTemplate template;
	private int resourceId; // ID from database (resource specific instance id)
	
	public Resource(int objectId, int resourceId, ResourceTemplate template) {
		super(objectId);
		this.setType(GameObjectType.RESOURCE);
		this.template = template;
		this.resourceId = resourceId;
		this.setName(template.getName());
	}
	
	public int getResourceId() {
		return resourceId;
	}
	
	public ResourceTemplate getTemplate() {
		return template;
	}
	public void setTemplate(ResourceTemplate template) {
		this.template = template;
	}
	
	/**
	 * Represents a resource gathering skill that has to be casted before obtaining that resource
	 * @author Alvys
	 *
	 */
	public static class ResourceSkill implements Castable {

		private String name;
		private int castingTime;
		
		public ResourceSkill(String name, int castingTime) {
			this.name = name;
			this.castingTime = castingTime;
		}
		
		@Override
		public int getCastingTime() {
			return castingTime;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void execute(GameCharacter caster, GameObject target) {
		}

		@Override
		public int getRange() {
			return 10;
		}
		
	}
	
}
