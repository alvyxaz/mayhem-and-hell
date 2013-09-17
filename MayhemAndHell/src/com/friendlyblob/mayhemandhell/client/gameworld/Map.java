package com.friendlyblob.mayhemandhell.client.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.entities.EnvironmentObject;
import com.friendlyblob.mayhemandhell.client.entities.GameObject;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.mapeditor.MapEditor;
import com.friendlyblob.mayhemandhell.client.mapeditor.MapEditorWindow;
import com.friendlyblob.mayhemandhell.client.mapeditor.ObjectEditorWindow;

public class Map {
	private static TextureRegion[][] splitTiles;
	private static int [][] collisions;
	
	public static final int TILE_WIDTH = 16;
	public static final int TILE_HEIGHT = 16;
	
	public static final int DEFAULT_MAP_WIDTH = 50;
	public static final int DEFAULT_MAP_HEIGHT = 50;
	
	private OrthographicCamera worldCam;
	private Vector3 camPos;
	
	private static Vector2 tileTarget = new Vector2(); 
	
	private GameWorld world;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMap map;
	
	// Object editor window
	ObjectEditorWindow objectEditor = null;
	
	public Map() {
		/*
		 * Loading main assets. 
		 * TODO Either load it at loading screen or load() method to be loaded for each level separately 
		 */
		
		Assets.manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		Assets.manager.load("./data/zones/mainIsland.tmx", TiledMap.class);
		Assets.manager.finishLoading();
		
		map = Assets.manager.get("./data/zones/mainIsland.tmx", TiledMap.class);
		Texture tiles = Assets.manager.get("./textures/tiles/tiles.png", Texture.class);
		splitTiles = TextureRegion.split(tiles, TILE_WIDTH, TILE_HEIGHT);

		renderer = new OrthogonalTiledMapRenderer(map);
	}
	
	public void draw(SpriteBatch spriteBatch) {
		renderer.setView(worldCam);
		renderer.render();
	}
	
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
					if (Input.isReleasing()) {
						if (MapEditor.editorWindow.collisionModeButton.isSelected()) {
							collisions[(int)tileTarget.x][(int)tileTarget.y] = Math.abs(collisions[(int)tileTarget.x][(int)tileTarget.y] - 1);
						}
					}
					
					if (Gdx.input.isTouched()) {
						if (!MapEditor.editorWindow.collisionModeButton.isSelected()) {
//							tiles[(int)tileTarget.x][(int)tileTarget.y] = MapEditor.selectedTileTexture;
						} 
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
		tileTarget.x = Math.max(worldX / TILE_WIDTH, 0);
		tileTarget.y = Math.max(worldY / TILE_HEIGHT, 0);
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
	}
	
	public static void load(int[][] map, int[][] c) {
//		tiles = map;
		collisions = c;
	}
	
	public TiledMap getMap() {
		return map;
	}

	
	public static int[][] getCollisionMap() {
		return collisions;
	}
	
	public static Map getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public static class SingletonHolder {
		public static final Map INSTANCE = new Map();
	}
}
