package com.friendlyblob.mayhemandhell.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.friendlyblob.mayhemandhell.client.MyGame;

public class TestScreen extends BaseScreen {

	private TextureRegion slotTexture;
	
	public TestScreen(MyGame game) {
		super(game);
		Texture texture = new Texture(Gdx.files.internal("textures/gui/ingame/skill_slot.png"));
		slotTexture = new TextureRegion(texture, 0, 0, 16, 16);
	}

	@Override
	public void draw(float deltaTime) {
		spriteBatch.begin();
		spriteBatch.draw(slotTexture, 20, 20);
		spriteBatch.end();
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepare() {
		// TODO Auto-generated method stub
		
	}

}
