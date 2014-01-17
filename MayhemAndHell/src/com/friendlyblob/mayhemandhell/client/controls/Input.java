package com.friendlyblob.mayhemandhell.client.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.MyGame;

public class Input {

	public static Touch[] touch;
	
	public static float xRatio;
	public static float yRatio;
	
	private static KeyboardKey [] keys;
	
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
		
		for(int i = 0; i < keys.length; i++){
			keys[i].lastPressed = keys[i].pressed;
			keys[i].pressed = Gdx.input.isKeyPressed(keys[i].key);
		}
		
		// Updating accelerometer value
		if(MyGame.isAndroid){
			accelerometer = Gdx.input.getAccelerometerX();
		} else {
			accelerometer = (MyGame.SCREEN_HALF_WIDTH - (Gdx.input.getX() * xRatio))
					/MyGame.SCREEN_HALF_WIDTH * acceleratorMaxValue;
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
		for(int i = 0; i < keys.length; i++){
			if(keys[i].key == key)
				return keys[i].lastPressed && ! keys[i].pressed;
		}
		return false;
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
		touch = new Touch[2];
		Gdx.input.setCatchBackKey(true);
		
		xRatio = MyGame.SCREEN_WIDTH / (float)Gdx.graphics.getWidth();
		yRatio = MyGame.SCREEN_HEIGHT / (float)Gdx.graphics.getHeight();
		for(int i= 0; i < touch.length; i++)
			touch[i] = new Touch();
		
		keys = new KeyboardKey[8];
		keys[0] = new KeyboardKey(Keys.F1);
		keys[1] = new KeyboardKey(Keys.PLUS);
		keys[2] = new KeyboardKey(Keys.MINUS);
		keys[3] = new KeyboardKey(Keys.G);
		keys[4] = new KeyboardKey(Keys.C);
		keys[5] = new KeyboardKey(Keys.F2);
		keys[6] = new KeyboardKey(Keys.ENTER);
		keys[7] = new KeyboardKey(Keys.BACKSPACE);
	}
	
	

}
