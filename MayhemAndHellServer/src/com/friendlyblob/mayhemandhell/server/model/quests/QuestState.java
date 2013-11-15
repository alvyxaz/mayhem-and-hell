package com.friendlyblob.mayhemandhell.server.model.quests;

import java.util.Map;

import javolution.util.FastMap;

public class QuestState {
	private int questId;
	
	private Map<String, String> values;
	
	public QuestState(Quest quest) {
		values = new FastMap<>();
		questId = quest.getQuestId();
	}
	
	private String getValue(String key) {
		return values.get(key);
	}
	
	private void setValue(String key, String value) {
		values.put(key, value);
	}
	
}
