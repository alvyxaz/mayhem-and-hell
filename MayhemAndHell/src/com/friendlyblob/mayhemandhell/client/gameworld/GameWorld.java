package com.friendlyblob.mayhemandhell.client.gameworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.entities.EnvironmentObject;
import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.GameObject;
import com.friendlyblob.mayhemandhell.client.entities.Player;
import com.friendlyblob.mayhemandhell.client.entities.TargetMark;
import com.friendlyblob.mayhemandhell.client.mapeditor.MapEditor;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RequestAction;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RequestTarget;

public class GameWorld {
	public static GameWorld instance;
	
	/*-------------------------------------
	 * Entities
	 */
	private Map map;
	public Player player;
	
	public MyGame game;
	
	public ConcurrentHashMap<Integer,GameCharacter> characters = new ConcurrentHashMap<Integer,GameCharacter>();
	public ConcurrentHashMap<Integer,GameObject> gameObjects = new ConcurrentHashMap<Integer,GameObject>();
	
	private static ArrayList<EnvironmentObject> environmentObjects = new ArrayList<EnvironmentObject>();
	
	public static HashMap<String, String> environmentObjectTypes = new HashMap<String, String>();
	
	public TargetMark targetMark;
	
	/*-------------------------------------
	 * Camera
	 */
	private OrthographicCamera worldCam;
	private Vector3 camPos;
	
	public GameWorld() {
		
		/*--------------------------------
		 * World camera setup
		 */
		worldCam = new OrthographicCamera(MyGame.SCREEN_WIDTH, MyGame.SCREEN_HEIGHT);
		worldCam.translate(MyGame.SCREEN_WIDTH/2, MyGame.SCREEN_HEIGHT/2);
		worldCam.update();
		camPos = worldCam.position;
		
		/*
		 * Entities initialization
		 */
		player = new Player(0, 100, 100); // TODO do not initialize until login is successful
		targetMark = new TargetMark();
	}
	
	public void putCharacter(GameCharacter character) {
		characters.put(character.objectId, character);
		gameObjects.put(character.objectId, character);
	}
	
	public void removeCharacter(int id) {
		characters.remove(id);
		gameObjects.remove(id);
	}
	
	public void putObject(GameObject object) {
		gameObjects.put(object.objectId, object);
	}
	
	public void removeObject(int id) {
		gameObjects.remove(id);
	}
	
	public boolean characterExists(int id) {
		return characters.containsKey(id);
	}
	
	public GameCharacter getCharacter(int id) {
		return characters.get(id);
	}
	
	public void update(float deltaTime) {
		player.update(deltaTime);
		
		// TODO optimize to avoid iterators (Make sure FastMap uses them first)
		for (GameCharacter character : characters.values()) {
			character.update(deltaTime);
		}
		
		if (!MapEditor.enabled){
			cameraFollowPlayer(deltaTime);
		}
		
		map.update(deltaTime);
	}
	
	/**
	 * Updates input that is related to the world (not gui),
	 * like walking or targeting an object.
	 */
	public void updateWorldInput() {
		if (Input.isReleasing()) {
			
			GameObject gameObject = getObjectAt(toWorldX(Input.getX()), toWorldY(Input.getY()));
			
			if (gameObject != null) {
				if (gameObject == targetMark.getTarget()) {
					MyGame.connection.sendPacket(new RequestAction(0));
				} else {
					MyGame.connection.sendPacket(new RequestTarget(gameObject.objectId));
				}
			} else {
				// Movement should be the last case
				getPlayer().requestMovementDestination(toWorldX(Input.getX()), toWorldY(Input.getY()));
			}
		}
	}
	
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.setProjectionMatrix(worldCam.combined);

		map.drawBelow(spriteBatch);

		for (EnvironmentObject go : environmentObjects) {
			go.draw(spriteBatch);
		}
		
		targetMark.draw(spriteBatch);
		
		for (GameObject go : gameObjects.values()) {
			go.draw(spriteBatch);
		}
		
		for (GameCharacter character : characters.values()) {
			character.draw(spriteBatch);
		}
		
		player.draw(spriteBatch);
		
		
		map.drawAbove(spriteBatch);
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
		
		// BOUNDS
		
		// Bottom
		if (worldCam.position.y - worldCam.viewportHeight/2 < 0) {
			worldCam.position.y = worldCam.viewportHeight/2;
		}
		
		// Left
		if (worldCam.position.x - worldCam.viewportWidth/2 < 0) {
			worldCam.position.x = worldCam.viewportWidth/2;
		}
		
		worldCam.update();
	}
	
	public void setGame(MyGame game) {
		this.game = game;
	}
	
	public Map getMap() {
		return map;
	}
	
	/**
	 * Creates a map from MapData
	 * @param data
	 */
	public void createMap(MapData data) {
		if (this.map != null) {
			this.map.dispose();
		}
		map = new Map(this, data);
	}
	
	public void setMap(Map map) {
		if (this.map != null) {
			this.map.dispose();
		}
		this.map = map;
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
		return (int)(x*worldCam.zoom + camPos.x-MyGame.SCREEN_HALF_WIDTH);
	}
	
	/*
	* Translate screen y coordinates to world y coordinate
	*/
	public int toWorldY(int y) {
		return (int)(y*worldCam.zoom + camPos.y-MyGame.SCREEN_HALF_HEIGHT);
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
	
	public OrthographicCamera getWorldCam() {
		return worldCam;
	}
	
}
