package com.friendlyblob.mayhemandhell.client.entities.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

/**
 * Represents a basic draggable window
 * @author Alvys
 *
 */
public abstract class GuiWindow extends GuiElement {

	private NinePatch ninePatch;
	
	private TextureRegion xTexture;
	private Rectangle xRectangle;
	
	private TextureRegion titleTexture;
	private Rectangle titleBox;
	
	private String title = "Window";

	private int contentOffsetY;
	
	private boolean dragging;
	private int draggingAtX;
	private int draggingAtY;
	
	public GuiWindow() {
		super(GuiPriority.HIGH);
		ninePatch = Assets.getNinePatch("gui/ingame/window");
		ninePatch.setPadLeft(100);
		
		xTexture = Assets.getTextureRegion("gui/ingame/x");
		xRectangle = new Rectangle(0, 0, xTexture.getRegionWidth(), xTexture.getRegionHeight());
		
		titleTexture = Assets.getTextureRegion("gui/ingame/window_title");
		titleBox = new Rectangle(0, 0, titleTexture.getRegionWidth(), titleTexture.getRegionHeight());
		
		setWindowSize(100, 100);
	}
	
	@Override
	public void onRelease(float x, float y) {
		if (xRectangle.contains(box.x + x, box.y + y)) {
			visible = false;
		}
	}

	@Override
	public void onTouching(float x, float y) {
		if (dragging) {
			setX(Input.getX()-draggingAtX);
			setY(Input.getY()-draggingAtY);
		}
		if (!dragging && titleBox.contains(box.x + x, box.y+y)) {
			startDragging((int) x, (int) y);
		}
	}
	
	public void startDragging(int x, int y) {
		dragging = true;
		draggingAtX = (int)x;
		draggingAtY = (int)y;
		this.manager.dragging = this;
	}
	
	public void stopDragging() {
		dragging = false;
		this.manager.dragging = null;
	}

	@Override
	public void establishSize() {
		setWindowSize(200, 100);
	}
	
	public void setWindowSize(int width, int height) {
		this.box.width = width;
		this.box.height = height;
		this.titleBox.width = width;
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		ninePatch.draw(spriteBatch, 
				box.x-ninePatch.getLeftWidth(), 
				box.y + ninePatch.getTopHeight(), 
				box.width + ninePatch.getLeftWidth()*2, 
				box.height);
		
		spriteBatch.draw(titleTexture, titleBox.x, titleBox.y, titleBox.width, titleBox.height);
		spriteBatch.draw(xTexture, xRectangle.x, xRectangle.y);
		Assets.defaultFont.draw(spriteBatch, title, titleBox.x+3, titleBox.y+titleBox.height);
	}

	@Override
	public void update(float deltaTime) {
		
	}
	
	public void offsetMovement(float offsetX, float offsetY) {
		setX(box.x + offsetX);
		setY(box.y + offsetY);
	}
	
	@Override
	public void setX(float x) {
		super.setX(x);
		xRectangle.x = x + box.width - xRectangle.width;
		titleBox.x = x;
	}
	
	@Override
	public void setY(float y) {
		super.setY(y);
		xRectangle.y = y + box.height - xRectangle.height;
		titleBox.y = y + box.height - titleBox.height;
	}

}
