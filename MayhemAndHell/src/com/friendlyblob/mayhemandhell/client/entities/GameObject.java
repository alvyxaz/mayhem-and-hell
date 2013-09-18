package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

/**
 * Represents an entity in the game world. 
 * @author Alvys
 *
 */
public abstract class GameObject {
	
	public int objectId;
	
	public Rectangle hitBox;
	
	public String name;
	
	public GameObject(int objectId) {
		this.objectId = objectId;
		this.name = "Unknown";
	}
	
	public void setHitBox(Rectangle rectangle) {
		hitBox = rectangle;
	}
	
	public abstract void draw(SpriteBatch sb);
}
