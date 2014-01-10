package com.friendlyblob.mayhemandhell.server.model;

import java.util.ArrayList;
import java.util.Arrays;

import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;
import com.friendlyblob.mayhemandhell.server.utils.ObjectPosition;

public class ZoneTemplate {

	private int zoneId;
	
	private String name;
	
	private int mapWidth;
	private int mapHeight;
	
	private int tileWidth = 16;		// Tile width in pixels
	private int tileHeight = 16;	// Tile height in pixels
	
	private int regionsCountX = 4;	// Zone width in regions
	private int regionsCountY = 4; 	// Zone height in regions
	
	private int regionWidth = 20;	// Region width in tiles
	private int regionHeight = 20;	// Region height in tiles
	
	private StatsSet set;
	
	private Tile[] tiles;
	
	public ZoneTemplate(int mapWidth, int mapHeight) {
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		
		initializeMap();
	}
	
	public Tile[] getTiles() {
		return tiles;
	}
	
	public int xPositionOfTile(int tile) {
		return (tile%mapWidth)*tileWidth + tileWidth/2; 
	}
	
	public int yPositionOfTile(int tile) {
		return (tile/mapWidth)*tileHeight + tileHeight/2;
	}
	
	public void initializeMap() {
		tiles = new Tile[mapWidth*mapHeight];
		
		// Creating objects
		for(int i = 0; i < tiles.length; i++) {
			tiles[i] = new Tile(i);
		}
	}
	
	public void establishNodeConnections() {
		// Finding nodes
		for(int i = 0; i < tiles.length; i++) {
			int x = i % mapWidth;
			int y = (i / mapWidth);
			
			if (x > 0) { // LEFT NODE
				tiles[i].addNode(tiles[i-1]);
			}
			if (x < mapWidth-1) { // RIGHT NODE
				tiles[i].addNode(tiles[i+1]);
			}
			if (y > 0) { // BOTTOM NODE
				tiles[i].addNode(tiles[tileAtIndex(x, y-1)]);
			}
			if (y < mapHeight-1) { // TOP NODE
				tiles[i].addNode(tiles[tileAtIndex(x, y+1)]);
			}
		}
	}
	
	public int[] calculatePathBetween(int tileA, int tileB) {
		// If any of the tiles are invalid
		if (tileA < 0 || tileA >= tiles.length ||
				tileB < 0 || tileB >= tiles.length) {
			return null;
		}

		calculatePrev(tiles[tileA]);
		
		return getPathBetween(tileA, tileB);
	}
	
	/**
	 * Returns an inverted path between two tiles
	 * @param tileA
	 * @param tileB
	 * @return
	 */
	public int [] getPathBetween(int tileA, int tileB) {
		// If we're trying to go to collision point
		if (tiles[tileB].getType() == TileType.COLLISION) {
			return null;
		}

		int size = 0;
		
		int u = tileB;
		while (tiles[tileA].getPrev()[u] != -1) {
			u = tiles[tileA].getPrev()[u];
			size++;
		}
		
		if (size == 0) {
			return null;
		}
		
		int path[] = new int[size];
		
		u = tileB;
		path[--size] = tileB;	// Add the final position
		while (size > 0) {
			u = tiles[tileA].getPrev()[u];
			path[size - 1] = u;
			size--;
		}
		
		return path;
	}
	
	
	public int tileAtIndex(int x, int y) {
		return (y*mapWidth + x);
	}
	
	public int tileAtPosition(ObjectPosition position) {
		return (int)(position.getY()/tileHeight)*mapWidth + (int)(position.getX()/tileWidth);
	}
	
	public void calculatePrev(Tile source) {
		float dist[] = new float[mapWidth * mapHeight];
		int prev[] = new int[mapWidth * mapHeight];
		
		ArrayList<Tile> q = new ArrayList<Tile>();
		
		for(int i = 0; i < tiles.length; i++) {
			dist[i] = Integer.MAX_VALUE;
			prev[i] = -1;
			q.add(tiles[i]);
		}
		
		dist[source.getId()] = 0;
		while(!q.isEmpty()) {
			// Get lowest distance tile
			Tile u = q.get(0);
			for (Tile tile : q) {
				if (dist[tile.getId()] < dist[u.getId()]) {
					u = tile;
				}
			}
			q.remove(u);
			
			// Remaining tile inaccessible from source
			if (dist[u.getId()] >= Integer.MAX_VALUE) {
				break;
			}
			
			for(Tile v : u.getNodes()) {
				float alt = dist[u.getId()] + distanceBetweenTiles(u, v);
				if (alt < dist[v.getId()]) {
					dist[v.getId()] = alt;
					prev[v.getId()] = u.getId();
				}
			}
			source.setPrev(prev);
		}
	}
	
