package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.animations.Animation;
import com.friendlyblob.mayhemandhell.client.animations.Animation.AnimationData;
import com.friendlyblob.mayhemandhell.client.animations.AnimationParser;
import com.friendlyblob.mayhemandhell.client.animations.CharacterAnimation;
import com.friendlyblob.mayhemandhell.client.animations.CharacterAnimation.CharacterAnimationType;
import com.friendlyblob.mayhemandhell.client.gameworld.Map;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RequestMove;

public class GameCharacter extends GameObject {

	protected int targetX;
	protected int targetY;
	
	private float easeX;
	private float easeY;
	private float easeTime;
	
	private int movementSpeed = 0; // Pixels per second
	
	// States
	private int state = 0;
	private final int IDLE = 0;
	private final int MOVING = 1;
	
	protected int direction = 2;
	
	float animationCycle = 0;
	float timePerFrame = 0.1f;
	int frameCount = 4;
	protected int currentFrame;
	
	protected CharacterAnimation animationHandler;
	
	public GameCharacter(int id, int x, int y, int animationId){
		super(id);
		position.set(x, y);
		hitBox = new Rectangle(x-7.5f, y, 15, 28);
		
		animationHandler = new CharacterAnimation();
		
		prepareAnimations(animationId);
	}
	
	public void prepareAnimations(int animationId) {
		for (AnimationData animation : AnimationParser.getCollection(animationId).values()) {
			animationHandler.setAnimation(CharacterAnimationType.valueOf(animation.type), 
					new Animation(animation, animationHandler));
		}
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
				onArrived();
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
		
		// Updating frame timer
		animationCycle += deltaTime;
		if(animationCycle > timePerFrame*frameCount) {
			animationCycle = 0;
		}
		currentFrame = (int)(( animationCycle /timePerFrame)) % frameCount;
	}
	
	public void onArrived() {
		switch(direction) {
			case 0: // UP
				animationHandler.play(CharacterAnimationType.IDLE_UP);
				break;
			case 1: // RIGHT
				animationHandler.play(CharacterAnimationType.IDLE_RIGHT);
				break;
			case 2: // DOWN
				animationHandler.play(CharacterAnimationType.IDLE_DOWN);
				break;
			case 3: // LEFT
				animationHandler.play(CharacterAnimationType.IDLE_LEFT);
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
		spriteBatch.setColor(1f, 1f, 1f, 0.3f);
		spriteBatch.draw(Assets.px, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
		spriteBatch.setColor(Color.WHITE);
		
		spriteBatch.draw(animationHandler.getFrame(Gdx.graphics.getDeltaTime()), position.x - animationHandler.getFrameWidth()/2, position.y);
	}
	
	/**
	 * Interpolates player position by give values
	 * @param xOffset
	 * @param yOffset
	 */
	public void easeByOffset(float xOffset, float yOffset, float time) {
		if (Math.abs(xOffset) > 40 || Math.abs(yOffset) > 40) {
			moveBy(xOffset, yOffset);
			return;
		}
		
		this.easeTime = time;
		this.easeX = xOffset / easeTime;
		this.easeY = yOffset / easeTime;
	}
	
	public void moveTo (int x, int y) {
		this.targetX = x;
		this.targetY = y;
		state = MOVING;
		
		float angle = (float)Math.atan2(targetY - position.y, targetX - position.x);

		// Calculate direction
		if (angle >= -Math.PI/4 && angle <= Math.PI/4) {
			direction = MovementDirection.RIGHT.value;
			animationHandler.play(CharacterAnimationType.WALKING_RIGHT);
		} else if (angle >= Math.PI/4 && angle <= Math.PI*3/4) {
			direction = MovementDirection.UP.value;
			animationHandler.play(CharacterAnimationType.WALKING_UP);
		} else if (angle >= -Math.PI*3/4 && angle <= -Math.PI/4) {
			direction = MovementDirection.DOWN.value;
			animationHandler.play(CharacterAnimationType.WALKING_DOWN);
		} else {
			direction = MovementDirection.LEFT.value;
			animationHandler.play(CharacterAnimationType.WALKING_LEFT);
		}
	}
	
	public void moveTo (int x, int y, int speed) {
		this.movementSpeed = speed;
		moveTo(x, y);
	}
	
	public void teleportTo (int x, int y) {
		this.setPosition(x, y);
		this.state = IDLE;
	}
	
	public boolean isMoving() {
		return state == MOVING;
	}
	
	public static enum MovementDirection {
		UP(0),
		RIGHT(1),
		DOWN(2),
		LEFT(3);
		
		public int value;
		
		MovementDirection(int value) {
			this.value = value;
		}
	};
}
