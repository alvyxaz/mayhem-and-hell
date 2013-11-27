package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class Resource extends GameObject {

	private TextureRegion texture;
	private String name;
	
	public Resource(int objectId, String name, String icon) {
		super(objectId);
		this.name = name;
		texture = Assets.getTextureRegion("objects/"+icon);
		this.hitBox = new Rectangle(0, 0, texture.getRegionWidth(), texture.getRegionHeight());
	}

	public void setPosition (int x, int y) {
		position.set(x, y);
		this.hitBox.x = x;
		this.hitBox.y = y;
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.draw(texture, position.x, position.y);
	}

	@Override
	public void update(float deltaTime) {
		
	}

}
