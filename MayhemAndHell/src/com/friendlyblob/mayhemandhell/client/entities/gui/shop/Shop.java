package com.friendlyblob.mayhemandhell.client.entities.gui.shop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.friendlyblob.mayhemandhell.client.entities.gui.GuiWindow;

public class Shop extends GuiWindow {

	private int visibleItems = 3;

	private ShopListItem [] itemList;
	
	// TODO: item info secondary window
	
	public Shop() {
		super();
		this.setTitle("Shop");
		
		setWindowSize(ShopListItem.WIDTH, visibleItems*ShopListItem.HEIGHT);
		
		visible = false;
	}
	
	@Override 
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
		
		float baseX, baseY;
		for (int i = 0; i < itemList.length; i++) {
			baseX = box.x + 5;
			baseY = box.y + (visibleItems - i - 1)*(ShopListItem.HEIGHT + 2) - 2;
			itemList[i].setPosition(baseX, baseY);
			itemList[i].draw(spriteBatch);
		}
	}

	@Override
	public void onContentRelease(float x, float y) {
		// TODO: optimize 
		for (int i = 0; i < itemList.length; i++) {
			if (itemList[i].contains(box.x+x, box.y+y)) {
				itemList[i].onContentRelease(box.x+x, box.y+y);
			}
		}
	}

	@Override
	public void onContentTouching(float x, float y) {
		
	}

	@Override
	public void establishSize() {
		// TODO Auto-generated method stub
		
	}
	
	public void setItemList(ShopListItem [] itemList) {
		this.itemList = itemList;
	}
}
