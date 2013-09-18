package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

/**
 * Represents a visual object (no interactions with it are possible)
 * Used for "scenery enchantment"
 * @author Alvys
 *
 */
public class EnvironmentObject {

	public int tempIdHolder; // TODO safely remove (from xml and etc)
	public int type;
	public int x;
	public int y;
	
	public EnvironmentObject(int x, int y, int type) {
		super();
		this.type = type;
		// TODO Auto-generated constructor stub
	}
	
	public void draw(SpriteBatch sb) {
		sb.draw(Assets.px, x, y, 50, 50);
		Assets.defaultFont.draw(sb, String.valueOf(type), x, y);
	}
}
