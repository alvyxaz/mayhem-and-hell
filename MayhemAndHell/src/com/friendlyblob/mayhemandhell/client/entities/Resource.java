package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class Resource extends GameObject {

	private TextureRegion texture;
	
	public Resource(int objectId, String name, String icon) {
		super(objectId);
		this.name = name;

		this.texture = Assets.getTextureRegion("objects/"+icon);
		this.hitBox = new Rectangle(0, 0, texture.getRegionWidth(), texture.getRegionHeight());
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.draw(texture, position.x, position.y);
	}

	@Override
	public void update(float deltaTime) {
		
	}

}
