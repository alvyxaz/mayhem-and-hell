package com.friendlyblob.mayhemandhell.server.utils;

/**
 * Represent object's current position
 */
public class ObjectPosition {
	private float x;
	private float y;
	
	public ObjectPosition() {
		x = 0;
		y = 0;
	}
	
	public int distanceTo(ObjectPosition target) {
		int dx = (int)(target.x - this.x);
		int dy = (int)(target.y - this.y);
		return (int) Math.sqrt(dx*dx + dy*dy);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY() {
		this.y = y;
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void offset(float dX, float dY) {
		this.x += dX;
		this.y += dY;
	}
}
