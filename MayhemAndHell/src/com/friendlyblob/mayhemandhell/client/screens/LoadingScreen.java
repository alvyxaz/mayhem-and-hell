package com.friendlyblob.mayhemandhell.client.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class LoadingScreen extends BaseScreen{

	private Texture px;
	
	public LoadingScreen(MyGame game) {
		super(game);
		Assets.manager.load("textures/debugging/px.png", Texture.class);
		Assets.manager.load("textures/textures.atlas", TextureAtlas.class);
		
	}

	@Override
	public void draw(float deltaTime) {
		if(Assets.manager.update()){
			if(game.screenGame == null)
				game.screenGame = new GameScreen(game);
			game.setScreen(game.screenZoneLoading);
		}
		
		// Between 0 and 1
		float progress = Assets.manager.getProgress();
	}

	@Override
	public void update(float deltaTime) {
		
	}

	@Override
	public void prepare() {
		
	}

}
