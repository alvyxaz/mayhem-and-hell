package com.friendlyblob.mayhemandhell.server.model.datatables;

import java.util.HashMap;
import java.util.Map;

import com.friendlyblob.mayhemandhell.server.model.actors.CharacterTemplate;
import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;

public class CharacterTemplateTable {

	private static CharacterTemplateTable instance;
	
	private static final Map<String, CharacterTemplate> characterTemplates = new HashMap<>();
	
	public CharacterTemplateTable() {
		// TODO implement reading from file
		StatsSet set = new StatsSet();
		set.set("baseMaxHealth", 100);
		characterTemplates.put("player", new CharacterTemplate(set));
	}
	
	public static void initialize() {
		instance = new CharacterTemplateTable();
	}
	
	public static CharacterTemplateTable getInstance() {
		return instance;
	}
	
	public CharacterTemplate getTemplate(String name) {
		return characterTemplates.get(name);
	}
	
}
