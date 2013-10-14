package com.friendlyblob.mayhemandhell.server.network.packets.client;

import com.friendlyblob.mayhemandhell.server.actions.GameActions.GameAction;
import com.friendlyblob.mayhemandhell.server.ai.Intention;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.ActionFailedMessage;

public class RequestAction extends ClientPacket {
	int actionIndex;
	
	@Override
	protected boolean read() {
		actionIndex = readD();
		return true;
	}

	@Override
	public void run() {
		Player player = getClient().getPlayer();
		GameAction[] actions = player.getAvailableActions();
		if (actions != null && actionIndex < actions.length && actionIndex >= 0) {
			// TODO find a better way AND place to switch between different actions
			if (actions[actionIndex] == GameAction.FOLLOW) {
				if (player.getTarget() instanceof GameCharacter) {
					player.getAi().startFollowing(
							(GameCharacter) player.getTarget());
				}
			} else if (actions[actionIndex] == GameAction.ATTACK) {
				// TODO implement range for different weapons and include
				// it in follow calculations
//				player.getAi().startAutoAttack();
			}
		} else {
			getClient().sendPacket(new ActionFailedMessage("[RequestAction packet] Invalid action"));
		}
	}
}
