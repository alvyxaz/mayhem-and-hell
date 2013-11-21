package com.friendlyblob.mayhemandhell.client.animations;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.friendlyblob.mayhemandhell.client.animations.Animation.AnimationData;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class AnimationParser {

	private static HashMap<Integer, HashMap<String, AnimationData>> animationCollections;
	
	public static void loadAll() {
		animationCollections = new HashMap<Integer, HashMap<String, AnimationData>>();
		
		XmlReader reader = new XmlReader();
		try {
			Element charactersXml = reader
					.parse(Gdx.files.internal("data/animations/characters.xml"));
			for(int i = 0; i < charactersXml.getChildCount(); i++){
				Element character = charactersXml.getChild(i);

				HashMap<String, AnimationData> animations = new HashMap<String, AnimationData>();
				
				for (int j = 0; j < character.getChildCount(); j++) {
					parseAnimation(character.getChild(j), animations);
				}
				animationCollections.put(character.getIntAttribute("id"), animations);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, AnimationData> getCollection(int id) {
		return animationCollections.get(id);
	}
	
	public static void parseAnimation(Element xml, HashMap<String, AnimationData> collection) {
		AnimationData animation = new AnimationData();
		animation.type = xml.getAttribute("type");
		animation.looped = xml.getBooleanAttribute("looped", false);
		animation.duration = xml.getFloatAttribute("duration", 1);
		
		int frameWidth = xml.getIntAttribute("frameWidth", 32);
		int frameHeight = xml.getIntAttribute("frameHeight", 32);

		// TODO fix texture loading (should be without finishLoading)
		Assets.manager.load("textures/" + xml.getAttribute("file"), Texture.class);
		Assets.manager.finishLoading();
		Texture texture = Assets.manager.get("textures/" + xml.getAttribute("file"));
		
		animation.frames = new TextureRegion[xml.getChildCount()];
		animation.frameStartMarks = new float[xml.getChildCount()];
		
		// Loading frames
		for (int i = 0; i < xml.getChildCount(); i++) {
			animation.frames[i] = new TextureRegion(texture, 
					xml.getChild(i).getIntAttribute("startX"),
					xml.getChild(i).getIntAttribute("startY"),
					frameWidth,
					frameHeight);
			animation.frameStartMarks[i] = xml.getChild(i).getFloatAttribute("startMark");
		}
		
		collection.put(animation.type, animation);
	}
	
}
