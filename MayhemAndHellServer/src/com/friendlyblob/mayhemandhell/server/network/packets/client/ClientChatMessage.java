package com.friendlyblob.mayhemandhell.server.network.packets.client;

import java.util.concurrent.ConcurrentHashMap;

import javolution.util.FastMap;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.World;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.ChatMessageNotify;

public class ClientChatMessage extends ClientPacket {

	String msg;
	int type;
	
	// message type specific vars
	int recipientPlayerId;
	int guildObjectId;
	int partyObjectId;
	
	@Override
	protected boolean read() {
		this.msg = readS();
		this.type = readD();
		
		switch (type) {
			case 1:
				this.recipientPlayerId = readD();
				break;
			case 2:
				this.guildObjectId = readD();
				break;
			case 3:
				this.partyObjectId = readD();
				break;
		}
		
		System.out.println("received message: " + msg + " " + type);
		
		System.out.println("Player at region: " + getClient().getPlayer().getRegion().regionX + " " + getClient().getPlayer().getRegion().regionY);
		
		return true;
	}

	@Override
	public void run() {
		int playerId = getClient().getPlayer().getObjectId();
		ChatMessageNotify packet = new ChatMessageNotify(playerId, msg, type);

		switch (type) {
			case 0:
				getClient().getPlayer().getRegion().broadcastToCloseRegions(packet);
				
				break;
			case 1:
				GameObject go = World.getInstance().getObject(recipientPlayerId);
				
				System.out.println(recipientPlayerId);
				
				// Send to recipient
				if (go instanceof Player) {
					System.out.println("player");
					((Player) go).sendPacket(packet);
				}
				
				// Reply to sender
				getClient().getPlayer().sendPacket(packet);
				break;
			case 2:
				// TODO: implement when guilds added
				break;
			case 3:
				// TODO: implement when party feature is added
				break;
			case 4:
				ConcurrentHashMap<Integer, GameCharacter> players = World.getInstance().getPlayers();
				
				for (GameCharacter character : players.values()) {
				    character.sendPacket(packet);
				}

				break;
		}
	}

	
}
