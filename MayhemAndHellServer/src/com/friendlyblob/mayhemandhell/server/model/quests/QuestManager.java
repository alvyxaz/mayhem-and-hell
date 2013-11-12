package com.friendlyblob.mayhemandhell.server.model.quests;

import java.util.Map;

import javolution.util.FastMap;

import com.friendlyblob.mayhemandhell.server.scripting.ScriptManager;

public class QuestManager extends ScriptManager<Quest> {

	private static QuestManager instance;
	
	private final Map<String, Quest> quests = new FastMap<>();
	
	public static QuestManager getInstance() {
		return instance;
	}
	
	public static void initialize() {
		instance = new QuestManager();
	}
	
	public void addQuest(Quest quest) {
		
	}
	
	@Override
	public String getScriptManagerName() {
		return "QuestManager";
	}
	
}
