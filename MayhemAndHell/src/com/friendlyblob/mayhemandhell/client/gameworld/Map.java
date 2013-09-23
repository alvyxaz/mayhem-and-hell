package com.friendlyblob.mayhemandhell.client.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.entities.EnvironmentObject;
import com.friendlyblob.mayhemandhell.client.gameworld.MapData.TileMapLayer;
import com.friendlyblob.mayhemandhell.client.mapeditor.MapEditor;

public class Map {
	public static final int TILE_WIDTH = 16;
	public static final int TILE_HEIGHT = 16;
	
	private GameWorld world;
	
	// Camera related
	private OrthographicCamera worldCam;
	private Vector3 camPos;
	
	private MapData mapData;
	
	// Drawing optimization variables
	int startX;
	int startY;
	int endX;
	int endY;
	
	public Map(GameWorld world, MapData data) {
		this.world = world;
		this.mapData = data;
		
		// Camera setup
		worldCam = world.getWorldCam();
		camPos = worldCam.position;
		
	}
	
	/**
	 * Draws layers that are below objects (floors, ground and etc)
	 * @param spriteBatch
	 */
	public void drawBelow(SpriteBatch spriteBatch) {
		for (int i = 0; i < mapData.layersBelow.length; i++) {
			drawLayer(mapData.layersBelow[i], spriteBatch);
		}
	}
	
	/**
	 * Draws layers that are on top of game objects
	 * @param spriteBatch
	 */
	public void drawAbove(SpriteBatch spriteBatch) {
		for (int i = 0; i < mapData.layersAbove.length; i++) {
			drawLayer(mapData.layersAbove[i], spriteBatch);
		}
		spriteBatch.enableBlending();
	}
	
	// TODO cache map data if performance is low
	public void drawLayer(TileMapLayer layer, SpriteBatch spriteBatch) {
		int [][] tiles = layer.tiles;
		
		if (layer.enableAlpha) {
			spriteBatch.enableBlending();
		} else {
			spriteBatch.disableBlending();
		}

		for (int y = startY; y < endY; y++) {
			for (int x = startX; x < endX; x++) {
				if (tiles[y][x] != -1) {
					spriteBatch.draw(
							mapData.textureRegions[tiles[y][x]], 
							x*TILE_WIDTH, 
							y*TILE_HEIGHT);
				}
			}
		}
	}
	
	public void update(float deltaTime) {
		startX = Math.max((int)((camPos.x - MyGame.SCREEN_HALF_WIDTH)/TILE_WIDTH)-1, 0);
		startY = Math.max((int)((camPos.y - MyGame.SCREEN_HALF_HEIGHT)/(TILE_HEIGHT)-1), 0);
		endX = Math.min(startX + MyGame.SCREEN_WIDTH/TILE_WIDTH+3, mapData.tilesInX);
		endY = Math.min(startY + MyGame.SCREEN_HEIGHT/TILE_HEIGHT+3, mapData.tilesInY);
	}

	/**
	 * Dispose of all the resources
	 */
	public void dispose() {
		
	}
	
}
