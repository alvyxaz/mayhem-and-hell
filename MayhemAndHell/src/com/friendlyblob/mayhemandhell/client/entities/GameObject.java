package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

/**
 * Represents an entity in the game world. 
 * @author Alvys
 *
 */
public abstract class GameObject {
	
	public int objectId;
	
	public Vector2 position;
	
	public Rectangle hitBox;
	
	public String name;
	
	public GameObjectType type;
	
	public static enum GameObjectType {
		OTHER(0),
		PLAYER(1),
		FRIENDLY_NPC(2),	// Non mobile object (Most likely a character you can "Talk" to)
		HOSTILE_NPC(3),
		ITEM(4),
		RESOURCE(5); // Mining, crafting and other resources
		
		public int value;
		
		GameObjectType(int value) {
			this.value = value;
		}
		
		public static GameObjectType fromValue(int value) {
			for (GameObjectType type : GameObjectType.values()) {
				if (value == type.value) {
					return type;
				}
			}
			return null;
		}
		
		// Checks whether entity should have health points
		public static boolean hasHealth(GameObjectType type) {
			return type == GameObjectType.HOSTILE_NPC || type == GameObjectType.PLAYER || type == GameObjectType.FRIENDLY_NPC;
		}
	}
	
	public GameObject(int objectId) {
		this.objectId = objectId;
		this.name = "Unknown";
		this.position = new Vector2();
	}
	
	public void setHitBox(Rectangle rectangle) {
		hitBox = rectangle;
	}
	
	public abstract void draw(SpriteBatch sb);
	public abstract void update(float deltaTime);
	
	public void setType(GameObjectType type) {
		this.type = type;
	}
	
	public void setType(int type) {
		setType(GameObjectType.fromValue(type));
	}
	
	public void setPosition (int x, int y) {
		position.set(x, y);
		this.hitBox.x = x;
		this.hitBox.y = y;
	}
}
