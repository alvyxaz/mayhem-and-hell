package com.friendlyblob.mayhemandhell.server.model.resources;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.items.Item;

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
	
}
