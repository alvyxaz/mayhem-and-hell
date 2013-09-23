package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RequestMove;

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
		MyGame.connection.sendPacket(new RequestMove(x, y));
	}
	
}
