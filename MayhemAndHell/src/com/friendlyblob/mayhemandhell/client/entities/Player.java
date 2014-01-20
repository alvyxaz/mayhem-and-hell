package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input.Direction;
import com.friendlyblob.mayhemandhell.client.entities.gui.ChatBubbleNotifications;
import com.friendlyblob.mayhemandhell.client.entities.gui.inventory.Inventory;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.PositionUpdate;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RequestMove;

public class Player extends GameCharacter {
	
	private TextureRegion [] textures;
	
	public int targetId;
	
	public Inventory inventory = new Inventory(3, 4);

	private int charId;
	
	public static final float POSITION_UPDATE_RATE = 0.3f;
	private float positionUpdateTimer = 0f;
	
	public Player (int id, int x, int y, int charId){
		super(id, x, y, charId);

		int width = 32;
		int height = 32;
		
		Texture texture = Assets.manager.get("textures/characters/characters.png", Texture.class);
		
		textures = new TextureRegion[12];
		
		for (int i = 0; i < textures.length; i++) {
			textures[i] = new TextureRegion(texture, 0 + (i%3)*width, 0 + (i/3)*height, 32, 32);
		}
	}

	public void update(float deltaTime){
		super.update(deltaTime);

		// Sending information to the server about our current position
		if (this.isMoving()) {
			positionUpdateTimer -= deltaTime;
			if (positionUpdateTimer < 0) {
				positionUpdateTimer += POSITION_UPDATE_RATE;
				// TODO don't send while idle
				MyGame.connection.sendPacket(new PositionUpdate(this.position));
			}
		}
	}
	
	public void moveToDirection(Direction direction) {
		final int virtualDistance = 200; // px per second
		
		if (direction == Direction.NONE) {
			
			this.stopMoving((int)this.position.x, (int)this.position.y);
		} else {
			float angle = direction.getAngle();
			
			int virtualX = (int) (position.x + Math.cos(angle) * virtualDistance);
			int virtualY = (int) (position.y + Math.sin(angle) * virtualDistance);
			this.moveTo(virtualX, virtualY);
		}
	}
	
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);		
	}
	
	public void requestMovementDestination(int x, int y) {
//		MyGame.connection.sendPacket(new RequestMove(x, y));
	}
	
	public Inventory getInventory() {
		return inventory;
	}
}
