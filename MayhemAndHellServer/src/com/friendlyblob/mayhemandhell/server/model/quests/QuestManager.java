package com.friendlyblob.mayhemandhell.server.model.quests;

import java.util.Map;

import javolution.util.FastMap;

import com.friendlyblob.mayhemandhell.server.scripting.ScriptManager;

public class QuestManager extends ScriptManager<Quest> {

	private static QuestManager instance;
	
	private final Map<Integer, Quest> quests = new FastMap<Integer, Quest>().shared();
	
	public Quest getQuest(int id) {
		return quests.get(id);
	}
	
	public static QuestManager getInstance() {
		return instance;
	}
	
	public static void initialize() {
		instance = new QuestManager();
	}
	
	public void addQuest(Quest quest) {
		if (quest == null ) {
			throw new IllegalArgumentException("Quest argument cannot be null");
		}
		
		Quest oldQuest = quests.get(quest.getName());
		
		if (oldQuest != null) {
			oldQuest.unload();
		}
		
		quests.put(quest.getQuestId(), quest);
	}
	
	@Override
	public String getScriptManagerName() {
		return "QuestManager";
	}
	
}
