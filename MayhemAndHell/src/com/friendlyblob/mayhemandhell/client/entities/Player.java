package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class Player extends GameCharacter{
	
	public Player (int id, int x, int y){
		super(id, x, y);
	}

	public void regenerate(int x, int y){

	}
	
	public void update(float deltaTime){
		super.update(deltaTime);
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch){
		spriteBatch.setColor(Color.CYAN);
		spriteBatch.draw(Assets.px, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
		spriteBatch.setColor(Color.WHITE);
	}
	
	public void requestMovementDestination(int x, int y) {
		// TODO Wait for response before moving
//		MyGame.connection.sendPacket(new RequestMove(x, y));

		moveTo(x, y,50);
	}
	
}
