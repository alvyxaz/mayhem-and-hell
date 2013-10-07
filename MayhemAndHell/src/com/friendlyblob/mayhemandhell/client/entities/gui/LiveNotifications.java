package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

/**
 * Manages live notifications buffer
 * @author Alvys
 *
 */
public class LiveNotifications {
	
	private static final int BUFFER_SIZE = 10;
	private static final float maxTimer = 1; 
	private static final int verticalSpeed = 25; // Pixels per second
	
	private String[] notifications;
	private Color[] colors;
	private float[] timers;
	private float[] x;
	private float[] y;
	
	private StringBuilder strBuilder;
	private int count;
	
	public LiveNotifications() {
		notifications = new String[BUFFER_SIZE];
		colors = new Color[BUFFER_SIZE];
		timers = new float[BUFFER_SIZE];
		x = new float[BUFFER_SIZE];
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
				y[i] += deltaTime*verticalSpeed;
				if (timers[i] <= 0) {
					count--;
				}
				//-------------------------------------
				// DRAW
				Assets.defaultFont.setColor(colors[i]);
				Assets.defaultFont.draw(spriteBatch, notifications[i], x[i], y[i]);
			}
		}
		// Restore regular color
		Assets.defaultFont.setColor(Color.WHITE);
	}
	
	public void addNotification (String notification, int x, int y, Color color) {
		int oldest = 0;
		for (int i = 0; i < BUFFER_SIZE; i++) {
			if (timers[i] < timers[oldest]) {
				oldest = i;
			} 
			if (timers[i] <= 0){
				oldest = i;
				count++;
				break;
			}
		}
		
		// Pushing notification
		this.timers[oldest] = maxTimer;
		this.notifications[oldest] = notification;
		this.colors[oldest] = color;
		this.x[oldest] = x;
		this.y[oldest] = y;
	}
	
	/**
	 * Adds a notification  that represents some sort of health change.
	 * Green color if gained health (positive offset), red if lost,
	 * and yellow if offset is equal to 0 (Indicates "miss").
	 * @param offset
	 * @param x
	 * @param y
	 */
	public void addHealthChangeNotification(int offset, int x, int y) {
		Color color = null;
		
		strBuilder.setLength(0);
		if (offset > 0) {
			strBuilder.append("+").append(offset);
			color = Color.GREEN;
		} else if (offset < 0) {
			strBuilder.append(offset);
			color = Color.RED;
		} else {
			strBuilder.append("miss");
			color = Color.YELLOW;
		}
		
		addNotification(strBuilder.toString(), x, y, color);
	}
	
}
