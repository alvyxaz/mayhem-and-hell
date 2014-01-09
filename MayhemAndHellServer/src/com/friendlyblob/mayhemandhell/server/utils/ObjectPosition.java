package com.friendlyblob.mayhemandhell.server.utils;

/**
 * Represent object's current position
 */
public class ObjectPosition {
	private float x;
	private float y;
	
	public ObjectPosition() {
		this.x = 0;
		this.y = 0;
	}
	
	public ObjectPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public float angleTo(int x, int y) {
		return (float)Math.atan2(y - this.y, x - this.x);
	}
	
	public float angleTo(ObjectPosition target) {
		return (float)Math.atan2(target.y - y, target.x - x);
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
	
	public void setY(float y) {
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
	
	public void offsetByAngle(float angle, float offset) {
		offset((float) Math.cos(angle) * offset, 
				(float) Math.sin(angle) * offset);
	}
	
	@Override
	public String toString() {
		return "[Position: " + x + " " + y + "]";
	}
}
