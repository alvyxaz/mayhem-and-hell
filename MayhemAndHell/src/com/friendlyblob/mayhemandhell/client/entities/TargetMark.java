package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class TargetMark {

	private TextureRegion texture;
	private GameObject target;
	private int targetWidth;
	private int targetHeight;
	
	private boolean hostile;
	
	public TargetMark() {
		texture = Assets.getTextureRegion("other/target_mark");
		targetWidth = texture.getRegionWidth();
		targetHeight = texture.getRegionHeight();
	}
	
	public void draw(SpriteBatch spriteBatch) {
		if (hostile) {
			spriteBatch.setColor(1f, 0.1f, 0, 1f);
		} else {
			spriteBatch.setColor(0.1f, 0.6f, 1f, 1f);
		}
		
		if (target != null) {
			spriteBatch.draw(texture, target.hitBox.x + (target.hitBox.width - targetWidth)/2, target.hitBox.y-targetHeight/2 + 2);
		}
		spriteBatch.setColor(Color.WHITE);
	}
	
	public TargetMark setTarget(GameObject target) {
		this.target = target;
		return this;
	}
	
	public void setHostile(boolean hostile) {
		this.hostile = hostile;
	}
	
}
