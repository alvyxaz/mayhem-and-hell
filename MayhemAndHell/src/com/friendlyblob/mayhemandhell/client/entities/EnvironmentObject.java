package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class EnvironmentObject extends GameObject {

	public int type;
	
	public EnvironmentObject(int x, int y, int type) {
		super(x, y);
		this.type = type;
		// TODO Auto-generated constructor stub
	}
	
	public void draw(SpriteBatch sb) {
		sb.draw(Assets.px, x, y, 50, 50);
		Assets.defaultFont.draw(sb, String.valueOf(type), x, y);
	}
}
