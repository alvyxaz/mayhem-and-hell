package com.friendlyblob.mayhemandhell.client.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.friendlyblob.mayhemandhell.client.entities.gui.ChatBubbleNotifications;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;

public class TopicTv extends GameObject {
	
	public TopicTv() {
		super(-1);
		
		setPosition(423, 482);
	}

	@Override
	public void update(float deltaTime) {
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
	}
	
	public void setTopic(String topic) {
		GameWorld.instance.chatBubbles.addTvNotification(topic, this.hitBox);
	}
}
