package com.friendlyblob.mayhemandhell.client.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.entities.EnvironmentObject;
import com.friendlyblob.mayhemandhell.client.entities.GameObject;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.mapeditor.MapEditor;
import com.friendlyblob.mayhemandhell.client.mapeditor.MapEditorWindow;
import com.friendlyblob.mayhemandhell.client.mapeditor.ObjectEditorWindow;

public class Map {
	
	private Texture texture;
	private TextureRegion [] tileTextures;
	
	private Texture tree;
	
	private static int [][] tiles;
	
	public static final int TILE_WIDTH = 16;
	public static final int TILE_HEIGHT = 16;
	
	private OrthographicCamera worldCam;
	private Vector3 camPos;
	
	private static Vector2 tileTarget = new Vector2(); 
	
	private GameWorld world;
	
	public Map() {
		/*
		 * Loading main assets. 
		 * TODO Either load it at loading screen or load() method to be loaded for each level separately 
		 */
		Assets.manager.load("textures/tiles/tiles.png", Texture.class);
		Assets.manager.finishLoading();
		texture = Assets.manager.get("textures/tiles/tiles.png", Texture.class);
		/*
		 * Loading all textureRegions
		 */
		int xTextureCount = texture.getWidth()/TILE_WIDTH;
		int yTextureCount = texture.getHeight()/TILE_HEIGHT;
		
		tileTextures = new TextureRegion[xTextureCount*yTextureCount];
		
		for (int i = 0; i < tileTextures.length; i++){
			tileTextures[i] = new TextureRegion(texture, (i%xTextureCount)*TILE_WIDTH, (i/yTextureCount)*TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
		}	
	}
	
	public void draw(SpriteBatch spriteBatch) {
		
		// Calculating bounds
		int startX = Math.max((int)((camPos.x - MyGame.SCREEN_HALF_WIDTH)/TILE_WIDTH)-1, 0);
		int startY = Math.max((int)((camPos.y - MyGame.SCREEN_HALF_HEIGHT)/(TILE_HEIGHT)-1), 0);
		int endX = Math.min(startX + MyGame.SCREEN_WIDTH/TILE_WIDTH+3, tiles[0].length);
		int endY = Math.min(startY + MyGame.SCREEN_HEIGHT/TILE_HEIGHT+3, tiles.length);
		
		for (int y = startY; y < endY; y++){
			for(int x = startX; x < endX; x++){
				if (y < tiles.length && y >= 0 && x < tiles[y].length && x >= 0)
					spriteBatch.draw(tileTextures[tiles[y][x]], x*TILE_WIDTH, y * TILE_HEIGHT);
			}
		}

	}
	
	ObjectEditorWindow objectEditor = null;
	
	public void update(float deltaTime) {
		if (Gdx.input.isTouched()) {
			updateTileTarget(); // Target is not MapEditor specific
		}	
		
		// Toggle map editor with F1 key
		if (Input.keyReleased(Keys.F1)){
			MapEditor.toggle();
		}
		
		// Toggle object editor 
		if (Input.keyReleased(Keys.F2)) {
			if (objectEditor == null) {
				objectEditor = new ObjectEditorWindow();
			}
			
			objectEditor.toggle();
		}
		
		// Map editor related updates
		if (MapEditor.enabled){
			MapEditor.cameraUpdate(worldCam, deltaTime);
			
			switch (MapEditor.editorWindow.tabbedPane.getSelectedIndex()) {
				case 1:
					if (Gdx.input.isTouched()) {
						tiles[(int)tileTarget.y][(int)tileTarget.x] = MapEditor.selectedTileTexture;
					}
					break;
				case 2:
					if (Input.isReleasing()) {
					GameWorld.getObjects().add(new EnvironmentObject(world.toWorldX(Input.getX()), world.toWorldY(Input.getY()), MapEditor.selectedObject));
					}
					break;
				default:
			}
		}
	}
	
	public void updateTileTarget() {
		int worldX = (int)(Input.getX()*worldCam.zoom + camPos.x-MyGame.SCREEN_HALF_WIDTH);
		int worldY = (int)(Input.getY()*worldCam.zoom + camPos.y-MyGame.SCREEN_HALF_HEIGHT);
		

		// Preliminary values
		tileTarget.x = worldX / TILE_WIDTH;
		tileTarget.y = worldY / TILE_HEIGHT;
	}
	
	public void setWorld(GameWorld gameWorld) {
		world = gameWorld;
	}
	
	public Vector3 getCamPos() {
		return camPos;
	}
	
	public void load(OrthographicCamera worldCam) {
		this.worldCam = worldCam;
		camPos = worldCam.position;
		
		tiles = new int[50][50];

	}
	
	public static void load(int[][] map) {
		tiles = map;
	}
	
	public static int[][] getTiles() {
		return tiles;
	}
	
	public static Map getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public static class SingletonHolder {
		public static final Map INSTANCE = new Map();
	}
}
