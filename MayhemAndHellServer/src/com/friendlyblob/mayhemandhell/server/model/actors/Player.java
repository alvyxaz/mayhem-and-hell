package com.friendlyblob.mayhemandhell.server.model.actors;

import java.util.HashMap;
import java.util.Map;

import com.friendlyblob.mayhemandhell.server.actions.GameActions;
import com.friendlyblob.mayhemandhell.server.actions.GameActions.GameAction;
import com.friendlyblob.mayhemandhell.server.ai.GameCharacterAi;
import com.friendlyblob.mayhemandhell.server.ai.Intention;
import com.friendlyblob.mayhemandhell.server.ai.PlayerAi;
import com.friendlyblob.mayhemandhell.server.model.World;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog;
import com.friendlyblob.mayhemandhell.server.model.instances.ItemInstance;
import com.friendlyblob.mayhemandhell.server.model.items.EquipableItem;
import com.friendlyblob.mayhemandhell.server.model.items.EquipableItem.EquipmentSlot;
import com.friendlyblob.mayhemandhell.server.model.items.Item;
import com.friendlyblob.mayhemandhell.server.model.quests.Quest;
import com.friendlyblob.mayhemandhell.server.model.quests.QuestState;
import com.friendlyblob.mayhemandhell.server.network.GameClient;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.CharacterStatusUpdate;
import com.friendlyblob.mayhemandhell.server.network.packets.server.EventNotification;

public class Player extends GameCharacter {
	
	private GameClient client = null;

	public ItemInstance [] equippedItems;
	
	private boolean online = true;
	
	private Inventory inventory;
	private int money;
	
	private Dialog dialog; // The last (current) dialog a player was interacting with
	
	private int lastShopId; // last shop the player interacted with
	
	private Map<Integer, QuestState> quests = new HashMap<Integer, QuestState>();
	
	private int charId;
	
	public QuestState getQuestState(int questId) {
		return quests.get(questId);
	}
	
	public void putQuestState(QuestState state) {
		quests.put(state.getQuestId(), state);
	}
	
	public Player(int objectId, CharacterTemplate template) {
		super(objectId, template);
		this.setObjectId((int)(5000 + Math.random()*2000)); // TODO remove
		this.setName("PC" + this.getObjectId());
		this.setType(GameObjectType.PLAYER);
		this.setMoney(100);
		
		this.setHealth(template.getBaseMaxHealth());
		
		equippedItems = new ItemInstance[EquipmentSlot.values().length];
		this.alive = true;
		attachAi();
		
		inventory = new Inventory(3, 4);
	}

	/**
	 * Equips a given item, removes old and adds new StatModifier's
	 * @param item
	 * @return true if successful, false if not
	 */
	public synchronized boolean equipItem(ItemInstance item) {
		if (item.isEquipable() && item.canEquip(this)) {
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
	public synchronized void unequipItem(ItemInstance item) {
		if (equippedItems[item.getSlotIndex()] != null) {
			this.getStats().removeStatModifiers(
					equippedItems[item.getSlotIndex()].getStatModifiers());
			equippedItems[item.getSlotIndex()] = null;
		}
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	/**
	 * Equips an item with a given id
	 * @param itemId
	 * @return
	 */
	public boolean equipItem (int itemId) {
		ItemInstance item = inventory.getItemAt(itemId);
		
		if (item != null && item.isEquipable()) {
			return equipItem(item);
		}
		return false;
	}
	
	/**
	 * Updates a player position. Different from setPosition because
	 * checks collision and whether a player is cheating.
	 * TODO Check if cheating
	 * @param x
	 * @param y
	 */
	public void updatePosition(int x, int y) {
		this.setPosition(x, y);
	}
	
	/**
	 * Unequips an item that is currently equipped.
	 * Loops through equippedItems, might need synchronization
	 * @param itemId
	 */
	public synchronized void unequipItem (int itemId) {
		ItemInstance item = null;
		
		for (int i = 0; i < equippedItems.length; i++) {
			if (equippedItems[i].getObjectId() == itemId) {
				item = equippedItems[i];
				break;
			}
		}
		
		if (item != null && item.isEquipable()) {
			unequipItem(item);
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
					return GameActions.friendlyPlayer;
				case HOSTILE_NPC:
					return GameActions.hostileNpc;
				case FRIENDLY_NPC:
					return GameActions.friendlyNpc;
				case ITEM:
					return GameActions.item;
				case RESOURCE:
					return GameActions.resource;
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
	
	@Override
	public synchronized void attachAi() {
		if (ai == null) {
			ai = new PlayerAi(this);
		}
	}
	
	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}
	
	public Dialog getDialog() {
		return dialog;
	}
	
	public void setLastShopId(int shopId) {
		this.lastShopId = shopId;
	}
	
	public int getLastShopId() {
		return lastShopId;
	}
	
	public boolean isOnline() {
		return online;
	}
	
	public void setOnline(boolean online) {
		this.online = online;
	}
	
	public int getMoney() {
		return money;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
	
	public void setCharId(int charId) {
		this.charId = charId;
	}
	
	public int getCharId() {
		return charId;
	}
	
	@Override
	public void cleanup() {
		setOnline(false);
		
		ai.setIntention(Intention.IDLE);
		
		World.getInstance().removePlayer(this);
		super.cleanup();
	}
	
	public void resurrect() {
		this.restoreVitals();
		this.sendPacket(new CharacterStatusUpdate(this));
		this.teleportTo(this.getZone(), 10, 10);
		this.alive = true;
	}
	
	public void sendEventNotification(String notification) {
		sendPacket(new EventNotification(notification));
	}
}
