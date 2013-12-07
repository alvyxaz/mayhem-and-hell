package com.friendlyblob.mayhemandhell.client.entities.gui.inventory;

public class InventoryItem extends SlotObject  {
	private static final int SIZE = 24;
	
	public int itemId;
	public String name;
	public String description;
	
	public InventoryItem(int itemId) {
		super(SIZE, SIZE);
		
		this.itemId = itemId;
		// TODO Auto-generated constructor stub
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
