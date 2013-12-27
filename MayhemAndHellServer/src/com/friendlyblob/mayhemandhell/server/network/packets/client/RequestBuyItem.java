package com.friendlyblob.mayhemandhell.server.network.packets.client;

import java.util.List;

import com.friendlyblob.mayhemandhell.server.actions.GameActions.GameAction;
import com.friendlyblob.mayhemandhell.server.ai.Intention;
import com.friendlyblob.mayhemandhell.server.factories.ItemFactory;
import com.friendlyblob.mayhemandhell.server.model.World;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter.DestinationReachedTask;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.model.actors.instances.NpcInstance;
import com.friendlyblob.mayhemandhell.server.model.datatables.DialogTable;
import com.friendlyblob.mayhemandhell.server.model.datatables.ItemTable;
import com.friendlyblob.mayhemandhell.server.model.datatables.ShopTable;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogPage;
import com.friendlyblob.mayhemandhell.server.model.instances.ItemInstance;
import com.friendlyblob.mayhemandhell.server.model.items.Item;
import com.friendlyblob.mayhemandhell.server.model.resources.Resource;
import com.friendlyblob.mayhemandhell.server.model.resources.Resource.ResourceSkill;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.ActionFailedMessage;
import com.friendlyblob.mayhemandhell.server.network.packets.server.CharacterStatusUpdate;
import com.friendlyblob.mayhemandhell.server.network.packets.server.DialogPageInfo;
import com.friendlyblob.mayhemandhell.server.network.packets.server.StartCasting;
import com.friendlyblob.mayhemandhell.server.network.packets.server.UpdateInventorySlot;

public class RequestBuyItem extends ClientPacket {
	int itemId;
	
	@Override
	protected boolean read() {
		itemId = readD();
		
		return true;
	}

	@Override
	public void run() {
		Player player = getClient().getPlayer();
		
		List<Item> shopItemList = ShopTable.getInstance().getShop(player.getLastShopId());
		
		Item item;
		// TODO: optimize
		if (shopItemList != null) {
			for (int i = 0; i < shopItemList.size(); i++) {
				item = shopItemList.get(i);
				
				if (item.getItemId() == itemId && item.getStatsSet().getInt("price") < player.getMoney()) {
					// TODO: deduct the sum from the player's money
					ItemInstance itemInstance = ItemFactory.getInstance().createItemInstance(item);
					int slot = player.getInventory().addItem(itemInstance);
					
					if (slot != -1) {
						getClient().sendPacket(new UpdateInventorySlot(slot, item.getItemId()));
					}
				}
			}
		}
	}
}
