package com.friendlyblob.mayhemandhell.server.network.packets.client;

import com.friendlyblob.mayhemandhell.server.actions.GameActions.GameAction;
import com.friendlyblob.mayhemandhell.server.ai.Intention;
import com.friendlyblob.mayhemandhell.server.model.World;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter.DestinationReachedTask;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.model.actors.instances.NpcInstance;
import com.friendlyblob.mayhemandhell.server.model.datatables.DialogTable;
import com.friendlyblob.mayhemandhell.server.model.datatables.ItemTable;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogPage;
import com.friendlyblob.mayhemandhell.server.model.instances.ItemInstance;
import com.friendlyblob.mayhemandhell.server.model.resources.Resource;
import com.friendlyblob.mayhemandhell.server.model.resources.Resource.ResourceSkill;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.ActionFailedMessage;
import com.friendlyblob.mayhemandhell.server.network.packets.server.DialogPageInfo;
import com.friendlyblob.mayhemandhell.server.network.packets.server.ItemPickedUp;
import com.friendlyblob.mayhemandhell.server.network.packets.server.StartCasting;

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
						player.moveCharacterTo(player.getTarget(), 
								new DestinationReachedTask(player, Intention.INTERACT, player.getTarget(), 
										DialogTable.getInstance().getDialog(dialog)));	
					}
				}
			} else if (actions[actionIndex] == GameAction.PICK_UP) {
				if (player.getTarget() instanceof ItemInstance) {
					ItemInstance item = (ItemInstance) player.getTarget();
					player.moveCharacterTo(player.getTarget(), 
							new DestinationReachedTask(player, Intention.PICK_UP, player.getTarget(), item));	
					
				}
			} else if (actions[actionIndex] == GameAction.GATHER) {
				if (player.getTarget() instanceof Resource) {
					player.moveCharacterTo(player.getTarget(), 
							new DestinationReachedTask(player, Intention.CAST, player.getTarget(), 
									new ResourceSkill("Testing", 1500)));
				}
			}
		} else {
			getClient().sendPacket(new ActionFailedMessage("[RequestAction packet] Invalid action"));
		}
	}
}
