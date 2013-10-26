package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.Gdx;
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
		super(id, x, y, 0);

		int width = 32;
		int height = 32;
		
		Texture texture = Assets.manager.get("textures/characters/characters.png", Texture.class);
		
		textures = new TextureRegion[12];
		
		for (int i = 0; i < textures.length; i++) {
			textures[i] = new TextureRegion(texture, 0 + (i%3)*width, 0 + (i/3)*height, 32, 32);
		}
	}

	public void update(float deltaTime){
		super.update(deltaTime);
	}
	
	public void requestMovementDestination(int x, int y) {
		MyGame.connection.sendPacket(new RequestMove(x, y));
	}

	
}
