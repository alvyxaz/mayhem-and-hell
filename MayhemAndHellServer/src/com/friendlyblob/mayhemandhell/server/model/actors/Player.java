package com.friendlyblob.mayhemandhell.server.model.actors;

import com.friendlyblob.mayhemandhell.server.actions.GameActions;
import com.friendlyblob.mayhemandhell.server.actions.GameActions.GameAction;
import com.friendlyblob.mayhemandhell.server.model.items.EquipableItem;
import com.friendlyblob.mayhemandhell.server.model.items.EquipableItem.EquipmentSlot;
import com.friendlyblob.mayhemandhell.server.model.items.Item;
import com.friendlyblob.mayhemandhell.server.network.GameClient;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class Player extends GameCharacter {
	
	private GameClient client = null;

	public EquipableItem [] equippedItems;
	
	public Player() {
		super();
		this.setObjectId((int)(Math.random()*2000)); // TODO put a real id
		this.setName("PC " + this.getObjectId());
		this.setType(GameObjectType.PLAYER);
		equippedItems = new EquipableItem[EquipmentSlot.values().length];
	}

	/**
	 * Equips a given item, removes old and adds new StatModifier's
	 * @param item
	 * @return true if successful, false if not
	 */
	public synchronized boolean equipItem(EquipableItem item) {
		if (item.canEquip(this)) {
			
			unequipItem(equippedItems[item.getSlotIndex()]);
			
			// Equipping new item
			equippedItems[item.getSlotIndex()] = item;
			
			// Adding new stat modifiers
			this.getStats().addStatModifiers(item.getStatModifiers());
			
			return true;
		}
		return false;
	}
	
	/**
	 * Unequips an intem and removes StatModifier's
	 * @param item
	 */
	public synchronized void unequipItem(EquipableItem item) {
		if (equippedItems[item.getSlotIndex()] != null) {
			this.getStats().removeStatModifiers(
					equippedItems[item.getSlotIndex()].getStatModifiers());
			equippedItems[item.getSlotIndex()] = null;
		}
	}
	
	/**
	 * Checks whether an item exists in inventory
	 * @param itemId
	 * @return
	 */
	public Item itemInInventory(int itemId) {
		// TODO Make an inventory
		return null;
	}
	
	/**
	 * Equips an item with a given id
	 * @param itemId
	 * @return
	 */
	public boolean equipItem (int itemId) {
		Item item = itemInInventory(itemId);
		
		if (item != null && item instanceof EquipableItem) {
			return equipItem((EquipableItem) item);
		}
		return false;
	}
	
	/**
	 * Unequips an item that is currently equipped.
	 * Loops through equippedItems, might need synchronization
	 * @param itemId
	 */
	public synchronized void unequipItem (int itemId) {
		Item item = null;
		
		for (int i = 0; i < equippedItems.length; i++) {
			if (equippedItems[i].getObjectId() == itemId) {
				item = equippedItems[i];
				break;
			}
		}
		
		if (item != null && item instanceof EquipableItem) {
			unequipItem((EquipableItem) item);
		}
	}
	
	public void setClient(GameClient client) {
		this.client = client;
	}
	
	public GameClient getClient() {
		return client;
	}
	
	@Override
	public void sendPacket(ServerPacket packet) {
		getClient().sendPacket(packet);
	}
	
	/**
	 * Analyses a target and returns a list of available actions.
	 * TODO If user cannot attack, don't send attack action as available and etc.
	 * @return
	 */
	public GameAction[] getAvailableActions() {
		if (getTarget() != null) {
			switch (getTarget().getType()) {
			case PLAYER:
				return GameActions.onPlayer;
			}
		}
		return null;
	}
	
	/**
	 * Returns whether or not the given action is allowed for this player
	 * @param action
	 * @return
	 */
	public boolean isActionAllowed(GameAction action) {
		return true;
	}
	
}
