package com.friendlyblob.mayhemandhell.client;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.helpers.Achievements;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.Connection;
import com.friendlyblob.mayhemandhell.client.network.PacketHandler;
import com.friendlyblob.mayhemandhell.client.network.packets.client.ClientVersion;
import com.friendlyblob.mayhemandhell.client.screens.GameScreen;
import com.friendlyblob.mayhemandhell.client.screens.LoadingScreen;
import com.friendlyblob.mayhemandhell.client.screens.TestScreen;
import com.friendlyblob.mayhemandhell.client.screens.ZoneLoadingScreen;

public class MyGame extends Game implements ApplicationListener {
	public static int SCREEN_WIDTH = 400;
	public static int SCREEN_HEIGHT = 240;
	public static int SCREEN_HALF_WIDTH;
	public static int SCREEN_HALF_HEIGHT;
	public static Rectangle SCREEN_RECTANGLE;
	public static boolean isAndroid;
	public static Random random;
	
	public static Preferences preferences;
	
	public static boolean mute;
	
	public GoogleInterface google;
	public static AdsInterface ads;
	
	public ActionResolver actionResolver;
	
	// Screens
	public LoadingScreen screenLoading;
	public ZoneLoadingScreen screenZoneLoading;
	public GameScreen screenGame;
	
	public static Connection connection;
	
	public MyGame(GoogleInterface google, ActionResolver actionResolver){
		this.google = google;
		this.actionResolver = actionResolver; 
		random = new Random();
	}
	
    public void create () {
    	// Preferences setup
    	preferences = Gdx.app.getPreferences("prefs");
    	mute = preferences.getBoolean("mute", false);
    	
    	// Preparing OpenGL viewport
    	calculateScreenSize();
		Gdx.graphics.getGLCommon().glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Preparing helpers
		Input.initialize();
		Assets.initialize();
		Achievements.initialize(google);

		isAndroid = Gdx.app.getType() == ApplicationType.Android;
		
		// Initializing screens;
		screenLoading = new LoadingScreen(this);
		
		// Setting first screen to render
		setScreen(screenLoading);
    }
    
    /**
     * Prepares screens that should be ready after loader is done
     * with loading main resources
     */
    public void prepareScreens() {
    	screenZoneLoading = new ZoneLoadingScreen(this, "mainIsland");
    	screenGame = new GameScreen(this);
    }

    public void connectToServer() {
    	try {
//			MyGame.connection = new Connection(new PacketHandler(), "localhost", 7777);
//			MyGame.connection.game = this;
//			MyGame.connection.start();
//			MyGame.connection.sendPacket(new ClientVersion(5));
		} catch (Exception e){
			System.out.println();
		}
    }
    
    public void render () {
    	Input.update();
		getScreen().render(Gdx.graphics.getDeltaTime());
    }
    
    /*
     * Dynamically calculates viewport size, according to the window ratio.
     */
    public void calculateScreenSize() {
		float ratio = Gdx.graphics.getHeight()
				/ (float) Gdx.graphics.getWidth();
		
		float difference = Gdx.graphics.getWidth()/(float)SCREEN_WIDTH;
		float mismatch = difference - (int) difference;
		float scale = ((int) difference) >= 1 ? (int) difference : mismatch; 
		
		if(mismatch <= 0.3f){
			SCREEN_WIDTH = (int)(Gdx.graphics.getWidth()/scale);
			SCREEN_HEIGHT= (int)(Gdx.graphics.getHeight()/scale);
		} else if(mismatch > 0.7f){
			scale += 1;
			SCREEN_WIDTH = (int)(Gdx.graphics.getWidth() / scale);
			SCREEN_HEIGHT= (int)(Gdx.graphics.getHeight()/scale);
		} else {
			int minSize = SCREEN_WIDTH;
			SCREEN_WIDTH = (minSize);
			SCREEN_HEIGHT = (int) (minSize * ratio);
		}
		
		SCREEN_HALF_WIDTH = SCREEN_WIDTH / 2;
		SCREEN_HALF_HEIGHT = SCREEN_HEIGHT / 2;

		SCREEN_RECTANGLE = new Rectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
	}
    
    public void resize (int width, int height) {
    	
    }

    public void pause () {
    }

    public void resume () {
    	this.getScreen().resume();
    }

    public void dispose () {
    	Assets.manager.clear();
    }
}
