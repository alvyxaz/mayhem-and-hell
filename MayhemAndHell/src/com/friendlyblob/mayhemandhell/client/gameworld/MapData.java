package com.friendlyblob.mayhemandhell.client.gameworld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

/**
 * Represents data that is loaded from a text file of map
 * @author Alvys
 *
 */
public class MapData {

	public int mapId;
	
	// Grid of regions
	private int regionsX = 6;
	private int regionsY = 6;
	
	// Grid of tiles in region;
	private int tilesInRegionX = 25;
	private int tilesInRegionY = 15;
	
	private HashMap<Integer,AssetDescriptor> requiredResources;
	
	public TileMapLayer[] layersBelow;
	public TileMapLayer[] layersAbove;
	
	public TextureRegion[] textureRegions;
	
	public int highestResourceId;
	
	public int tilesInX;
	public int tilesInY;
	
	public boolean drawMetaLayer = false;
	
	public MapData(int mapId) {
		this.mapId = mapId;
		requiredResources = new HashMap<Integer,AssetDescriptor>();
		readFromFile();
	}
	
	public void setRequiredResources(String [] requiredTextures) {
		this.requiredResources = requiredResources;
	}

	public HashMap<Integer, AssetDescriptor> getRequiredResources() {
		return requiredResources;
	}
	
	/**
	 * Adds a resource to the queue
	 * @param resource
	 * @param types
	 */
	public void addRequiredResource(int assetId, AssetDescriptor asset) {
		requiredResources.put(assetId, asset);
		
		if (highestResourceId < assetId) {
			highestResourceId = assetId;
		}
	}
	
	public void readFromFile() {
		XmlReader xmlReader = new XmlReader();
		FileHandle file = Gdx.files.internal("data/zones/" + mapId + ".tmx");
		
		Element metaLayer = null;
		
		if (!file.exists()) {
			// TODO send request to server and download a map
		}
		
		XmlReader.Element root = null;
		
		try {
			root = xmlReader.parse(file);
		} catch (IOException e1) {
			// TODO Error handling
			e1.printStackTrace();
		}
		
		/*-----------------------------
		 * Reading map size
		 */
		tilesInX = root.getIntAttribute("width", -1);
		tilesInY = root.getIntAttribute("height", -1);
		if (tilesInX == -1 || tilesInY == -1) {
			System.out.println("Could not find map width and height attributes");
			// TODO Error handling
		}
		
		/*-----------------------------
		 * Reading map porperties
		 */
		Element mapProperties = root.getChildByName("properties");
		if (mapProperties != null) {
			for (int i = 0; i < mapProperties.getChildCount(); i++) {
				String property = mapProperties.getChild(i).getAttribute("name");
				if (property.equals("regionsX")) {
					regionsX = mapProperties.getChild(i).getIntAttribute("value", regionsX);
				} else if (property.equals("regionsY")) {
					regionsY = mapProperties.getChild(i).getIntAttribute("value", regionsY);
				}
			}
		}
		
		/*-----------------------------
		 * Reading tilesets
		 */
		Array<Element> tilesets = root.getChildrenByName("tileset");
		for (int i = 0; i < tilesets.size; i++) {
			addRequiredResource(i, 
					new AssetDescriptor<Texture>(
							"textures/tiles/"+ tilesets.get(i).getChild(0).getAttribute("source"), 
							Texture.class, null)
			);
		}
		
		/*-----------------------------
		 * Reading layers
		 */
		Array<Element> layers = root.getChildrenByName("layer");
		int layersBelow = 0;
		int layersAbove = 0;
		
		// Counting above and below layers (feel free to change if there's a better way)
		// If you have added new properties or etc, don't parse them here, because 
		// this loop is just for counting a number of layers. New properties can
		// and should be analyzed in a loop below
		for (int i = 0; i < layers.size; i++) {
			if (!layers.get(i).getAttribute("name").equals("Meta") || drawMetaLayer){
				Element tempLayerProperties = layers.get(i).getChildByName("properties");
				for (int j = 0; j < tempLayerProperties.getChildCount(); j++) {
					String property = tempLayerProperties.getChild(j).getAttribute("name");
					if (property.equals("above")) {
						if (tempLayerProperties.getChild(j).getAttribute("value").equals("true")) {
							layersAbove ++;
						} else {
							layersBelow ++;
						}
					} 
				}
			} else {
				metaLayer = layers.get(i);
			}
		}
		
		int layerAboveIndex = 0;
		int layerBelowIndex = 0;
		
		this.layersAbove = new TileMapLayer[layersAbove];
		this.layersBelow = new TileMapLayer[layersBelow];
		
		// Counting above and below layers
		for (int i = 0; i < layers.size; i++) {
			if (!layers.get(i).getAttribute("name").equals("Meta") || drawMetaLayer){
				TileMapLayer layer = new TileMapLayer(tilesInX, tilesInY);
				
				Element layerElement = layers.get(i);
				
				// Analyzing layer properties
				Element tempLayerProperties = layerElement.getChildByName("properties");
				for (int j = 0; j < tempLayerProperties.getChildCount(); j++) {
					String property = tempLayerProperties.getChild(j).getAttribute("name");
					
					if (property.equals("above")) {
						// Assigning a layer to either below or above layers collection
						if (tempLayerProperties.getChild(j).getAttribute("value").equals("true")) {
							// Above
							this.layersAbove[layerAboveIndex] = layer;
							layerAboveIndex++;
						} else {
							// Below
							this.layersBelow[layerBelowIndex] = layer;
							layerBelowIndex++;
						}
					} else if (property.equals("enableAlpha")) {
						if (tempLayerProperties.getChild(j).getAttribute("value").equals("true")) {
							layer.enableAlpha = true;
						}
					}
				}
				
				layer.fillTileData(layerElement.getChildByName("data").getText());
			}
		}
		
		if (metaLayer != null) {
			TileMapLayer layer = new TileMapLayer(tilesInX, tilesInY);
		}
		
	}
	
