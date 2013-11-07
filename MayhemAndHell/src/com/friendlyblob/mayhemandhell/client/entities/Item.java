package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class Item extends GameObject {
	
	private int itemId;
	
	public Item(int objectId, int itemId, int x, int y) {
		super(objectId);
		this.itemId = itemId;
		this.position = new Vector2(x, y);
		
		// TODO variable hitbox size?
		this.hitBox = new Rectangle(x-7.5f, y, 12, 12);
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	@Override
	public void draw(SpriteBatch sb) {
		// TODO draw by itemId
		sb.draw(Assets.getTextureRegion("icons/sword"), position.x, position.y);
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
	}
}
