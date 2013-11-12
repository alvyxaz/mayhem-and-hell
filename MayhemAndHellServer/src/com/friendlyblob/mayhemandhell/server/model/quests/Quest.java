package com.friendlyblob.mayhemandhell.server.model.quests;

import java.io.File;

import com.friendlyblob.mayhemandhell.server.model.actors.NpcTemplate;
import com.friendlyblob.mayhemandhell.server.model.datatables.NpcTable;
import com.friendlyblob.mayhemandhell.server.scripting.Script;
import com.friendlyblob.mayhemandhell.server.scripting.ScriptManager;

public class Quest extends Script{

	private int questId;
	private String name;
	
	public Quest(int questId, String name, String filePath) {
		this.questId = questId;
		this.name = name;
		this.setScriptFile(new File(filePath));
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Adds an Npc that starts this quest
	 * @param npcId
	 */
	public void addStartNpc(int npcId) {
		addEventToNpc(QuestEventType.QUEST_START, npcId);
	}
	
	/**
	 * Adds an npc that needs to be killed. 
	 * @param npcId
	 */
	public void addKillNpc(int npcId) {
		addEventToNpc(QuestEventType.QUEST_START, npcId);
	}
	
	/**
	 * Adds an event to NPC
	 * @param eventType
	 * @param npcId
	 */
	public void addEventToNpc(QuestEventType eventType, int npcId) {
		NpcTemplate npc = NpcTable.getInstance().getTemplate(npcId);
		if (npc != null) {
			npc.addQuestEvent(eventType, this);
		}
	}
	
	public static enum QuestEventType {
		QUEST_START,
		ON_TALK,
		ON_ATTACK,
		ON_KILL,
		ON_SPAWN,
	}

	@Override
	public boolean unload() {
		return false;
	}

	@Override
	public String getScriptName() {
		return null;
	}

	@Override
	public ScriptManager getScriptManager() {
		return null;
	}
	
}
