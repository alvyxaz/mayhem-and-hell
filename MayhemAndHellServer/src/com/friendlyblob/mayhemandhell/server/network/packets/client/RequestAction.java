package com.friendlyblob.mayhemandhell.server.network.packets.client;

import com.friendlyblob.mayhemandhell.server.actions.GameActions.GameAction;
import com.friendlyblob.mayhemandhell.server.ai.Intention;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.model.actors.instances.NpcInstance;
import com.friendlyblob.mayhemandhell.server.model.datatables.DialogTable;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogPage;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.ActionFailedMessage;
import com.friendlyblob.mayhemandhell.server.network.packets.server.DialogPageInfo;

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
				player.getAi().startAutoAttack();
			} else if (actions[actionIndex] == GameAction.TALK) {
				if (player.getTarget() instanceof NpcInstance) {
					int dialog = ((NpcInstance) player.getTarget()).getTemplate().set.getInteger("dialog", -1);
					if (dialog != -1) {
						player.sendPacket(new DialogPageInfo(player.getTarget().getName(), 
								DialogTable.getInstance().getDialog(dialog).getPage(0)));
					}
				}
			}
		} else {
			getClient().sendPacket(new ActionFailedMessage("[RequestAction packet] Invalid action"));
		}
	}
}
