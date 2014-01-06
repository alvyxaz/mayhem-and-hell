package com.friendlyblob.mayhemandhell.client.entities.gui.shop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.ChatMessagePacket;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RequestBuyItem;

public class ShopListItem extends Rectangle {
	
	public static int WIDTH = 100;
	public static int HEIGHT = 20;
	
	public Rectangle buyButton;
	
	// TODO: Needs stats set and full structure of
	// the item from the server
	private int itemId;
	private String name;
	private int price;
	
	public ShopListItem(int itemId, String name, int price) {
		super(0, 0, WIDTH, HEIGHT);
		
		this.itemId = itemId;	
		this.name = name;
		this.price = price;
		
		buyButton = new Rectangle(0, 0, 15, 8);
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPrice() {
		return price;
	}

	public void draw(SpriteBatch spriteBatch) {
		// TODO: proper icon drawing
		// draw bg
		spriteBatch.setColor(.3f, .0f, .0f, .3f);
		spriteBatch.draw(Assets.px, x, y, ShopListItem.WIDTH, ShopListItem.HEIGHT);
		spriteBatch.setColor(Color.WHITE);
		// draw data
		spriteBatch.draw(getTexture(), x, y - 2);
		Assets.defaultFont.draw(spriteBatch, getName(), x + getTexture().getRegionWidth(), y + ShopListItem.HEIGHT - Assets.defaultFont.getLineHeight()/2);
		// draw buy button
		buyButton.setPosition(x + WIDTH - buyButton.width - 4, y  + HEIGHT/2 - buyButton.height/2);
		spriteBatch.setColor(213/255f, 154/255f, 109/255f, 1f);
		spriteBatch.draw(Assets.px, buyButton.x, buyButton.y, buyButton.width, buyButton.height);
	}
	
	public TextureRegion getTexture() {
		return Assets.getTextureRegion("icons/sword");
	}

	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
	}
	
	public void onContentRelease(float x, float y) {
		if (buyButton.contains(x, y)) {
			MyGame.connection.sendPacket(new RequestBuyItem(itemId));
		}
	}
}
