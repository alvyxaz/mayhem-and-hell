package com.friendlyblob.mayhemandhell.client.gameworld;

import java.util.ArrayList;
import java.util.HashMap;

import javolution.util.FastMap;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.entities.EnvironmentObject;
import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.GameObject;
import com.friendlyblob.mayhemandhell.client.entities.Player;
import com.friendlyblob.mayhemandhell.client.entities.gui.TargetBar.TargetInfo;
import com.friendlyblob.mayhemandhell.client.mapeditor.MapEditor;
import com.friendlyblob.mayhemandhell.client.screens.BaseScreen;
import com.friendlyblob.mayhemandhell.client.screens.GameScreen;
import com.friendlyblob.mayhemandhell.client.screens.ZoneLoadingScreen;

public class GameWorld {
	public static GameWorld instance;
	
	/*-------------------------------------
	 * Entities
	 */
	private Map map;
	public Player player;
	
	public MyGame game;
	
	public FastMap<Integer,GameCharacter> characters = new FastMap<Integer,GameCharacter>().shared();
	public FastMap<Integer,GameObject> gameObjects = new FastMap<Integer,GameObject>().shared();
	
	private static ArrayList<EnvironmentObject> environmentObjects = new ArrayList<EnvironmentObject>();
	
	public static HashMap<String, String> environmentObjectTypes = new HashMap<String, String>();
	
	/*-------------------------------------
	 * Camera
	 */
	private OrthographicCamera worldCam;

	public GameWorld() {
		
		/*--------------------------------
		 * World camera setup
		 */
		worldCam = new OrthographicCamera(MyGame.SCREEN_WIDTH, MyGame.SCREEN_HEIGHT);
		worldCam.translate(MyGame.SCREEN_WIDTH/2, MyGame.SCREEN_HEIGHT/2);
		worldCam.update();
		
		/*
		 * Entities initialization
		 */
		map = Map.getInstance();
		map.setWorld(this);
		map.load(worldCam);

		player = new Player(0, 100, 100, (TiledMapTileLayer) map.getMap().getLayers().get(0)); // TODO do not initialize until login is successful

		// TODO cleanup temp code below.
		putCharacter(new GameCharacter(13362, 50, 90,(TiledMapTileLayer) map.getMap().getLayers().get(0)));
	}
	
	public void putCharacter(GameCharacter character) {
		characters.put(character.objectId, character);
		gameObjects.put(character.objectId, character);
	}
	
	public void removeCharacter(int id) {
		characters.remove(id);
		gameObjects.remove(id);
	}
	
	public boolean characterExists(int id) {
		return characters.containsKey(id);
	}
	
	public GameCharacter getCharacter(int id) {
		return characters.get(id);
	}
	
	public void update(float deltaTime) {
//		if (Input.isReleasing()) {
//			game.setScreen(new ZoneLoadingScreen(game, "mainIsland"));
//		}
		
		player.update(deltaTime);
		
		// TODO optimize to avoid iterators (Make sure FastMap uses them first)
		for (GameCharacter character : characters.values()) {
			character.update(deltaTime);
		}
		
		map.update(deltaTime);
		
		if (!MapEditor.enabled){
			cameraFollowPlayer(deltaTime);
		}
	}
	
	/**
	 * Updates input that is related to the world (not gui),
	 * like walking or targeting an object.
	 */
	public void updateWorldInput() {
		if (Input.isReleasing()) {
			
			GameObject gameObject = getObjectAt(toWorldX(Input.getX()), toWorldY(Input.getY()));
			
			if (gameObject != null) {
				// Clicked on an object, let's show a target bar
				// TODO check whether clicked on the object that is already targeted
				// And act accordingly (like use the first available action on double click)
				TargetInfo targetInfo = new TargetInfo();
				targetInfo.name = gameObject.name;
				game.screenGame.guiManager.targetBar.showTarget(targetInfo);
			} else {
				// Movement should be the last case
				getPlayer().requestMovementDestination(toWorldX(Input.getX()), toWorldY(Input.getY()));
			}
		}
	}
	
	public void draw(SpriteBatch spriteBatch) {
		map.draw(spriteBatch);
		
		
		spriteBatch.setProjectionMatrix(worldCam.combined);

		player.draw(spriteBatch);
		
		for (EnvironmentObject go : environmentObjects) {
			go.draw(spriteBatch);
		}
		
		// TODO optimize to avoid iterators (Make sure FastMap uses them first)
		for (GameCharacter character : characters.values()) {
			character.draw(spriteBatch);
		}
		
	}
	
	public void cameraFollowPlayer(float deltaTime){
		// X follow
		if (player.hitBox.x > worldCam.position.x + MyGame.SCREEN_WIDTH*0.15f) {
			worldCam.position.x += player.hitBox.x - (worldCam.position.x + MyGame.SCREEN_WIDTH*0.15f);
		} else if (player.hitBox.x < worldCam.position.x - MyGame.SCREEN_WIDTH*0.15f) {
			worldCam.position.x -= (worldCam.position.x - MyGame.SCREEN_WIDTH*0.15f) - player.hitBox.x;
		}
		
		// Y follow
		if (player.hitBox.y > worldCam.position.y + MyGame.SCREEN_HEIGHT*0.15f) {
			worldCam.position.y += player.hitBox.y - (worldCam.position.y + MyGame.SCREEN_HEIGHT*0.15f);
		} else if (player.hitBox.y < worldCam.position.y - MyGame.SCREEN_HEIGHT*0.15f) {
			worldCam.position.y -= (worldCam.position.y - MyGame.SCREEN_HEIGHT*0.15f) - player.hitBox.y;
		}
		
		worldCam.update();
	}
	
	public void setGame(MyGame game) {
		this.game = game;
	}
	
	public Map getMap() {
		return map;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public static ArrayList<EnvironmentObject> getObjects() {
		return environmentObjects;
	}
	
	/*
	* Translate screen x coordinates to world x coordinate
	*/
	public int toWorldX(int x) {
		return (int)(x*worldCam.zoom + map.getCamPos().x-MyGame.SCREEN_HALF_WIDTH);
	}
	
	/*
	* Translate screen y coordinates to world y coordinate
	*/
	public int toWorldY(int y) {
		return (int)(y*worldCam.zoom + map.getCamPos().y-MyGame.SCREEN_HALF_HEIGHT);
	}
	
	/**
	 * Gets a visible object that is at location x y
	 * @param x
	 * @param y
	 * @return GameObject at x y, or null if there's no object
	 */
	public GameObject getObjectAt(int x, int y) {
		
		for (GameObject object : gameObjects.values()) {
			if (object.hitBox.contains(x, y)) {
				return object;
			}
		}

		return null;
	}
	
	public static void initialize() {
		instance = new GameWorld();
	}
	
	public static GameWorld getInstance() {
		return instance;
	}
	
}
