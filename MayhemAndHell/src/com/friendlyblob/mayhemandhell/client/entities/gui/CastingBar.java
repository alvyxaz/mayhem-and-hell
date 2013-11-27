package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class CastingBar extends GuiElement{
	
	private TextureRegion bgTexture;
	private TextureRegion bgProgressTexture;

	private int progressWidth;
	private int progressHeight;
	
	// Casting data
	private float maxTime;
	private float timeLeft;
	private String text;
	private float percentage;
	private Color color = new Color(1f, 0, 0, 1f);
	
	public CastingBar() {
		super(GuiPriority.HIGH);
		bgTexture = Assets.getTextureRegion("gui/ingame/casting_bg");
		bgProgressTexture= Assets.getTextureRegion("gui/ingame/casting_progress_bg");
		visible = false;
	}

	@Override
	public void onRelease(float x, float y) {
		
	}

	@Override
	public void onTouching(float x, float y) {
		
	}

	@Override
	public void establishSize() {
		box.width = bgTexture.getRegionWidth();
		box.height = bgTexture.getRegionHeight();
		
		progressWidth = (int)box.width-2;
		progressHeight = (int)box.height-2;
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.setColor(color);
		
		spriteBatch.draw(bgTexture, box.x, box.y);
		spriteBatch.draw(bgProgressTexture, box.x+1, box.y+1, progressWidth*percentage, progressHeight);
		spriteBatch.draw(Assets.px, box.x + progressWidth*percentage, box.y+1, 1, progressHeight);
		
		spriteBatch.setColor(Color.WHITE);
		
		Assets.defaultFont.draw(spriteBatch, text, box.x+5, box.y + box.height-2);
	}

	@Override
	public void update(float deltaTime) {
		timeLeft -= deltaTime;
		percentage = timeLeft/maxTime;
		
		if (percentage < 0 ) {
			this.hide();
		}
	}
	
	public void startCasting(String text, int time) {
		this.maxTime = (float)(time/1000);
		this.timeLeft = maxTime;
		this.percentage = 1;
		
		this.text = text;
		this.show();
	}
	
}
