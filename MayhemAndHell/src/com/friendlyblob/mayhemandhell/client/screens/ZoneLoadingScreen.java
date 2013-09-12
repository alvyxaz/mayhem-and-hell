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
		
		int[][] tempMap = null;
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
		  int regionWidth = Integer.parseInt(zone.getAttribute("regionWidth"));
		  int regionHeight = Integer.parseInt(zone.getAttribute("regionHeight"));

	      XmlReader.Element data = root.getChildByName("map");
		  if (regionWidth != 0 || regionHeight != 0) {
			  tempMap = new int[regionWidth][regionHeight];
		      
		      for (int i = 0; i < regionWidth; i++) {
				for (int j = 0; j < regionHeight; j++) {
					tempMap[i][j] = Integer.parseInt(data.getChild(i*regionHeight+j).getAttribute("gid"));
				}
			  }  
		  } else {
			  tempMap = new int[100][50];
			  
			  for (int i = 0; i < tempMap.length; i++) {
				for (int j = 0; j < tempMap[i].length; j++) {
					tempMap[i][j] = Integer.parseInt(data.getChild(0).getAttribute("gid"));
				}
			}
		  }
		  
		  Map.load(tempMap);
		  if (regionHeight == 0 || regionWidth == 0) {
			  Map.setInfinite(true);
		  }

		  // map loaded

		  XmlReader.Element objects = root.getChildByName("objects");
		 
		 ArrayList<EnvironmentObject> objs = GameWorld.getInstance().getObjects();
		 objs.clear();

		  for (int i = 0; i < objects.getChildCount(); i++) {
			  XmlReader.Element object = objects.getChild(i);
			  
			  objs.add(new EnvironmentObject(Integer.parseInt(object.getAttribute("x")), Integer.parseInt(object.getAttribute("y")), Integer.parseInt(object.getAttribute("type"))));
		  }
		  
		  // objects loaded
		  XmlReader.Element resources = root.getChildByName("resources");
		  XmlReader.Element tileSheet = resources.getChildByName("image");
		  XmlReader.Element objectTypes = resources.getChildByName("objects");
		  
		  HashMap<String, String> environmentObjects = new HashMap<String, String>();
		  
		  Assets.manager.load(tileSheet.getAttribute("texture"), Texture.class);
		  for (int i = 0; i < objectTypes.getChildCount(); i++) {
			  XmlReader.Element o = objectTypes.getChild(i);
			  
			environmentObjects.put(o.getAttribute("id"), o.getAttribute("texture"));
			  
			Assets.manager.load(o.getAttribute("texture"), Texture.class);
		  }
		  
		  
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
