package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RequestMove;

public class Player extends GameCharacter{
	
	public Player (int id, int x, int y){
		super(id, x, y);
	}

	public void regenerate(int x, int y){

	}
	
	public void update(float deltaTime){
		super.update(deltaTime);
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch){
		spriteBatch.setColor(Color.CYAN);
		spriteBatch.draw(Assets.px, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
		spriteBatch.setColor(Color.WHITE);
	}
	
	public void requestMovementDestination(int x, int y) {
		// TODO Wait for response before moving
		MyGame.connection.sendPacket(new RequestMove(x, y));
	}
	
}
