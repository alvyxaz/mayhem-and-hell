package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.friendlyblob.mayhemandhell.client.controls.Touch;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class SkillBar extends GuiElement {
	
	private final int slots = 5;
	private final int slotWidth = 16;
	private final int slotHeight = 16;
	
	private TextureRegion slotTexture;
	
	public SkillBar(GuiPriority priority) {
		super(priority);
		
		slotTexture = Assets.getTextureRegion("gui/ingame/skill_slot");
		Assets.manager.load("textures/textures.atlas", TextureAtlas.class);
	}
	
	private TextureRegion slotIcon;

	@Override
	public void onRelease(float x, float y) {
		int slotSelected = (int) (y / slotHeight);
	}

	@Override
	public void onTouching(float x, float y) {
		
	}

	@Override
	public void establishSize() {
		box.width = slotWidth;
		box.height = slotHeight*slots;
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		for (int i = 0; i < slots; i++) {
			spriteBatch.draw(slotTexture, box.x, box.y+i*slotHeight);
		}
	}

	@Override
	public void update(float deltaTime) {
	}
	
}