	/**
	 * TODO check if resource is tileset
	 */
	public void initializeTextureRegions() {
		int textureIndex = 0;
		int textureCount = 0;
		
		// Calculating a number of texture regions
		for (Entry<Integer,AssetDescriptor> entry : requiredResources.entrySet()) {
			if (entry.getValue().type == Texture.class) {
				Texture texture = Assets.manager.get(entry.getValue().fileName);
				textureCount += texture.getWidth()/Map.TILE_WIDTH * texture.getHeight()/Map.TILE_HEIGHT;
			}
		}
		
		textureRegions = new TextureRegion[textureCount];
		
		// Filling texture regions with data
		for (Entry<Integer,AssetDescriptor> entry : requiredResources.entrySet()) {
			// Checking if resource is texture
			if (entry.getValue().type != Texture.class) {
				continue;
			}
			
			// Retrieve texture
			Texture texture = Assets.manager.get(entry.getValue().fileName);
			
			// Create Texture Regions
			int resourceId = entry.getKey();
			int tilesInX = texture.getWidth()/Map.TILE_WIDTH;
			int tilesInY = texture.getHeight()/Map.TILE_HEIGHT;
			int regionCount = tilesInX * tilesInY;
		
			for (int i = 0; i < regionCount; i++) {
				textureRegions[textureIndex++] = new TextureRegion(
						texture, 
						(i % tilesInX)*Map.TILE_WIDTH, 
						(i/tilesInX)*Map.TILE_HEIGHT, 
						Map.TILE_WIDTH, 
						Map.TILE_HEIGHT);
			}
		}
	}
	
	/**
	 * Represents a single map layer
	 * @author Alvys
	 *
	 */
	public static class TileMapLayer{
		// Whether or not alpha blending should be enabled
		public boolean enableAlpha = true;
		
		public int [][] tiles; // Tiles of the layer
		
		public TileMapLayer(int width, int height) {
			tiles = new int[height][width];
		}
		
		/**
		 * Fills tile texture data from resource
		 * @param data string of data in csv encoding
		 */
		public void fillTileData(String data) {
			String [] lines = data.split("\n");
			
			// Going backwards, because tiles from source go from top to bottom
			for (int i = lines.length-1; i >= 0; i--) {
				String [] strTiles = lines[i].split(",");
				
				for (int x = 0; x < strTiles.length; x++) {
					try {
						this.tiles[this.tiles.length-1-i][x] = Integer.parseInt(strTiles[x])-1;

					} catch (Exception e) {
						
					}
				}
			}
		}
	}
	
}
