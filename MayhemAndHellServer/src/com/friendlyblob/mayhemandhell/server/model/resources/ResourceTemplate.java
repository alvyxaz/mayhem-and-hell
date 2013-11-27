package com.friendlyblob.mayhemandhell.server.model.resources;

import com.friendlyblob.mayhemandhell.server.model.items.Item;

public class ResourceTemplate {
	private String name;
	private int id;
	private Item item;
	private String icon;
	private boolean limited;
	private int regenerationTime;
	private int gatherTime;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public boolean isLimited() {
		return limited;
	}
	public void setLimited(boolean limited) {
		this.limited = limited;
	}
	public int getRegenerationTime() {
		return regenerationTime;
	}
	public void setRegenerationTime(int regenerationTime) {
		this.regenerationTime = regenerationTime;
	}
	public int getGatherTime() {
		return gatherTime;
	}
	public void setGatherTime(int gatherTime) {
		this.gatherTime = gatherTime;
	}
	
}
