package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RequestMove;

public class Player extends GameCharacter {
	
	private TextureRegion [] textures;
	
	public int targetId;
	
	public Player (int id, int x, int y){
		super(id, x, y);

		int width = 32;
		int height = 32;
		
		Texture texture = Assets.manager.get("textures/characters/characters.png", Texture.class);
		
		textures = new TextureRegion[12];
		
		for (int i = 0; i < textures.length; i++) {
			textures[i] = new TextureRegion(texture, 0 + (i%3)*width, 0 + (i/3)*height, 32, 32);
		}
	}

	public void regenerate(int x, int y){

	}
	
	public void update(float deltaTime){
		super.update(deltaTime);
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch){
		spriteBatch.setColor(0f, 1f, 1f, 0.4f);
		spriteBatch.draw(Assets.px, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
		spriteBatch.setColor(Color.WHITE);

		if (isMoving()) {
			spriteBatch.draw(textures[this.direction*3 + calculateFrame()], hitBox.x, hitBox.y);
		} else {
			spriteBatch.draw(textures[this.direction*3], hitBox.x, hitBox.y);
		}
	}
	
	public void requestMovementDestination(int x, int y) {
		MyGame.connection.sendPacket(new RequestMove(x, y));
	}
	
	/**
	 * Even though we have only 3 frames available, 
	 * "currentFrame" has values from 0 to 3 (4 total).
	 * We change 2 and 3 frames to create one virtual frame.
	 * 
	 * If it's not clear why, check the texture and try to imagine 
	 * animation loop (there's no step inbetween 1 ant 2)
	 * @return
	 */
	public int calculateFrame() {
		if (currentFrame == 2) {
			return 0; // Virtual frame
		} else if (currentFrame == 3) {
			return 2;
		}
		return currentFrame;
	}
	
}
