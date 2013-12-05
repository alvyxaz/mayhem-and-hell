package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class PlayerStatus extends GuiElement {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 50;
	private static final int PADDING = 5;
	
	private static final int LINE_HEIGHT = 13;
	
	private ProgressBar healthBar;
	private ProgressBar energyBar;
	
	private NinePatch ninePatch;
	
	private Rectangle namePosition;
	
	public PlayerStatus() {
		super(GuiPriority.LOW);
		ninePatch = Assets.getNinePatch("gui/ingame/window");
		
		healthBar = new ProgressBar(0, 0, WIDTH - PADDING*2, new Color(1f, 0, 0, 1f));
		energyBar = new ProgressBar(0, 0, WIDTH - PADDING*2, new Color(0, 0.75f, 0.95f, 1f));
		energyBar.update("100/100", 1);
		
		namePosition = new Rectangle(PADDING, HEIGHT - PADDING, WIDTH - PADDING*2,  11);
		
	}

	public void setHealth(int currentHealth, int maxHealth) {
		healthBar.update(currentHealth+"/"+maxHealth, currentHealth / (float)maxHealth);
	}
	
	@Override
	public void onRelease(float x, float y) {
		
		visible = true;
	}

	@Override
	public void onTouching(float x, float y) {
	}

	@Override
	public void setX(float x) {
		super.setX(x);
		namePosition.x += box.x;
		healthBar.setX(x + PADDING);
		energyBar.setX(x + PADDING);
	}
	
	@Override
	public void setY(float y) {
		super.setY(y);
		namePosition.y += box.y;
		namePosition.setY(y + box.getHeight() - PADDING +3 - LINE_HEIGHT);
		healthBar.setY(y + box.getHeight() - PADDING +3 - 2*LINE_HEIGHT);
		energyBar.setY(y + box.getHeight() - PADDING +3 - 3*LINE_HEIGHT);
	}
	
	@Override
	public void establishSize() {
		box.width = WIDTH;
		box.height = HEIGHT;
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		
		ninePatch.draw(spriteBatch, box.x, box.y, box.width, box.height);
		
		spriteBatch.setColor(0.98f, 0.94f, 0.85f, 1f);
		spriteBatch.draw(Assets.px, namePosition.x, namePosition.y, namePosition.width, namePosition.height);
		spriteBatch.setColor(Color.WHITE);
		
		Assets.defaultFont.setColor(0.4f, 0.3f, 0.2f, 1f);
		Assets.defaultFont.draw(spriteBatch, "Roflcopteroid", namePosition.x+1, namePosition.y+ namePosition.height);
		Assets.defaultFont.setColor(Color.WHITE);
		
		healthBar.draw(spriteBatch);
		energyBar.draw(spriteBatch);
	}

	@Override
	public void update(float deltaTime) {
	}

	public static class ProgressBar {
		
		private TextureRegion bgTexture;
		private TextureRegion bgTextureEnd;
		private TextureRegion bgProgressTexture;
		private TextureRegion bgProgressStart;
		
		private Rectangle box;
		
		private int progressWidth;
		private int progressHeight = 11;
		
		private String text = "";
		
		private Color color;
		
		private float percentage = 1f;
		
		public ProgressBar(int x, int y, int width, Color color) {
			this.color = color;
			this.progressWidth = width-2;
			this.box = new Rectangle(x, y, width, progressHeight);
			TextureRegion texture = Assets.getTextureRegion("gui/ingame/status_bar");
			
			bgTexture = new TextureRegion( texture.getTexture(), texture.getRegionX()+3, 
					texture.getRegionY(), 1, texture.getRegionHeight());
			
			bgProgressTexture = new TextureRegion(texture.getTexture(), texture.getRegionX()+1, 
					texture.getRegionY(), 1, texture.getRegionHeight());
			
			bgProgressStart = new TextureRegion( texture.getTexture(), texture.getRegionX(), 
					texture.getRegionY(), 1, texture.getRegionHeight());
			
			bgTextureEnd = new TextureRegion( texture.getTexture(), texture.getRegionX()+4, 
					texture.getRegionY(), 1, texture.getRegionHeight());
		}
		
		public void draw(SpriteBatch spriteBatch) {
			// Right side
			spriteBatch.draw(bgTexture, box.x + progressWidth*percentage +1, box.y, progressWidth*(1- percentage), progressHeight);
			spriteBatch.draw(bgTextureEnd, box.x + progressWidth+1, box.y);
			
			// Left side
			spriteBatch.setColor(color);
			spriteBatch.draw(bgProgressTexture, box.x+1, box.y, progressWidth*percentage, progressHeight);
			spriteBatch.draw(bgProgressStart, box.x, box.y);
			if (percentage < 1 && percentage > 0) {
				spriteBatch.draw(Assets.px, box.x + progressWidth*percentage, box.y, 1, progressHeight);
			}
			spriteBatch.setColor(Color.WHITE);
			
			Assets.defaultFont.draw(spriteBatch, text, box.x+5, box.y + box.height-1);
		}
		
		public void setX(float x) {
			box.x = x;
		}
		
		public void setY(float y) {
			box.y = y;
		}
		
		public void setPosition(float x, float y) {
			box.x = x;
			box.y = y;
		}
		
		public void update(String text, float percentage) {
			this.text = text;
			this.percentage = percentage;
		}
		
	}
	
}
