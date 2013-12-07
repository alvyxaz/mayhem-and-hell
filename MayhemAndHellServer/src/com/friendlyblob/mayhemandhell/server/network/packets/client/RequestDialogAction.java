package com.friendlyblob.mayhemandhell.server.network.packets.client;

import java.util.List;

import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.model.actors.instances.NpcInstance;
import com.friendlyblob.mayhemandhell.server.model.datatables.DialogTable;
import com.friendlyblob.mayhemandhell.server.model.datatables.ShopTable;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogLink;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogLinkType;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogPage;
import com.friendlyblob.mayhemandhell.server.model.items.Item;
import com.friendlyblob.mayhemandhell.server.model.quests.Quest;
import com.friendlyblob.mayhemandhell.server.model.quests.QuestManager;
import com.friendlyblob.mayhemandhell.server.model.quests.QuestState;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.DialogPageInfo;
import com.friendlyblob.mayhemandhell.server.network.packets.server.ShowShop;

public class RequestDialogAction extends ClientPacket {

	@Override
	protected boolean read() {
		Player player = getClient().getPlayer();
		int index = readD();
		int currentPage = readD();
		
		// Checking if any of bottom buttons were clicked
		if (index < 0) {
			if (index == -1) {
				// Right button (Goodbye)
				player.setDialog(null);
			} else if (index == -2) {
				// Left button
				if (player.getDialog() != null) {
					Quest quest = player.getDialog().getQuest();
					if (quest != null) {
						QuestState state = player.getQuestState(quest.getQuestId());
						if (state == null) {
							quest.startQuest(player);
						} else if (state.isTurnIn()) {
							state.setCompleted();
							player.sendEventNotification("'"+ quest.getName() + "' completed");
						}
					}
				}
			}
			return false;
		}
		
		Dialog dialog = player.getDialog();
		
		if (dialog != null) {
			DialogLink link = player.getDialog().getPage(currentPage).getLinks(player).get(index);
			
			DialogPage page = null;
			
			if (link != null) {
				switch(link.getType()) {
					case PAGE:
						page = dialog.getPage(link.getTarget());
						break;
					case QUEST_START:
						Quest questStart = QuestManager.getInstance().getQuest(link.getTarget());
						if (questStart != null) {
							dialog = questStart.getDialogStart();
							page = dialog.getPage(0);
						}
						break;
					case QUEST_COMPLETE:
						Quest questComplete = QuestManager.getInstance().getQuest(link.getTarget());
						if (questComplete != null) {
							dialog = questComplete.getDialogComplete();
							page = dialog.getPage(0);
						}
						break;
					case SHOP:
						int shopId = link.getTarget();
						List<Item> shopItemList = ShopTable.getInstance().getShop(shopId);

						if (shopItemList != null) {
							player.setLastShopId(shopId);
							
							getClient().sendPacket(new ShowShop(shopItemList));
						}
						
						break;
					case UNKNOWN:
						break;
					default:
						break;
					}
			}
			
			// After the switch, either page or dialog can be changed
			// If the dialog is changed, we need to make sure players last dialog
			// is updated
			player.setDialog(dialog);
			
			if (page != null) {
				getClient().sendPacket(
						new DialogPageInfo(player.getTarget().getName(), 
								page, 
								page.getLinks(player)));
			}
			
		}
		return false;
	}

	@Override
	public void run() {
		
	}

}
