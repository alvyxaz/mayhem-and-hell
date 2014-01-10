package com.friendlyblob.mayhemandhell.client;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class TexturePackerRun {
	public static void main (String[] args) {
		Settings settings = new Settings();
		
		// Game textures
		TexturePacker2.process(settings, "textures/atlas", "../MayhemAndHellAndroid/assets/textures/atlas", "textures");
	
		// UI
		TexturePacker2.process(settings, "C:/Users/Alvys/Desktop/skin", "../MayhemAndHellAndroid/assets/data/ui2", "uiskin");
	}
}
