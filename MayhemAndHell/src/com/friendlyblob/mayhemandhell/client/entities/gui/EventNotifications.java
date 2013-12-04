package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

/**
 * Manages live notifications buffer
 * @author Alvys
 *
 */
public class EventNotifications {
	
	private static final Color REGULAR_COLOR = new Color(1f, 0.86f, 0f, 1);// Color(1f, 0.7f, 0, 1f);
	
	private static final int BUFFER_SIZE = 5;
	private static final float MAX_TIMER = 2; 
	
	private String[] notifications;
	private Color[] colors;
	private float[] timers;
	private float[] y;
	
	private StringBuilder strBuilder;
	private int count;
	
	private BitmapFont font;
	
	public EventNotifications() {
		font = Assets.defaultFontStroked;
		notifications = new String[BUFFER_SIZE];
		colors = new Color[BUFFER_SIZE];
		timers = new float[BUFFER_SIZE];
		y = new float[BUFFER_SIZE];
		
		for (int i = 0; i < BUFFER_SIZE; i++) {
			notifications[i] = "";
			colors[i] = Color.WHITE;
		}
		
		strBuilder = new StringBuilder(128);
		count = 0;
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime) {
		if (count <= 0) {
			return;
		}
		
		for (int i = 0; i < BUFFER_SIZE; i++) {
			if (timers[i] > 0) {
				//-------------------------------------
				// UPDATE
				timers[i] -= deltaTime;
//				y[i] += deltaTime*verticalSpeed;
				if (timers[i] <= 0) {
					count--;
				}
				//-------------------------------------
				// DRAW
				font.setColor(colors[i]);
				font.drawWrapped(spriteBatch, notifications[i], 0, y[i], 
						MyGame.SCREEN_WIDTH, HAlignment.CENTER);
			}
		}
		// Restore regular color
		font.setColor(Color.WHITE);
	}
	
	public synchronized void addNotification (String notification, Color color) {
		int oldest = 0;
		for (int i = 0; i < BUFFER_SIZE; i++) {
			if (timers[i] < timers[oldest]) {
				oldest = i;
			}
			if (timers[i] > 0) {
				y[i] += font.getLineHeight();
			}
		}
		
		// Pushing notification
		this.timers[oldest] = MAX_TIMER;
		this.notifications[oldest] = notification;
		this.colors[oldest] = color;
		this.y[oldest] = MyGame.SCREEN_HEIGHT * 0.75f;
		count++;
	}
	
	public void addRegularNotification(String notification) {
		addNotification(notification, REGULAR_COLOR);
	}
	
}
