package com.friendlyblob.mayhemandhell.client.screens;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.XmlReader;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.entities.EnvironmentObject;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.gameworld.Map;
import com.friendlyblob.mayhemandhell.client.gameworld.MapData;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

/**
 * Represents a loading screen for zones.
 * Every resource that zone needs has to be loaded from this class.
 * @author Alvys
 *
 */
public class ZoneLoadingScreen extends BaseScreen{

	private Texture px;
	
	private MapData loadedMapData;
	
	public ZoneLoadingScreen(MyGame game, String title) {
		super(game);
		
		int [][] tilemap = new int[100][100];

	}

	@Override
	public void draw(float deltaTime) {
//		if(Assets.manager.update()){
//			// Create a new game screen if necessary
//			if(game.screenGame == null)
//				game.screenGame = new GameScreen(game);
//			
//			loadedMapData.initializeTextureRegions();
//			
//			game.setScreen(game.screenGame);
//		}
//		
//		// Between 0 and 1
//		float progress = Assets.manager.getProgress();
	}

	/**
	 * Loads data for zone 
	 */
	public void loadZone(int zoneId) {
		
		/*------------------------------------------
		 * READING MAP DATA FROM A FILE
		 */
		// TODO implement, instead of using default values
		MapData mapData = new MapData(zoneId);
		
		loadedMapData = mapData;
		
		/*------------------------------------------
		 * USING MAP DATA TO CREATE A MAP
		 */
		GameWorld.getInstance().createMap(mapData);
		
		/*------------------------------------------
		 * QUEUEING TO LOAD REQUIRED RESOURCES
		 */
		for (Entry<Integer,AssetDescriptor> entry : mapData.getRequiredResources().entrySet()) {
			Assets.manager.load(entry.getValue());
		}
	}
	
	@Override
	public void update(float deltaTime) {
		
	}

	@Override
	public void prepare() {
		
	}

}
