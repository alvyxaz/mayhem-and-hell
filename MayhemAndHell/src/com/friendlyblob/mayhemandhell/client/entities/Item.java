package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class Item extends GameObject {
	
	private int itemId;
	
	private TextureRegion texture;
	
	public Item(int itemId, int objectId) {
		super(objectId);
		this.itemId = itemId;
		
		texture = Assets.getTextureRegion("icons/sword");
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
