package com.friendlyblob.mayhemandhell.client.screens;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.XmlReader;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.entities.EnvironmentObject;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.gameworld.Map;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class ZoneLoadingScreen extends BaseScreen{

	private Texture px;
	
	public ZoneLoadingScreen(MyGame game, String title) {
		super(game);
		
		Assets.manager.load("textures/debugging/px.png", Texture.class);
		
		int[][] tempMap = null;
		int[][] tempCollisionMap = null;
	    XmlReader xmlReader = new XmlReader();
	      
	      
	      FileHandle file = Gdx.files.internal("./data/zones/" + title + ".xml");
	      
	      XmlReader.Element root = null;
	      
			try {
				root = xmlReader.parse(file);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		  XmlReader.Element zone = root.getChildByName("zone");
		  int regionWidth = zone.getIntAttribute("regionWidth");
		  int regionHeight = zone.getIntAttribute("regionHeight");

	      XmlReader.Element data = root.getChildByName("map");
		  if (regionWidth != 0 || regionHeight != 0) {
		      XmlReader.Element tmpChild = null;
			  
			  tempMap = new int[regionHeight][regionWidth];
			  tempCollisionMap = new int[regionHeight][regionWidth];
		      
		      for (int i = 0; i < regionHeight; i++) {
				for (int j = 0; j < regionWidth; j++) {
					tmpChild = data.getChild(i*regionHeight+j);
					
					tempMap[i][j] = tmpChild.getIntAttribute("gid");
					tempCollisionMap[i][j] = tmpChild.getIntAttribute("collision");
				}
			  }  
		  } else {
			  tempMap = new int[100][50];
			  tempCollisionMap = new int[100][50];
			  
			  for (int i = 0; i < tempMap.length; i++) {
				for (int j = 0; j < tempMap[i].length; j++) {
					tempMap[i][j] = data.getChild(0).getIntAttribute("gid");
				}
			}
		  }
		  
		  Map.load(tempMap, tempCollisionMap);

		  // map loaded
//
//		  XmlReader.Element objects = root.getChildByName("objects");
//		 
//		 ArrayList<EnvironmentObject> objs = GameWorld.getInstance().getObjects();
//		 objs.clear();
//
//		  for (int i = 0; i < objects.getChildCount(); i++) {
//			  XmlReader.Element object = objects.getChild(i);
//			  
//			  objs.add(new EnvironmentObject(object.getIntAttribute("x"), object.getIntAttribute("y"), object.getIntAttribute("type")));
//		  }
		  
		  // objects loaded
//		  XmlReader.Element resources = root.getChildByName("resources");
//		  XmlReader.Element tileSheet = resources.getChildByName("image");
//		  XmlReader.Element objectTypes = resources.getChildByName("objects");
//		  
//		  HashMap<String, String> environmentObjects = new HashMap<String, String>();
//		  
//		  Assets.manager.load(tileSheet.getAttribute("texture"), Texture.class);
//		  for (int i = 0; i < objectTypes.getChildCount(); i++) {
//			  XmlReader.Element o = objectTypes.getChild(i);
//			  
//			environmentObjects.put(o.getAttribute("id"), o.getAttribute("texture"));
//			  
//			Assets.manager.load(o.getAttribute("texture"), Texture.class);
//		  }
		  
		  System.out.println("mainIsland loaded");
		  
	}

	@Override
	public void draw(float deltaTime) {
		if(Assets.manager.update()){
			if(game.screenGame == null)
				game.screenGame = new GameScreen(game);
			game.setScreen(game.screenGame);
		}
		
		// Between 0 and 1
		float progress = Assets.manager.getProgress();
	}

	@Override
	public void update(float deltaTime) {
		
	}

	@Override
	public void prepare() {
		
	}

}
