package com.friendlyblob.mayhemandhell.server.model.datatables;

import java.util.HashMap;
import java.util.Map;

import com.friendlyblob.mayhemandhell.server.data.NpcDataParser;
import com.friendlyblob.mayhemandhell.server.model.actors.NpcTemplate;
import com.friendlyblob.mayhemandhell.server.model.items.Item;

public class NpcTable {
	private static NpcTable instance;
	
	private NpcTemplate[] npcTemplates;
	private final Map<Integer, NpcTemplate> npcs;
	
	public NpcTable() {
		npcs = new HashMap<>();
		load();
	}
	
	public void load() {
		int highest = 0;
		
		NpcDataParser parser = new NpcDataParser();
		parser.load();
		
		for (NpcTemplate npc : parser.getNpcs()) {
			if (highest < npc.getNpcId()) {
				highest = npc.getNpcId();
			}
			npcs.put(npc.getNpcId(), npc);
		}
		buildLookupTable(highest);
	}
	
	/**
	 * Builds a quick table of templates (for fastest possible access)
	 * @param highest
	 */
	public void buildLookupTable(int highest) {
		npcTemplates = new NpcTemplate[highest+1];

		for (NpcTemplate npc : npcs.values()) {
			npcTemplates[npc.getNpcId()] = npc;
		}
		
	}
	
	public static NpcTable getInstance() {
		return instance;
	}
	
	public static void initialize() {
		instance = new NpcTable();
	}
}
