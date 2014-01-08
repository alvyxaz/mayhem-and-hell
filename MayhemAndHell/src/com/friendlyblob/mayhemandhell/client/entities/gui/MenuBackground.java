package com.friendlyblob.mayhemandhell.client.entities.gui;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class MenuBackground {

	private TextureRegion background;
	private TextureRegion bgPattern;
	
	private Rectangle[] bgPatterns;
	
	private int patternWidth;
	private int maxBlankPatternSize = 10;
	
	public MenuBackground() {
		background = Assets.getTextureRegion("gui/bg");
		bgPattern = Assets.getTextureRegion("gui/bg_pattern");
		
		patternWidth = (int) bgPattern.getRegionWidth();
		
		bgPatterns = new Rectangle[15];
		
		Random random = new Random();
		
		for (int i = 0; i < bgPatterns.length; i++ ) {
			if (random.nextInt(10) > 4) {
				bgPatterns[i] = new Rectangle(
						random.nextInt(MyGame.SCREEN_WIDTH - bgPattern.getRegionHeight()), 
						random.nextInt(MyGame.SCREEN_HEIGHT - bgPattern.getRegionWidth()),
						bgPattern.getRegionWidth(),
						bgPattern.getRegionHeight()
						);
			} else {
				bgPatterns[i] = new Rectangle(
						random.nextInt(MyGame.SCREEN_WIDTH - bgPattern.getRegionHeight()), 
						random.nextInt(MyGame.SCREEN_HEIGHT - bgPattern.getRegionWidth()),
						random.nextInt(maxBlankPatternSize)+1,
						random.nextInt(maxBlankPatternSize)+1);
			}
			
		}
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime) {
		spriteBatch.draw(background, 0, 0, MyGame.SCREEN_WIDTH, MyGame.SCREEN_HEIGHT);
		
		spriteBatch.setColor(1, 1, 1, 0.1f);
		for (int i = 0; i < bgPatterns.length; i++) {
			// If it's a pattern image
			if (bgPatterns[i].width == patternWidth) {
				spriteBatch.draw(bgPattern, bgPatterns[i].x, bgPatterns[i].y);
			} else {
				spriteBatch.draw(Assets.px, bgPatterns[i].x, bgPatterns[i].y, bgPatterns[i].width, bgPatterns[i].height);
			}
		}
		spriteBatch.setColor(Color.WHITE);
	}
	
}
