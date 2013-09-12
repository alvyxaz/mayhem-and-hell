package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.MyGame;
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
	
	public GameCharacter(int id, int x, int y){
		super(x, y);
		this.objectId = id;
		hitBox = new Rectangle(x, y, 32, 64);
	}
	
	public void update(float deltaTime) {
		switch (state) {
		case IDLE :
			break;
		case MOVING:
			// Checking whether object has arrived
			if (Math.abs(targetX - hitBox.x) < movementSpeed * deltaTime && Math.abs(targetY - hitBox.y) < movementSpeed * deltaTime) {
				state = IDLE;
				return;
			}
						
			float angle = (float)Math.atan2(targetY - hitBox.y, targetX - hitBox.x);
			
			// Moving 
			hitBox.x += Math.cos(angle) * movementSpeed * deltaTime;
			hitBox.y += Math.sin(angle) * movementSpeed * deltaTime;
			
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
		this.targetY = y;
		state = MOVING;
	}
	
	public void moveTo (int x, int y, int speed) {
		this.movementSpeed = speed;
		moveTo(x, y);
	}
}
