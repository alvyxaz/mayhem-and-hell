package com.friendlyblob.mayhemandhell.client.entities.gui;

import java.util.Arrays;
import java.util.PriorityQueue;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.friendlyblob.mayhemandhell.client.MyGame;

/**
 * Takes care of GuiElement placement on screen
 * @author Alvys
 *
 */
public class GuiManager {
	// Priority queue used to sort 
	private PriorityQueue<GuiElement> guiElementsQueue;
	
	// Caches an ordered priority queue
	private GuiElement [] guiElements;
	
	public GuiManager() {
		guiElements = new GuiElement[0];
		guiElementsQueue = new PriorityQueue<GuiElement>(5);
	}
	
	/**
	 * Adds a GuiElement to queue and caches it.
	 * @param element
	 */
	public  void addGuiElement(GuiElement element, 
			GuiPositionHorizontal horizontal, GuiPositionVertical vertical) {

		// Establishing Y position
		switch(vertical) {
			case TOP:
				element.box.y = MyGame.SCREEN_HEIGHT - element.box.height;
				break;
			case MIDDLE:
				element.box.y = (MyGame.SCREEN_HEIGHT - element.box.height)/2;
				break;
			case BOTTOM:
				element.box.y = 0;
				break;
		}
		
		// Establishing X position
		switch(horizontal) {
			case LEFT:
				element.box.x = 0;
				break;
			case MIDDLE:
				element.box.x = (MyGame.SCREEN_WIDTH - element.box.width)/2;
				break;
			case RIGHT:
				element.box.x = MyGame.SCREEN_WIDTH - element.box.width;
				break;
		}
		
		synchronized (guiElementsQueue) {
			guiElementsQueue.add(element);
			cacheGuiElements();
		}
		
	}
	
	/**
	 * Removes a GuiElement from queue and caches a new array.
	 * @param element
	 */
	public synchronized void removeGuiElement(GuiElement element) {
		guiElementsQueue.remove(element);
		cacheGuiElements();
	}
	
	public void draw(SpriteBatch spriteBatch) {
		for (int i = 0; i < guiElements.length; i++) {
			guiElements[i].draw(spriteBatch);
		}
	}

	/**
	 * Updates all of the GUI related stuff
	 * @param deltaTime
	 * @return true if any of the GUI elements were clicked 
	 */
	public boolean update(float deltaTime) {
		return false;
	}
	
	/**
	 * Saves an ordered guiElementsQueue into guiElements array
	 * to avoid calling queue.toArray() every frame. (No other way to iterate queue)
	 */
	public void cacheGuiElements() {
		// TODO Check if there's a way to avoid new
		guiElements = new GuiElement[guiElementsQueue.size()];
		guiElements = guiElementsQueue.toArray(guiElements);
		Arrays.sort(guiElements, guiElementsQueue.comparator());
	}
	
	public enum GuiPositionVertical {
		TOP,
		MIDDLE,
		BOTTOM,
	}
	
	public enum GuiPositionHorizontal {
		LEFT,
		MIDDLE,
		RIGHT,
	}
	
}
