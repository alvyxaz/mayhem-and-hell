package com.friendlyblob.mayhemandhell.server.model.datatables;

import java.util.Map;

import javolution.util.FastMap;

import com.friendlyblob.mayhemandhell.server.data.ZoneDataParser;
import com.friendlyblob.mayhemandhell.server.model.ZoneTemplate;

public class ZoneTable {

	private static ZoneTable instance;
	
	private ZoneTemplate [] zoneTemplates;
	private Map<Integer, ZoneTemplate> zones;
	
	public static void initialize() {
		instance = new ZoneTable();
	}
	
	public ZoneTable() {
		zones = new FastMap<>();
		load();
	}
	
	private void load() {
		int highest = 0;
		
		ZoneDataParser zoneParser = new ZoneDataParser();
		zoneParser.load();
		
		for (ZoneTemplate zone: zoneParser.getZoneTemplates()) {
			if (highest < zone.getZoneId()) {
				highest = zone.getZoneId();
			}
			zones.put(zone.getZoneId(), zone);
		}
		
		buildLookupTable(highest);
	}
	
	private void buildLookupTable(int highest) {
		zoneTemplates = new ZoneTemplate[highest+1];

		for (ZoneTemplate zone: zones.values()) {
			zoneTemplates[zone.getZoneId()] = zone;
		}
	}
	
	public ZoneTemplate[] getZoneTemplates() {
		return zoneTemplates;
	}
	
	public ZoneTemplate getZoneTemplate(int id) {
		return zoneTemplates[id];
	}

	public static ZoneTable getInstance() {
		return instance;
	}
	
	
	
}
