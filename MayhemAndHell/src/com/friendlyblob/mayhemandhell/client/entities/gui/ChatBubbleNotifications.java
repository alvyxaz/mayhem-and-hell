package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.GameObject;
import com.friendlyblob.mayhemandhell.client.entities.Player;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

/**
 * Manages live notifications buffer
 * @author Alvys
 *
 */
public class ChatBubbleNotifications {
	
	private static final Color REGULAR_COLOR = new Color(1f, 0.86f, 0f, 1);// Color(1f, 0.7f, 0, 1f);
	
	private static final int BUFFER_SIZE = 2;
	private static final float MAX_TIMER = 2; 
	private static final int BUBBLE_MAX_WIDTH = 100;
	
	private String[] notifications;
	private Color[] colors;
	private float[] timers;
	
	private StringBuilder strBuilder;
	private int count;
	
	private BitmapFont font;
	
	private GameObject gameObject;
	
	public ChatBubbleNotifications(GameObject gameObject) {
		this.gameObject = gameObject;
		
		font = Assets.defaultFontStroked;
		notifications = new String[BUFFER_SIZE];
		colors = new Color[BUFFER_SIZE];
		timers = new float[BUFFER_SIZE];
		
		for (int i = 0; i < BUFFER_SIZE; i++) {
			notifications[i] = "";
			colors[i] = Color.WHITE;
		}
		
		strBuilder = new StringBuilder(128);
		count = 0;
	}
	
	public void draw(SpriteBatch spriteBatch) {
		if (count <= 0) {
			return;
		}
		
		for (int i = 0; i < BUFFER_SIZE; i++) {	
			font.setColor(colors[i]);
			// TODO: center the text
			font.drawWrapped(
				spriteBatch, 
				notifications[i], 
				gameObject.position.x, 
				gameObject.position.y + gameObject.hitBox.height + gameObject.hitBox.height/2,
				BUBBLE_MAX_WIDTH
			);
		}
		// Restore regular color
		font.setColor(Color.WHITE);
	}
	
	public void update(float deltaTime) {
		for (int i = 0; i < BUFFER_SIZE; i++) {
			if (timers[i] > 0) {
				//-------------------------------------
				// UPDATE
				timers[i] -= deltaTime;

				if (timers[i] <= 0) {
					count--;
				}
			}
		}
	}
	
	public synchronized void addNotification (String notification, Color color) {
		int oldest = 0;
		for (int i = 0; i < BUFFER_SIZE; i++) {
			if (timers[i] < timers[oldest]) {
				oldest = i;
			}
		}
		
		// Pushing notification
		this.timers[oldest] = MAX_TIMER;
		this.notifications[oldest] = notification;
		this.colors[oldest] = color;
		count++;
	}
	
	public void addRegularNotification(String notification) {
		addNotification(notification, REGULAR_COLOR);
	}
	
}
