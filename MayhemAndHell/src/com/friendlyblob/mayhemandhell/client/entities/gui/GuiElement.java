package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.controls.Touch;

/**
 * Represents a basic GUI element.
 * Most of the time, element has no position if it 
 * hasn't been added to GuiManager
 * @author Alvys
 *
 */
public abstract class GuiElement implements Comparable<GuiElement>{
	/**
	 * Represents a priority of GuiElement.
	 * @author Alvys
	 *
	 */
	public enum GuiPriority {
		LOW,
		MEDIUM,
		HIGH
	}
	
	public GuiManager manager;
	
	public boolean visible = true;
	public GuiPriority priority;
	
	// Basic variables, used by GuiManager to arrange object 
	public Rectangle box;
	
	public GuiElement(GuiPriority priority) {
		box = new Rectangle();
		this.priority = priority;
	}
	
	/**
	 * Get's called if touch was released on this element
	 * @param x relative x position of the touch
	 * @param y relative y position of the touch
	 */
	public abstract void onRelease(float x, float y);
	
	/**
	 * Get's called every frame for as long as this element is being touched
	 * @param x relative x position of the touch
	 * @param y relative y position of the touch
	 */
	public abstract void onTouching(float x, float y);
	
	/**
	 * Called by GuiElement constructor to establish a size of element.
	 */
	public abstract void establishSize();
	
	public abstract void draw(SpriteBatch spriteBatch);
	
	public abstract void update(float deltaTime);
	
	@Override
	public int compareTo(GuiElement other) {
		return Integer.compare(priority.ordinal(), other.priority.ordinal());
	}
	
	public void setX(float x) {
		box.x = x;
	}
	
	public void setY(float y) {
		box.y = y;
	}
	
	public void setPosition(float x, float y) {
		setX(x);
		setY(y);
	}
	
}
