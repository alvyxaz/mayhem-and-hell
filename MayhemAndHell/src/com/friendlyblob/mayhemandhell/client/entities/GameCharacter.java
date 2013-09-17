package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.gameworld.Map;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RequestMove;

public class GameCharacter extends GameObject {

	public Rectangle hitBox;
	
	private int targetX;
	private int targetY;
	
	private int movementSpeed = 0; // Pixels per second
	
	// States
	private int state = 0;
	private final int IDLE = 0;
	private final int MOVING = 1;
	
	private float visibilityTimeOut;

	private TiledMapTileLayer collisionLayer;
	
	public GameCharacter(int id, int x, int y, TiledMapTileLayer collisionLayer){
		super(x, y);
		this.objectId = id;
		hitBox = new Rectangle(x, y, 15, 28);
		
		this.collisionLayer = collisionLayer;
	}
	
	public void update(float deltaTime) {
		switch (state) {
		case IDLE :
			break;
		case MOVING:
			float oldX = hitBox.x, oldY = hitBox.y;
			boolean collisionX = false, collisionY = false;
			
			// Checking whether object has arrived
			if (Math.abs(targetX - hitBox.x) < movementSpeed * deltaTime && Math.abs(targetY - hitBox.y) < movementSpeed * deltaTime) {
				state = IDLE;
				return;
			}
						
			float angle = (float)Math.atan2(targetY - hitBox.y, targetX - hitBox.x);
			
			// Moving 
			hitBox.x += Math.cos(angle) * movementSpeed * deltaTime;
			hitBox.y += Math.sin(angle) * movementSpeed * deltaTime;
			
			if (MyGame.DEBUG) {
				System.out.println("bottom left");
				System.out.println((int) (hitBox.x / Map.TILE_WIDTH));
				System.out.println((int) (hitBox.y / Map.TILE_HEIGHT));
				System.out.println("top right");
				System.out.println((int) ((hitBox.x + hitBox.width) / Map.TILE_WIDTH));
				System.out.println((int) ((hitBox.y + hitBox.height) / Map.TILE_HEIGHT));
			}

			
			if (hitBox.x - oldX > 0) {
				// top right
				collisionX =  collisionLayer.getCell((int) ((hitBox.x + hitBox.width) / Map.TILE_WIDTH), (int) ((hitBox.y + hitBox.height) / Map.TILE_HEIGHT)).getTile().getProperties().containsKey("collidable");

				// middle right
				if (!collisionX) {
					collisionX =  collisionLayer.getCell((int) ((hitBox.x + hitBox.width) / Map.TILE_WIDTH), (int) ((hitBox.y + hitBox.height/2) / Map.TILE_HEIGHT)).getTile().getProperties().containsKey("collidable");

				}
				// bottom right
				if (!collisionX) {
					collisionX =  collisionLayer.getCell((int) ((hitBox.x + hitBox.width) / Map.TILE_WIDTH), (int) (hitBox.y / Map.TILE_HEIGHT)).getTile().getProperties().containsKey("collidable");
				}
			} else {
				// top left
				collisionX =  collisionLayer.getCell((int) (hitBox.x / Map.TILE_WIDTH), (int) ((hitBox.y + hitBox.height) / Map.TILE_HEIGHT)).getTile().getProperties().containsKey("collidable");

				// middle left
				if (!collisionX) {
					collisionX =  collisionLayer.getCell((int) (hitBox.x / Map.TILE_WIDTH), (int) ((hitBox.y + hitBox.height/2) / Map.TILE_HEIGHT)).getTile().getProperties().containsKey("collidable");
				}
				// bottom left
				if (!collisionX) {
					collisionX =  collisionLayer.getCell((int) (hitBox.x / Map.TILE_WIDTH), (int) (hitBox.y / Map.TILE_HEIGHT)).getTile().getProperties().containsKey("collidable");
				}
			}
			
			if (collisionX) {
				hitBox.x = oldX;
				stop(oldX, oldY);
			}
			
			if (hitBox.y - oldY > 0) {
				// top left
				collisionY =  collisionLayer.getCell((int) (hitBox.x / Map.TILE_WIDTH), (int) ((hitBox.y  + hitBox.height)/ Map.TILE_HEIGHT)).getTile().getProperties().containsKey("collidable");
				//top middle
				if (!collisionY) {
					collisionY =  collisionLayer.getCell((int) ((hitBox.x + hitBox.width/2) / Map.TILE_WIDTH), (int) ((hitBox.y  + hitBox.height) / Map.TILE_HEIGHT)).getTile().getProperties().containsKey("collidable");
				}
				//top right
				if (!collisionY) {
					collisionY =  collisionLayer.getCell((int) ((hitBox.x + hitBox.width) / Map.TILE_WIDTH), (int) ((hitBox.y  + hitBox.height) / Map.TILE_HEIGHT)).getTile().getProperties().containsKey("collidable");
				}
			} else {
				//bottom left
				collisionY =  collisionLayer.getCell((int) (hitBox.x / Map.TILE_WIDTH), (int) (hitBox.y / Map.TILE_HEIGHT)).getTile().getProperties().containsKey("collidable");
				// bottom middle
				if (!collisionY) {
					collisionY =  collisionLayer.getCell((int) ((hitBox.x + hitBox.width/2) / Map.TILE_WIDTH), (int) (hitBox.y / Map.TILE_HEIGHT)).getTile().getProperties().containsKey("collidable");
				}
				// bottom right
				if (!collisionY) {
					collisionY =  collisionLayer.getCell((int) ((hitBox.x + hitBox.width) / Map.TILE_WIDTH), (int) (hitBox.y/ Map.TILE_HEIGHT)).getTile().getProperties().containsKey("collidable");
				}
			}
			
			if (collisionY) {
				hitBox.y = oldY;
				stop(oldX, oldY);
			}
			
			break;
		}
	}
	
	public void setPosition(int x, int y) {
		this.hitBox.x = x - (int) hitBox.width/2;
		this.hitBox.y = y;
	}
	
	public void draw(SpriteBatch spriteBatch){
		spriteBatch.setColor(1f, 1f, 1f, 0.5f);
		spriteBatch.draw(Assets.px, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
		spriteBatch.setColor(Color.WHITE);
	}
	
	public void moveTo (int x, int y) {
		this.targetX = x - (int) hitBox.width/2;
		this.targetX = x;
		this.targetY = y;
		state = MOVING;
	}
	
	public void moveTo (int x, int y, int speed) {
		this.movementSpeed = speed;
		moveTo(x, y);
	}
	
	public void stop(float oldX, float oldY) {
		targetX = (int) oldX;
		targetY = (int) oldY;
	}
}
