package com.friendlyblob.mayhemandhell.server.model.datatables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import javolution.util.FastMap;

import com.friendlyblob.mayhemandhell.server.DatabaseFactory;
import com.friendlyblob.mayhemandhell.server.data.ResourceDataParser;
import com.friendlyblob.mayhemandhell.server.factories.IdFactory;
import com.friendlyblob.mayhemandhell.server.model.World;
import com.friendlyblob.mayhemandhell.server.model.Zone;
import com.friendlyblob.mayhemandhell.server.model.resources.Resource;
import com.friendlyblob.mayhemandhell.server.model.resources.ResourceTemplate;
import com.friendlyblob.mayhemandhell.server.utils.ObjectPosition;

public class ResourceTable {

	private static final String SELECT_RESOURCES = "SELECT * FROM resources";
	
	private static ResourceTable instance;
	
	private Map<Integer, ResourceTemplate> templates;
	
	public ResourceTable() {
		loadTemplates();
		addResourcesToWorld();
	}
	
	public void addResourcesToWorld() {
		try {
			Connection connection = DatabaseFactory.getInstance().getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(SELECT_RESOURCES);
			
			Resource resource = null;
			
			while(resultSet.next()) {
				int objectId = IdFactory.getInstance().getNextId();
				int resourceId = resultSet.getInt("id");
				ResourceTemplate template = templates.get(resultSet.getInt("template_id"));
				resource = new Resource(objectId, resourceId, template);
				
				resource.setPosition(new ObjectPosition(resultSet.getInt("x"),
						resultSet.getInt("y")));
				
				Zone zone = World.getInstance().getZone(resultSet.getInt("zone_id"));
				zone.addObject(resource);
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadTemplates() {
		templates = new FastMap<>();
		
		ResourceDataParser parser = new ResourceDataParser();
		parser.load();
		
		for (ResourceTemplate resource: parser.getResources()) {
			templates.put(resource.getId(), resource);
		}
	}
	
	public ResourceTemplate getTemplate(int index) {
		return templates.get(index);
	}
	
	public static void initialize() {
		instance = new ResourceTable();
	}
	
	public static ResourceTable getInstance() {
		return instance;
	}
	
	
}
