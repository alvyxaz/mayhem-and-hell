package com.friendlyblob.mayhemandhell.client.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

//TODO prepare for android lifecycle (resume, dispose and etc.)
public class Assets {
	public static Assets assets;
	public static AssetManager manager;
	
	public static BitmapFont defaultFont;
	
	public static Texture px;
	
	public static void initialize(){
		assets = new Assets();
		manager = new AssetManager();
		
		// Loading critical resources
		manager.load("textures/debugging/px.png", Texture.class);
		manager.load("fonts/minecraftia.fnt", BitmapFont.class);
		
		// Making sure critical resources are loaded before going further
		manager.finishLoading();
		
		defaultFont = manager.get("fonts/minecraftia.fnt", BitmapFont.class);
		px = manager.get("textures/debugging/px.png", Texture.class);
	}
	
	// TODO somehow separate different types of textures?
	public static TextureRegion getTextureRegion(String url) {
		return manager.get("textures/textures.atlas", TextureAtlas.class).findRegion(url);
	}
	public static NinePatch getNinePatch(String url) {
		return manager.get("textures/textures.atlas", TextureAtlas.class).createPatch(url);
	}
	
}
