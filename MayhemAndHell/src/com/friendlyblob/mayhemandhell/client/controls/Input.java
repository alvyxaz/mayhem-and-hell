package com.friendlyblob.mayhemandhell.client.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.MyGame;

public class Input {

	public static Touch[] touch;
	
	public static float xRatio;
	public static float yRatio;
	
	private static int[] trackedKeys;
	private static KeyState[] keys;
	
	private static Direction direction;
	private static Direction prevDirection;
	
	public static int getX(){
		return touch[0].x;
	}
	
	public static int getY() {
		return touch[0].y;
	}
	
	private static float acceleratorMaxValue = 5;
	public static float accelerometer = 0;
	
	public static void update() {
		for(int i= 0; i < touch.length; i++){
			touch[i].lastTouched = touch[i].touched;
			
			if(Gdx.input.isTouched(i)){
				touch[i].touched = true;
				touch[i].x = (int)(Gdx.input.getX(i) * xRatio);
				touch[i].y = MyGame.SCREEN_HEIGHT - (int)(Gdx.input.getY(i) * yRatio);
				
				// Calculating highest movement speed
				touch[i].dx = Gdx.input.getDeltaX(i)*xRatio;
				touch[i].dy = -Gdx.input.getDeltaY(i)*yRatio;
				
			} else {
				touch[i].touched = false;
			}
		}
		
		for(int i = 0; i < trackedKeys.length; i++){
			keys[trackedKeys[i]].lastPressed = keys[trackedKeys[i]].pressed;
			keys[trackedKeys[i]].pressed = Gdx.input.isKeyPressed(trackedKeys[i]);
		}
		
		// Updating accelerometer value
		if(MyGame.isAndroid){
			accelerometer = Gdx.input.getAccelerometerX();
		} else {
			accelerometer = (MyGame.SCREEN_HALF_WIDTH - (Gdx.input.getX() * xRatio))
					/MyGame.SCREEN_HALF_WIDTH * acceleratorMaxValue;
		}
		
		updateDirectionInput(5);
	}
	
	public static boolean isDirectionChanged() {
		return prevDirection != direction;
	}
	
	public static Direction getDirection() {
		return direction;
	}
	
	private static void updateDirectionInput(int key) {
		prevDirection = direction;
		boolean cought = false;
		
		if (keys[Keys.W].pressed) {
			if (keys[Keys.D].pressed) {
				direction = Direction.UP_RIGHT;
				return;
			} else {
				direction = Direction.UP;
			}
			cought = true;
		}
		
		if (keys[Keys.D].pressed) {
			if (keys[Keys.S].pressed) {
				direction = Direction.DOWN_RIGHT;
				return;
			} else {
				direction = Direction.RIGHT;
			}
			cought = true;
		}
		
		if (keys[Keys.S].pressed) {
			if (keys[Keys.A].pressed) {
				direction = Direction.DOWN_LEFT;
				return;
			} else {
				direction = Direction.DOWN;
			}
			cought = true;
		}
		
		if (keys[Keys.A].pressed) {
			if (keys[Keys.W].pressed) {
				direction = Direction.UP_LEFT;
				return;
			} else {
				direction = Direction.LEFT;
			}
			cought = true;
		}
		
		if (!cought) {
			direction = Direction.NONE;
		}
	}
	
	/*
	 * Check whether a rectangle is being touched at the moment
	 */
	public static boolean isTouching(Rectangle rect){
		for(int i = 0; i < 2; i++){
			if(!touch[i].touched) continue;
			if(rect.x <= touch[i].x && rect.x+rect.width >= touch[i].x){	
				if(rect.y <= touch[i].y && rect.y+rect.height >= touch[i].y)
					return true;
			}
		}
		return false;
	}
	
	public static boolean isReleasing(){
		for(int i = 0; i < 2; i++){
			if(touch[i].touched) continue;
			if(!touch[i].lastTouched) continue;
			return true;
		}
		return false;
	}
	
	/**
	 * Check whether a rectangle has been released after a touck.
	 * 
	 * TODO: Seems like something's not working
	 * 
	 * @param rect Rectangle of interest
	 */
	public static boolean isReleasing(Rectangle rect){
		for(int i = 0; i < 2; i++){
			if(touch[i].touched) continue;
			if(!touch[i].lastTouched) continue;
			if(rect.x <= touch[i].x && rect.x+rect.width >= touch[i].x){	
				if(rect.y <= touch[i].y && rect.y+rect.height >= touch[i].y)
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Check whether a certain keyboard key was released. Have in mind
	 * that in order to track a key, it has to be registered in keys array,
	 * which happens in initialize() method.
	 * 
	 * @param rect Rectangle of interest
	 */
	public static boolean keyReleased(int key){
		return keys[key].lastPressed && ! keys[key].pressed;
	}
	
	/**
	 * In case there are multiple touches in the screen, return an index 
	 * of touch that is touching a given rectangle
	 * 
	 * @param rect Rectangle of interest
	 * @return Index of touch, -1 if not touching.
	 */
	public static int getTouchIndex(Rectangle rect){
		for(int i = 0; i < 2; i++){
			if(!Gdx.input.isTouched(i)) continue;
			if(rect.x <= touch[i].x && rect.x+rect.width >= touch[i].x){	
				if(rect.y <= MyGame.SCREEN_HEIGHT-touch[i].y && rect.y+rect.height >= MyGame.SCREEN_HEIGHT-touch[i].y)
					return i;
			}
		}
		return -1;
	}
	
	// TODO cleanup tracked keys before publishing
	public static void initialize(){
		direction = Direction.NONE;
		prevDirection = Direction.NONE;
		touch = new Touch[2];
		Gdx.input.setCatchBackKey(true);
		
		xRatio = MyGame.SCREEN_WIDTH / (float)Gdx.graphics.getWidth();
		yRatio = MyGame.SCREEN_HEIGHT / (float)Gdx.graphics.getHeight();
		for(int i= 0; i < touch.length; i++)
			touch[i] = new Touch();
		
		trackedKeys = new int[4];
		trackedKeys[0] = Keys.W;
		trackedKeys[1] = Keys.A;
		trackedKeys[2] = Keys.S;
		trackedKeys[3] = Keys.D;
		
		keys = new KeyState[Keys.F12+1];
		
		for(int i = 0; i < trackedKeys.length; i++) {
			keys[trackedKeys[i]] = new KeyState();
		}
	}
	
	public static enum Direction {
		NONE(0),
		UP((float)Math.PI/2),
		UP_RIGHT((float)Math.PI/4),
		RIGHT(0),
		DOWN_RIGHT(-(float)Math.PI/4),
		DOWN(-(float)Math.PI/2),
		DOWN_LEFT((-(float)Math.PI/4)*3),
		LEFT(-(float)Math.PI),
		UP_LEFT(((float)Math.PI/4)*3);
		
		Direction(float angle) {
			this.angle = angle;
		}
		
		public float getAngle() {
			return angle;
		}
		
		private float angle;
	}

	public static class KeyState {
		public boolean pressed;
		public boolean lastPressed;
	}
	
}
