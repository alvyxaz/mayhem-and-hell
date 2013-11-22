package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.friendlyblob.mayhemandhell.client.entities.gui.SlotObject;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class Item extends GameObject {
	
	private SlotObject inventoryItem;
	private int itemId;
	
	public Item(int objectId, int itemId, int x, int y) {
		super(objectId);
		this.itemId = itemId;
		this.position = new Vector2(x, y);
		
		// TODO variable hitbox size?
		this.hitBox = new Rectangle(x-12, y, 24, 24);
		inventoryItem = new SlotObject(24);
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
	
	public SlotObject getInventoryItem() {
		return inventoryItem;
	}

	@Override
	public void draw(SpriteBatch sb) {
		sb.setColor(1f, 1f, 1f, 0.1f);
		sb.draw(Assets.px, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
		sb.setColor(Color.WHITE);
		// TODO draw by itemId
		sb.draw(Assets.getTextureRegion("icons/sword"), hitBox.x, hitBox.y);
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
	}
}
