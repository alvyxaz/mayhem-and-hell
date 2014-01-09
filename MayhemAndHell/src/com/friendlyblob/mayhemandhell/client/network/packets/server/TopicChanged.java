package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class TopicChanged extends ReceivablePacket {

	private String topic;
	
	@Override
	public boolean read() {
		topic = readS();

		return true;
	}

	@Override
	public void run() {
		GameWorld.getInstance().topicTv.setTopic(topic);
	}

}
