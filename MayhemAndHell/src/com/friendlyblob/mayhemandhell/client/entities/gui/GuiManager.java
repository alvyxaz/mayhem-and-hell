package com.friendlyblob.mayhemandhell.client.entities.gui;

import java.util.Arrays;
import java.util.PriorityQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.entities.gui.chat.Chat;
import com.friendlyblob.mayhemandhell.client.entities.gui.inventory.InventoryIcon;
import com.friendlyblob.mayhemandhell.client.entities.gui.inventory.InventoryView;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;

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
	
	public TargetBar targetBar;
	public InventoryView inventory;
	public Dialog dialog;
	public InventoryIcon inventoryIcon;
	public ActionsBar actionsBar;
	public PlayerStatus playerStatus;
	public Resurrection resurrection;
	public CastingBar castingBar;
	
	public Chat chat;
	
	public GuiWindow dragging = null;
	
	public GuiManager() {
		guiElements = new GuiElement[0];
		guiElementsQueue = new PriorityQueue<GuiElement>(5);
		
		targetBar = new TargetBar();
		addGuiElement(targetBar, GuiPositionHorizontal.MIDDLE, GuiPositionVertical.TOP);
		
		inventory = new InventoryView();
		inventory.setDataSource(GameWorld.getInstance().getPlayer().getInventory());
		addGuiElement(inventory, GuiPositionHorizontal.MIDDLE, GuiPositionVertical.MIDDLE);
		
		actionsBar = new ActionsBar();
		addGuiElement(actionsBar, GuiPositionHorizontal.RIGHT, GuiPositionVertical.BOTTOM);
		
		chat = new Chat();
		addGuiElement(chat, GuiPositionHorizontal.LEFT, GuiPositionVertical.BOTTOM);
		
		playerStatus = new PlayerStatus();
		addGuiElement(playerStatus, GuiPositionHorizontal.LEFT, GuiPositionVertical.TOP);
		
		resurrection = new Resurrection();
		addGuiElement(resurrection, GuiPositionHorizontal.MIDDLE, GuiPositionVertical.MIDDLE);
		
		inventoryIcon = new InventoryIcon();
		addGuiElement(inventoryIcon, GuiPositionHorizontal.RIGHT, GuiPositionVertical.TOP);
		
		dialog = new Dialog();
		addGuiElement(dialog, GuiPositionHorizontal.MIDDLE, GuiPositionVertical.MIDDLE);
		
		castingBar = new CastingBar();
		addGuiElement(castingBar, GuiPositionHorizontal.MIDDLE, GuiPositionVertical.BOTTOM);
	}
	
	/**
	 * Adds a GuiElement to queue and caches it.
	 * @param element
	 */
	public void addGuiElement(GuiElement element, 
			GuiPositionHorizontal horizontal, GuiPositionVertical vertical) {
		element.setManager(this);
		element.establishSize();
		
		// Establishing Y position
		switch(vertical) {
			case TOP:
				element.setY(MyGame.SCREEN_HEIGHT - element.box.height);
				break;
			case MIDDLE:
				element.setY((MyGame.SCREEN_HEIGHT - element.box.height)/2);
				break;
			case BOTTOM:
				element.setY(0);
				break;
		}
		
		// Establishing X position
		switch(horizontal) {
			case LEFT:
				element.setX(0);
				break;
			case MIDDLE:
				element.setX((MyGame.SCREEN_WIDTH - element.box.width)/2);
				break;
			case RIGHT:
				element.setX(MyGame.SCREEN_WIDTH - element.box.width);
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
		// Going backwards, because highest priority has to be drawn last
		for (int i = guiElements.length-1; i >= 0; i--) {
			if (!guiElements[i].visible) {
				continue;
			}
			guiElements[i].draw(spriteBatch);
		}
	}

	/**
	 * Updates all of the GUI related stuff
	 * @param deltaTime
	 * @return true if any of the GUI elements were clicked 
	 */
	public boolean update(float deltaTime) {
		for (int i = 0; i < guiElements.length; i++) {
			if (guiElements[i].visible) {
				guiElements[i].update(deltaTime);
			}
		}
		
		if (!Gdx.input.isTouched() && !Input.isReleasing()) {
			// We can pretend that gui was clicked so that
			// we don't have to analyse click input in gameplay logic.
			return true;
		}
		
		// Disable other element input checking while dragging a window
		if (dragging != null) {
			dragging.onTouching(Input.getX()-dragging.box.x, Input.getY()-dragging.box.y);
			if (Input.isReleasing()) {
				dragging.stopDragging();
			}
			return true;
		}
		
		for (int i = 0; i < guiElements.length; i++) {
			if (!guiElements[i].visible) {
				continue;
			}
			
			if (Input.isTouching(guiElements[i].box)) {
				guiElements[i].onTouching(Input.getX()-guiElements[i].box.x, Input.getY()-guiElements[i].box.y);
			} else if (Input.isReleasing(guiElements[i].box)) {
				guiElements[i].onRelease(Input.getX()-guiElements[i].box.x, Input.getY()-guiElements[i].box.y);
				return true;
			}
		}
		
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
