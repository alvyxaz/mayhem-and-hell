package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.entities.GameObject;
import com.friendlyblob.mayhemandhell.client.entities.GameObject.GameObjectType;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RequestTarget;

public class TargetBar extends GuiElement {

	private TargetInfo targetInfo;
	
	private TextureRegion xTexture;
	private TextureRegion background;
	
	private Rectangle xRectangle;
	
	private float healthPercentage = 1;
	
	public TargetBar() {
		super(GuiPriority.LOW);
		
		xTexture = Assets.getTextureRegion("gui/ingame/x");
		background = Assets.getTextureRegion("gui/ingame/target_background");
		
		xRectangle = new Rectangle(0, 0, xTexture.getRegionWidth(), xTexture.getRegionHeight());
		
		visible = false;
	}

	@Override
	public void setX(float x) {
		super.setX(x);
		xRectangle.x = x + box.width - xRectangle.width;
	}
	
	@Override
	public void setY(float y) {
		super.setY(y);
		xRectangle.y = y + box.height - xRectangle.height;
	}
	
	@Override
	public void onRelease(float x, float y) {
		if (xRectangle.contains(box.x + x, box.y + y)) {
			removeTarget(); 
		}
	}

	@Override
	public void onTouching(float x, float y) {
		
	}

	@Override
	public void establishSize() {
		box.width = background.getRegionWidth();
		box.height = background.getRegionHeight();
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.draw(background, box.x, box.y);
		spriteBatch.draw(xTexture, xRectangle.x, xRectangle.y);
		Assets.defaultFont.draw(spriteBatch, targetInfo.name, box.x + 5, box.y + box.height - 2);
		
		spriteBatch.setColor(Color.GRAY);
		spriteBatch.draw(Assets.px, box.x+4, box.y+2, (box.width-8), 2);
		spriteBatch.setColor(Color.RED);
		spriteBatch.draw(Assets.px, box.x+4, box.y+2, (box.width-8) * healthPercentage, 2);
		spriteBatch.setColor(Color.WHITE);
	}

	@Override
	public void update(float deltaTime) {
	
	}
	
	public void updateHealthPercentage() {
		this.healthPercentage = targetInfo.currentHealth / (float)targetInfo.maxHealth;
	}
	
	public void removeTarget() {
		this.visible = false;
		this.targetInfo = null;
		manager.actionsBar.hide();
		MyGame.connection.sendPacket(new RequestTarget(-1));
	}

	public void showTarget(TargetInfo info, String[] actions) {
		this.targetInfo = info;
		this.visible = true;
		updateHealthPercentage();
		manager.actionsBar.show(actions);
	}
	
	public TargetInfo getTargetInfo() {
		return this.targetInfo;
	}
	
	public static class TargetInfo {
		public String name;
		public int objectId;
		public GameObjectType objectType;
		public int currentHealth;
		public int maxHealth;
		public boolean hasHealth;
		
		public void cleanup() {
			this.name = "";
			this.objectType = GameObjectType.OTHER;
			this.currentHealth = 0;
			this.maxHealth = 0;
		}
	}

}
