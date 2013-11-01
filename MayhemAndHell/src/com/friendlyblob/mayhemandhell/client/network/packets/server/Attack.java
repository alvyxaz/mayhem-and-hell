package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.gui.LiveNotifications;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;


public class Attack extends ReceivablePacket {

	private int targetId;
	private int damage;
	
	@Override
	public boolean read() {
		targetId = readD();
		damage = readD();
		return true;
	}

	@Override
	public void run() {
		GameCharacter character = null;
		
		if (targetId == GameWorld.instance.getPlayer().objectId) {
			character = GameWorld.instance.getPlayer();
		} else {
			character = GameWorld.getInstance().characters.get(targetId);
		}
		
		// TODO start an animation, after which display damage
		if (character != null) {
			GameWorld.getInstance().game.screenGame.notifications.addHealthChangeNotification(
					-damage, 
					character.hitBox.x, 
					character.hitBox.y + character.hitBox.height/2);
		}
	
	}

}
