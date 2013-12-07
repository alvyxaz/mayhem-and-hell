package com.friendlyblob.mayhemandhell.client.entities.gui.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class SlotObject extends Rectangle {
	
	public SlotObject(int width, int height) {
		super(0, 0, width, height);
		// TODO Auto-generated constructor stub
	}
	
	private TextureRegion texture = Assets.getTextureRegion("icons/sword");
	
	@Override
	public Rectangle setPosition(float x, float y) {
		this.x = x - width/2;
		this.y = y - height/2;
		
		return this;
	}
	
	public TextureRegion getTexture() {
		return texture;
	}
}
