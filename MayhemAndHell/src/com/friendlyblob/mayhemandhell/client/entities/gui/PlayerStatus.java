package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class PlayerStatus extends GuiElement {

	private float healthPercentage = 1; 
	
	private int currentHealth;
	private int maxHealth;
	
	public PlayerStatus() {
		super(GuiPriority.LOW);
	}

	public void setHealth(int currentHealth, int maxHealth) {
		this.currentHealth = currentHealth;
		this.maxHealth = maxHealth;
		this.healthPercentage = currentHealth / (float)maxHealth;
	}
	
	public void updateHealthPercentage() {
		
	}
	
	@Override
	public void onRelease(float x, float y) {
		
		visible = true;
	}

	@Override
	public void onTouching(float x, float y) {
	}

	@Override
	public void establishSize() {
		box.width = 100;
		box.height = 20;
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.setColor(0.3f, 0.2f, 0, 1f);
		spriteBatch.draw(Assets.px, box.x, box.y, box.width, box.height);
		
		spriteBatch.setColor(Color.GRAY);
		spriteBatch.draw(Assets.px, box.x+4, box.y+2, (box.width-8), 2);
		spriteBatch.setColor(Color.RED);
		spriteBatch.draw(Assets.px, box.x+4, box.y+2, (box.width-8) * healthPercentage, 2);
		spriteBatch.setColor(Color.WHITE);
	}

	@Override
	public void update(float deltaTime) {
	}

}
