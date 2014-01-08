package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class CharacterViewer extends Widget {
	
	private final static int SPRITE_WIDTH = 32;
	private final static int SPRITE_HEIGHT = 32;
	
    private int charId;
	private TextureRegion [] textures;
	private TextureRegion padTexture;
	
	public CharacterViewer(String path) {
		padTexture = Assets.getTextureRegion("gui/pad");
		
        Texture texture = Assets.manager.get(path, Texture.class);
		textures = new TextureRegion[texture.getWidth() / texture.getHeight()];
        
		for (int i = 0; i < textures.length; i++) {
			textures[i] = new TextureRegion(texture, i*SPRITE_WIDTH, 0, SPRITE_WIDTH, SPRITE_HEIGHT);
		}
	}
	
	@Override
	public void draw (SpriteBatch spriteBatch, float parentAlpha) {
		super.draw(spriteBatch, parentAlpha);
		
		// draw the pad underneath the character
		Color currentColor = spriteBatch.getColor();
		spriteBatch.setColor(currentColor.r, currentColor.g, currentColor.b, .15f);
		spriteBatch.draw(padTexture, getX()-1, getY()+1);
		spriteBatch.setColor(currentColor);
		// draw the character texture
		spriteBatch.draw(textures[charId], getX(), getY());
	}
	
	public void prev() {
		if (charId > 0) {
			charId--;
		}
	}
	
	public void next() {
		if (charId < textures.length - 1) {
			charId++;
		}
	}
	
	public int getCharId() {
		return charId;
	}
	
	public float getPrefWidth () {
		return SPRITE_WIDTH;
	}

	public float getPrefHeight () {
		return SPRITE_HEIGHT;
	}
}
