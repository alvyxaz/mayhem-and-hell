package com.friendlyblob.mayhemandhell.client.screens;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.screens.TestScreen.Point;

public class TestScreen2 extends BaseScreen {

	private TextureRegion slotTexture;
	
	public int tileWidth = 16;
	public int tileHeight = 16;

	public int charWidth = 20;
	public int charHeight = 10;
	
	private int tilesInX = 50;
	private int tilesInY = 50;
	
	public Tile[] tiles;
	
	public Pixmap pixmap;
	public Texture gridTexture;
	public Texture collisionTexture;
	public Texture pathTexture;
	
	private BitmapFont font = new BitmapFont();
	
	public TestScreen2(MyGame game) {
		super(game);
		tiles = new Tile[tilesInX*tilesInY];
		
		// Creating objects
		for(int i = 0; i < tiles.length; i++) {
			tiles[i] = new Tile(i);
		}
		
		// Setting a collision
		tiles[tileAt(4,4)].setType(TileType.COLLISION);
		
		// Finding nodes
		for(int i = 0; i < tiles.length; i++) {
			int x = i % tilesInX;
			int y = (i / tilesInX);
			
			if (x > 0) { // LEFT NODE
				tiles[i].addNode(tiles[i-1]);
			}
			if (x < tilesInX-1) { // RIGHT NODE
				tiles[i].addNode(tiles[i+1]);
			}
			if (y > 0) { // BOTTOM NODE
				tiles[i].addNode(tiles[tileAt(x, y-1)]);
			}
			if (y < tilesInY-1) { // TOP NODE
				tiles[i].addNode(tiles[tileAt(x, y+1)]);
			}
		}
		
		for(int i = 0; i < tiles.length; i++) {
			calculatePrev(tiles[i]);
		}
		
		prepareArt();
		
		// PERFORMANCE
		Runtime runtime = Runtime.getRuntime();
		runtime.gc();
		long memory = runtime.totalMemory() - runtime.freeMemory();
		
		System.out.println(memory/ (1024L * 1024L));
		
		font.setScale(0.5f);
		font.setColor(1, 1, 1, 0.2f);
	}
	
	public void saveResults(Tile[] tiles, String file) {
		try {
			DataOutputStream os = new DataOutputStream(new FileOutputStream(file));
			os.writeInt(tiles.length);
			
			for(Tile tile : tiles) {
				os.writeInt(tile.getId());
				os.writeInt(tile.getType().ordinal());
				for(int p : tile.getPrev()) {
					os.writeInt(p);
				}
			}
			
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Tile[] readResults(String file) {
		Tile[] tiles = null;
		
		try {
			DataInputStream is = new DataInputStream(new FileInputStream(file));
			
			int length = is.readInt();
			tiles = new Tile[length];
			
			for(int i = 0; i < length; i++) {
				int id = is.readInt();
				int typeOrdinal = is.readInt();
				tiles[i] = new Tile(id);
				tiles[i].setType(TileType.fromOrdinal(typeOrdinal));
				
				int[] prev = new int[length];
				for(int j = 0; j < length; j++) {
					prev[j] = is.readInt();
				}
			}
			
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tiles;
	}
	
	public void calculatePrev(Tile source) {
		float dist[] = new float[tilesInX * tilesInY];
		int prev[] = new int[tilesInX * tilesInY];
		
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
	
	public void parseShortestPath(Tile tileA, Tile tileB) {
		int u = tileB.getId();
		while(tileA.getPrev()[u] != -1) {
			// append u to the beginning
			u = tileA.getPrev()[u];
			tiles[u].setType(TileType.PATH);
		}
	}
	
	public float distanceBetweenTiles(Tile tileA, Tile tileB) {
		int xA = tileA.getId() % tilesInX;
		int yA = tileA.getId() / tilesInX;
		int xB = tileB.getId() % tilesInX;
		int yB = tileB.getId() / tilesInX;
		
		return (xA - xB)*(xA - xB) + (yA - yB)*(yA - yB);
	}

	@Override
	public void draw(float deltaTime) {
		spriteBatch.begin();
		
		for(int i = 0; i < tilesInY; i++) {
			for(int j = 0; j < tilesInX; j++) {
				switch(tiles[tileAt(j,i)].getType()) {
				case GRID:
					spriteBatch.draw(gridTexture, j*tileWidth, i*tileHeight);
					break;
				case COLLISION:
					spriteBatch.draw(collisionTexture, j*tileWidth, i*tileHeight);
					break;
				case PATH:
					spriteBatch.draw(pathTexture, j*tileWidth, i*tileHeight);
					break;
				}
				font.drawWrapped(spriteBatch, tileAt(j, i) + "" , j*tileWidth+ 2, i*tileHeight + 9, tileWidth);
			}
		}
		spriteBatch.end();
	}
	
	public int tileAt(int x, int y) {
		return (y*tilesInX + x);
	}
	
	public void prepareArt() {
		pixmap = new Pixmap(tileWidth, tileHeight, Pixmap.Format.RGBA8888);
		
		pixmap.setColor(Color.DARK_GRAY);
		pixmap.drawLine(0, 0, 0, tileHeight);
		pixmap.drawLine(0, 0, tileWidth, 0);

		gridTexture = new Texture(pixmap);
		
		pixmap = new Pixmap(tileWidth, tileHeight, Pixmap.Format.RGBA8888);
		pixmap.setColor(0.5f, 0, 0, 1f);
		pixmap.fillRectangle(0, 0, tileWidth, tileHeight);
		collisionTexture = new Texture(pixmap);
		
		pixmap = new Pixmap(tileWidth, tileHeight, Pixmap.Format.RGBA8888);
		pixmap.setColor(0, 1f, 0, 0.5f);
		pixmap.fillRectangle(0, 0, tileWidth, tileHeight);
		pathTexture = new Texture(pixmap);
	}

	@Override
	public void update(float deltaTime) {
		if (Gdx.input.isTouched()) {
			for(Tile tile: tiles) {
				if(tile.getType() == TileType.PATH) {
					tile.setType(TileType.GRID);
				}
			}
			
			int x = Input.getX()/tileWidth;
			int y = Input.getY()/tileHeight;
			parseShortestPath(tiles[0], tiles[y*tilesInX +x]);
		}
	}

	@Override
	public void prepare() {
		
	}

	public static class Tile {
		private TileType type;
		private Tile[] nodes;
		private int id;
		
		private int[] prev;
		
		public Tile(int id) {
			this.id = id;
			nodes = new Tile[0];
			type = TileType.GRID;
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
		GRID,
		COLLISION,
		PATH;
		
		public static TileType fromOrdinal(int ordinal) {
			for(TileType type: TileType.values()) {
				if(type.ordinal() == ordinal) {
					return type;
				}
			}
			return GRID;
		}
	}
}
