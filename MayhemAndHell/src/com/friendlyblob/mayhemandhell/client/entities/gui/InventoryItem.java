package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.friendlyblob.mayhemandhell.client.entities.GameObject;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class InventoryItem extends SlotObject  {
	public String name;
	public String description;
	
	public InventoryItem(int size) {
		super(size);
		// TODO Auto-generated constructor stub
	}
	
	public InventoryItem setName(String name) {
		this.name = name;
		return this;
	}
	
	public InventoryItem setDescription(String description) {
		this.description = description;
		return this;
	}
}
