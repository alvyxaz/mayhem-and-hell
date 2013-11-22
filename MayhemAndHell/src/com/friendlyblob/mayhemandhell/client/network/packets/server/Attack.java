package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.entities.GameCharacter;
import com.friendlyblob.mayhemandhell.client.entities.Player;
import com.friendlyblob.mayhemandhell.client.entities.gui.LiveNotifications;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;


public class Attack extends ReceivablePacket {

	private int targetId;
	private int attackerId;
	private int damage;
	private float attackAngle;
	
	@Override
	public boolean read() {
		targetId = readD();
		attackerId = readD();
		damage = readD();
		attackAngle = (float) readF();
		return true;
	}

	@Override
	public void run() {
		GameCharacter target = null;
		GameCharacter attacker = null;
		Player player = GameWorld.instance.getPlayer();
		
		// Whether damage is done to player 
		if (targetId == player.objectId) {
			target = player;
		} else {
			target = GameWorld.getInstance().characters.get(targetId);
		}
		
		if (attackerId == player.objectId) {
			attacker = player;
		} else {
			attacker = GameWorld.getInstance().characters.get(attackerId); 
		}
		
		// Play attack animation
		if (attacker != null) {
			attacker.attack(attackAngle);
		}
		
		if (target != null) {
			GameWorld.getInstance().game.screenGame.notifications.addHealthChangeNotification(
					-damage, 
					target.hitBox.x, 
					target.hitBox.y + target.hitBox.height/2);
		}
	
	}

}
