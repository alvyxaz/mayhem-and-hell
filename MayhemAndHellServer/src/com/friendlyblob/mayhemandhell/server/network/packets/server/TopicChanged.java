package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class TopicChanged extends ServerPacket {

	public String topic;
	
	public TopicChanged(String topic) {
		this.topic = topic;
	}
	
	@Override
	protected void write() {
		writeC(0x17);
		writeS(topic);
	}

}
