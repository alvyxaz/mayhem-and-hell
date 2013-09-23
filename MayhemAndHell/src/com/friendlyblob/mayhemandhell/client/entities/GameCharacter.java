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

	private int targetX;
	private int targetY;
	
	private float easeX;
	private float easeY;
	private float easeTime;
	
	private int movementSpeed = 0; // Pixels per second
	
	// States
	private int state = 0;
	private final int IDLE = 0;
	private final int MOVING = 1;
	
	public GameCharacter(int id, int x, int y){
		super(id);
		position.set(x, y);
		hitBox = new Rectangle(x, y, 15, 28);
	}
	
	public void update(float deltaTime) {
		switch (state) {
		case IDLE :
			break;
		case MOVING:
			// Checking whether object has arrived
			if (Math.abs(targetX - position.x) < movementSpeed * deltaTime && Math.abs(targetY - position.y) < movementSpeed * deltaTime) {
				state = IDLE;
				setPosition(targetX, targetY);
				return;
			}
						
			float angle = (float)Math.atan2(targetY - position.y, targetX - position.x);
			float movementX = (float) Math.cos(angle) * movementSpeed * deltaTime;
			float movementY = (float) Math.sin(angle) * movementSpeed * deltaTime;
			
			if (easeTime > 0) {
				movementX += easeX * deltaTime;
				movementY += easeY * deltaTime;
				easeTime -= deltaTime;
			}
			
			moveBy(movementX, movementY);
					
			
			break;
		}
	}
	
	public void moveBy(float x, float y) {
		position.x += x;
		position.y += y;
		hitBox.x = position.x - hitBox.width/2;
		hitBox.y = position.y;
	}
	
	public void setPosition(int x, int y) {
		this.position.x = x;
		this.position.y = y;
		hitBox.x = x - hitBox.width/2;
		hitBox.y = y;
	}
	
	public void draw(SpriteBatch spriteBatch){
		spriteBatch.setColor(1f, 1f, 1f, 0.5f);
		spriteBatch.draw(Assets.px, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
		spriteBatch.setColor(Color.WHITE);
	}
	
	/**
	 * Interpolates player position by give values
	 * @param xOffset
	 * @param yOffset
	 */
	public void easeByOffset(float xOffset, float yOffset, float time) {
		this.easeTime = time;
		this.easeX = xOffset / easeTime;
		this.easeY = yOffset / easeTime;
	}
	
	public void moveTo (int x, int y) {
		this.targetX = x;
		this.targetY = y;
		state = MOVING;
	}
	
	public void moveTo (int x, int y, int speed) {
		this.movementSpeed = speed;
		moveTo(x, y);
	}
}
