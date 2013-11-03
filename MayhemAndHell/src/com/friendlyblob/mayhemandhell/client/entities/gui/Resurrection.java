package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.Connection;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RequestResurrection;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RequestTarget;

public class Resurrection extends GuiElement {

	private Rectangle button;
	
	public Resurrection() {
		super(GuiPriority.HIGH);
		this.visible = false;
		button = new Rectangle(0, 0, 70, 20);
	}

	public void display() {
		this.visible = true;
	}
	
	public void hide() {
		this.visible = false;
	}
	
	@Override
	public void onRelease(float x, float y) {
		if (Input.isReleasing(button)) {
			MyGame.connection.sendPacket(new RequestResurrection());
		}
	}

	@Override
	public void onTouching(float x, float y) {
		
	}
	
	@Override
	public void setX(float x) {
		super.setX(x);
		button.x = x + (box.width - button.width)/2;
	}
	
	@Override
	public void setY(float y) {
		super.setY(y);
		button.y = y + 2;
	}

	@Override
	public void establishSize() {
		box.width = 200;
		box.height = 40;
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.setColor(Color.DARK_GRAY);
		spriteBatch.draw(Assets.px, box.x, box.y , box.width, box.height);
		spriteBatch.setColor(Color.WHITE);
		
		Assets.defaultFont.draw(spriteBatch, "You, my friend, are dead...", box.x + 5, box.y+ box.height - 5);
	
		spriteBatch.setColor(0.9f, 0.4f, 0.15f, 1f);
		spriteBatch.draw(Assets.px, button.x, button.y , button.width, button.height);
		spriteBatch.setColor(Color.WHITE);
		Assets.defaultFont.draw(spriteBatch, "Ressurect", button.x + 4, button.y+ button.height - 4);
	}

	@Override
	public void update(float deltaTime) {
		
	}

}
