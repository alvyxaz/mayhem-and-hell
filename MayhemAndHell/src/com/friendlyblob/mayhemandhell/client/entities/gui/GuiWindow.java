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

	public int contentOffsetY;
	
	private boolean dragging;
	private int draggingAtX;
	private int draggingAtY;
	
	private int titleDx;
	private int titleDy;
	
	// Ammount of pixels movement required to initiate dragging
	private int draggingTreshold = 2; 
	private boolean calculatingTreshold;
	
	public int paddingLeft;
	public int paddingTop;
	public int contentPaddingLeft;
	public int contentPaddingTop;
	public static final int contentPadding = 1;
	
	
	public GuiWindow() {
		super(GuiPriority.HIGH);
		ninePatch = Assets.getNinePatch("gui/ingame/window");
		ninePatch.setPadLeft(100);
		
		xTexture = Assets.getTextureRegion("gui/ingame/x");
		xRectangle = new Rectangle(0, 0, xTexture.getRegionWidth(), xTexture.getRegionHeight());
		
		titleTexture = Assets.getTextureRegion("gui/ingame/window_title");
		titleBox = new Rectangle(0, 0, titleTexture.getRegionWidth(), titleTexture.getRegionHeight());
		
		paddingLeft = (int) ninePatch.getLeftWidth();
		paddingTop = (int) ninePatch.getTopHeight();
		contentPaddingTop = paddingTop + contentPadding;
		contentPaddingLeft = paddingLeft + contentPadding;
	}
	
	@Override
	public final void onRelease(float x, float y) {
		titleDx = 0;
		titleDy = 0;
		calculatingTreshold = false;
		if (xRectangle.contains(box.x + x, box.y + y)) {
			visible = false;
		}
		onContentRelease(x, y);
	}
	
	public abstract void onContentRelease(float x, float y);
	public abstract void onContentTouching(float x, float y);

	@Override
	public final void onTouching(float x, float y) {
		if (dragging) {
			setX(Input.getX()-draggingAtX);
			setY(Input.getY()-draggingAtY);
		}
		
		if (calculatingTreshold) {
			titleDx += Input.touch[0].dx;
			titleDx += Input.touch[0].dy;
			if (Math.abs(titleDx) > draggingTreshold || Math.abs(titleDy) > draggingTreshold ) {
				startDragging((int) x, (int) y);
			}
		}
		
		if (!dragging && titleBox.contains(box.x + x, box.y+y)) {
			calculatingTreshold = true;
		}
		
		onContentTouching(x, y);
	}
	
	public void startDragging(int x, int y) {
		titleDx = 0;
		titleDy = 0;
		calculatingTreshold = false;
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
	public abstract void establishSize();
	
	public void setWindowSize(int width, int height) {
		this.box.width = width + contentPaddingLeft*2;
		this.box.height = height + paddingTop*2 + titleBox.height + contentPadding*2;
		this.titleBox.width = width + contentPadding*2;
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		ninePatch.draw(spriteBatch, 
				box.x, 
				box.y, 
				box.width, 
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
		xRectangle.x = x + box.width - xRectangle.width - paddingLeft-1;
		titleBox.x = x + paddingLeft;
	}
	
	@Override
	public void setY(float y) {
		super.setY(y);
		xRectangle.y = y + box.height - xRectangle.height - paddingTop-1;
		titleBox.y = y + box.height - titleBox.height - paddingTop;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String string) {
		this.title = string;
	}

}
