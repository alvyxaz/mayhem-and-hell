package com.friendlyblob.mayhemandhell.server.model;

import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;

public class ZoneTemplate {

	private int zoneId;
	
	private String name;
	
	private int tileWidth = 16;		// Tile width in pixels
	private int tileHeight = 16;	// Tile height in pixels
	
	private int regionsCountX = 4;		// Zone width in regions
	private int regionsCountY = 4; 		// Zone height in regions
	
	private int regionWidth = 10;	// Region width in tiles
	private int regionHeight = 10;	// Region height in tiles
	
	private StatsSet set;
	
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
	
	
	
}
