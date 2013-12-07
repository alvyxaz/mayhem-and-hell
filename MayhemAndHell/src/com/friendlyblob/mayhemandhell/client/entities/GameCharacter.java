package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
	
	float animationCycle = 0;
	float timePerFrame = 0.1f;
	int frameCount = 4;
	protected int currentFrame;
	
	protected CharacterAnimation animationHandler;
	private boolean twoDirectionalAnimations;
	
	private MovementDirection currentDirection; 
	
	private TextureRegion hint;
	private int hintXOffset;
	private int hintYOffset;
	
	public GameCharacter(int id, int x, int y, int animationId){
		super(id);
		position.set(x, y);
		hitBox = new Rectangle(x-7.5f, y, 15, 28);
		
		animationHandler = new CharacterAnimation();
		
		prepareAnimations(animationId);
		twoDirectionalAnimations = animationHandler.isTwoDirectional();
	}
	
	public void prepareAnimations(int animationId) {
		for (AnimationData animation : AnimationParser.getCollection(animationId).values()) {
			animationHandler.setAnimation(animation.type, 
					new Animation(animation, animationHandler));
		}
	}
	
	public void setHint(TextureRegion hint) {
		this.hint = hint;
		if (hint != null) {
			hintXOffset = (int)hitBox.width/2 - (int)hint.getRegionWidth()/2;
			hintYOffset = (int)hitBox.height + 1;
		}
	}
	
	public void setHint(int hint) {
		switch(hint) {
			case 0: // NONE
				setHint(null);
				break;
			case 1: // QUEST_GIVE
				setHint(Assets.getTextureRegion("gui/ingame/icon_char_quest"));
				break;
			case 2: // QUEST_RETURN
				setHint(Assets.getTextureRegion("gui/ingame/icon_char_quest_in"));
				break;
			case 3: // QUEST_IN_PROGRESS
				setHint(Assets.getTextureRegion("gui/ingame/icon_char_quest_in_gray"));
				break;
			case 4: // QUEST_ATTACK
				setHint(Assets.getTextureRegion("gui/ingame/icon_char_attack"));
				break;
			default: 
				setHint(null);
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
		switch(currentDirection) {
			case UP: // UP
				animationHandler.play(CharacterAnimationType.IDLE_UP);
				break;
			case RIGHT: // RIGHT
				animationHandler.play(CharacterAnimationType.IDLE_RIGHT);
				break;
			case DOWN: // DOWN
				animationHandler.play(CharacterAnimationType.IDLE_DOWN);
				break;
			case LEFT: // LEFT
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
		spriteBatch.setColor(1f, 1f, 1f, 0.1f);
		spriteBatch.draw(Assets.px, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
//		Assets.defaultFont.draw(spriteBatch, objectId + "", hitBox.x + 10, hitBox.y - 10);
		spriteBatch.setColor(Color.WHITE);
		
		spriteBatch.draw(animationHandler.getFrame(Gdx.graphics.getDeltaTime()), position.x - animationHandler.getFrameWidth()/2, position.y);
	
		if (hint != null) {
			spriteBatch.draw(hint, hitBox.x + hintXOffset, hitBox.y + hintYOffset);
		}
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

		currentDirection = angleToDirection(angle);
		animationHandler.play(directionToWalking(currentDirection));
	}
	
	/**
	 * Given a direction, returns a movement animation
	 * @param direction
	 * @return
	 */
	public CharacterAnimationType directionToWalking(MovementDirection direction) {
		switch(direction) {
			case DOWN:
				return CharacterAnimationType.WALKING_DOWN;
			case LEFT:
				return CharacterAnimationType.WALKING_LEFT;
			case RIGHT:
				return CharacterAnimationType.WALKING_RIGHT;
			case UP:
				return CharacterAnimationType.WALKING_UP;
		}
		
		return CharacterAnimationType.WALKING_DOWN;
	}
	
	/**
	 * Given a direction, returns attack animation to that direction
	 * @param direction
	 * @return
	 */
	public CharacterAnimationType directionToAttackAnimation(MovementDirection direction) {
		switch(direction) {
			case DOWN:
				return CharacterAnimationType.ATTACK_DOWN;
			case LEFT:
				return CharacterAnimationType.ATTACK_LEFT;
			case RIGHT:
				return CharacterAnimationType.ATTACK_RIGHT;
			case UP:
				return CharacterAnimationType.ATTACK_UP;
		}
		
		return CharacterAnimationType.IDLE_DOWN;
	}
	
	public CharacterAnimationType directionToIdle(MovementDirection direction) {
		switch(direction) {
			case DOWN:
				return CharacterAnimationType.IDLE_DOWN;
			case LEFT:
				return CharacterAnimationType.IDLE_LEFT;
			case RIGHT:
				return CharacterAnimationType.IDLE_RIGHT;
			case UP:
				return CharacterAnimationType.IDLE_UP;
		}
	
		return CharacterAnimationType.IDLE_DOWN;
	}
	
	/**
	 * Given an angle, returns a direction.
	 * @param angle
	 * @return
	 */
	public MovementDirection angleToDirection(float angle) {
		MovementDirection direction = MovementDirection.DOWN;
		
		// Calculating direction of movement (for animating purposes)
		if (angle >= -Math.PI/2 && angle <= Math.PI/2) {
			// Moving right
			direction = MovementDirection.RIGHT;
			
			if (!twoDirectionalAnimations && angle >= Math.PI/4) {
				// Moving up
				direction = MovementDirection.UP;
			} else if (!twoDirectionalAnimations && angle <= -Math.PI/4) {
				// Moving down
				direction = MovementDirection.DOWN;
			}
		} else {
			// Moving left
			direction = MovementDirection.LEFT;
			
			if (!twoDirectionalAnimations && angle <= Math.PI*3/4 && angle > Math.PI/2) {
				// Moving up
				direction = MovementDirection.UP;
			} else if (!twoDirectionalAnimations && angle >= -Math.PI*3/4 && angle < - Math.PI/2) {
				// Moving down
				direction = MovementDirection.DOWN;
			}
		}
		
		return direction;
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
	
	public void attack(float angle) {
		MovementDirection direction = angleToDirection(angle);
		currentDirection = direction;
		animationHandler.play(directionToIdle(direction));
		animationHandler.play(
				directionToAttackAnimation(direction));
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