	public float distanceBetweenTiles(Tile tileA, Tile tileB) {
		int xA = tileA.getId() % mapWidth;
		int yA = tileA.getId() / mapWidth;
		int xB = tileB.getId() % mapWidth;
		int yB = tileB.getId() / mapWidth;
		
		return (xA - xB)*(xA - xB) + (yA - yB)*(yA - yB);
	}
	
	/**
	 * Saves a set of settings and uses some of their values
	 * to set as default template values
	 * @param set
	 */
	public void setStatsSet(StatsSet set) {
		this.set = set;
		tileWidth = set.getInt("tileWidth", 16);
		tileHeight = set.getInt("tileHeight", 16);
		
		regionsCountX = set.getInt("regionsCountX", 4);
		regionsCountY = set.getInt("regionsCountY", 4);
		
		regionWidth = set.getInt("regionWidth", 20);
		regionWidth = set.getInt("regionHeight", 20);
	}

	public StatsSet getStatsSet() {
		return set;
	}
	
	public int getMapWidth() {
		return mapWidth;
	}

	public void setMapWidth(int mapWidth) {
		this.mapWidth = mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public void setMapHeight(int mapHeight) {
		this.mapHeight = mapHeight;
	}

	/**
	 * @return the zoneId
	 */
	public int getZoneId() {
		return zoneId;
	}
	
	/**
	 * @param zoneId the zoneId to set
	 */
	public void setZoneId(int templateId) {
		this.zoneId = templateId;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the tileWidth
	 */
	public int getTileWidth() {
		return tileWidth;
	}
	
	/**
	 * @param tileWidth the tileWidth to set
	 */
	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}
	
	/**
	 * @return the tileHeight
	 */
	public int getTileHeight() {
		return tileHeight;
	}
	
	/**
	 * @param tileHeight the tileHeight to set
	 */
	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}
	
	/**
	 * @return the regionsCountX
	 */
	public int getRegionsCountX() {
		return regionsCountX;
	}
	
	/**
	 * @param regionsCountX the regionsCountX to set
	 */
	public void setRegionsCountX(int regionsCountX) {
		this.regionsCountX = regionsCountX;
	}
	
	/**
	 * @return the regionsCountY
	 */
	public int getRegionsCountY() {
		return regionsCountY;
	}
	
	/**
	 * @param regionsCountY the regionsCountY to set
	 */
	public void setRegionsCountY(int regionsCountY) {
		this.regionsCountY = regionsCountY;
	}
	
	/**
	 * @return the regionWidth
	 */
	public int getRegionWidth() {
		return regionWidth;
	}
	
	/**
	 * @param regionWidth the regionWidth to set
	 */
	public void setRegionWidth(int regionWidth) {
		this.regionWidth = regionWidth;
	}
	
	/**
	 * @return the regionHeight
	 */
	public int getRegionHeight() {
		return regionHeight;
	}
	
	/**
	 * @param regionHeight the regionHeight to set
	 */
	public void setRegionHeight(int regionHeight) {
		this.regionHeight = regionHeight;
	}
	
	
	public static class Tile {
		private TileType type;
		private Tile[] nodes;
		private int id;
		
		private int[] prev;
		
		public Tile(int id) {
			this.id = id;
			nodes = new Tile[0];
			type = TileType.NORMAL;
		}
		
		@Override
		public String toString() {
			String string = id + "," + type.ordinal() +","+ nodes.length + ";";
			for(int p : prev) {
				
			}
			return id + "," + type.ordinal() +","+ nodes.length;
		}
		
		public void setPrev(int[] prev) {
			this.prev = prev;
		}
		
		public int[] getPrev() {
			return prev;
		}
		
		public void addNode(Tile node) {
			if (node.getType() == TileType.COLLISION) {
				return;
			}
			Tile[] temp = Arrays.copyOf(nodes, nodes.length+1);
			temp[temp.length-1] = node;
			nodes = temp;
			temp = null;
		}
		
		public Tile[] getNodes() {
			return nodes;
		}
		
		public TileType getType() {
			return type;
		}
		
		public void setType(TileType type) {
			this.type = type;
		}
		
		public int getId() {
			return id;
		}
		
	}
	
	public static enum TileType {
		NORMAL,
		COLLISION;
		
		public static TileType fromOrdinal(int ordinal) {
			for(TileType type: TileType.values()) {
				if(type.ordinal() == ordinal) {
					return type;
				}
			}
			return NORMAL;
		}
	}
}
