package com.friendlyblob.mayhemandhell.client.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.animations.AnimationParser;
import com.friendlyblob.mayhemandhell.client.gameworld.MapData;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class LoadingScreen extends BaseScreen{

	private Texture px;
	
	public LoadingScreen(MyGame game) {
		super(game);
		Assets.manager.load("textures/atlas/textures.atlas", TextureAtlas.class);
		
		AnimationParser.loadAll();
	}

	@Override
	public void draw(float deltaTime) {
		if(Assets.manager.update()){
			game.prepareScreens();
			
			// TODO get a response from server, find out which zone a player is in,
			// and load a map of that zone.
			int zoneId = 1;
			game.screenZoneLoading.loadZone(zoneId);	// Initialize zone loading
			
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
