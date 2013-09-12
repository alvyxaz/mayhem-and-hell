package com.friendlyblob.mayhemandhell.client.helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

//TODO prepare for android lifecycle (resume, dispose and etc.)
public class Assets {
	public static Assets assets;
	public static AssetManager manager;
	
	public static BitmapFont defaultFont = new BitmapFont();
	
	public static Texture px;
	
	public static void initialize(){
		assets = new Assets();
		manager = new AssetManager();
		Assets.manager.load("textures/debugging/px.png", Texture.class);
		manager.finishLoading();
		
		px = manager.get("textures/debugging/px.png", Texture.class);
	}
	
}
