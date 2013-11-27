package com.friendlyblob.mayhemandhell.server.data;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javolution.util.FastList;

import com.friendlyblob.mayhemandhell.server.model.datatables.ItemTable;
import com.friendlyblob.mayhemandhell.server.model.items.Item;
import com.friendlyblob.mayhemandhell.server.model.resources.ResourceTemplate;
import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;

public class ResourceDataParser extends DataParser {
	
	private final List<ResourceTemplate> resourcesInFile = new FastList<>();
	
	public final String folderUrl = "data/resources/";
	
	public void load() {
		try {
			File folder = new File(folderUrl);
			File [] files = folder.listFiles();
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
				Document doc = dBuilder.parse(files[fileIndex]);
				doc.getDocumentElement().normalize();
				
				NodeList resources = doc.getElementsByTagName("resource");
				
				for (int i = 0; i < resources.getLength(); i++) {
					resourcesInFile.add(parseResource(resources.item(i)));
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	public ResourceTemplate parseResource(Node resourceNode) throws InvocationTargetException {
		int resourceId = Integer.parseInt(resourceNode.getAttributes().getNamedItem("id").getNodeValue());
		String resourceName = resourceNode.getAttributes().getNamedItem("name").getNodeValue();

		ResourceTemplate resource = new ResourceTemplate();
		
		resource.setId(resourceId);
		resource.setName(resourceName);
		
		for (resourceNode = resourceNode.getFirstChild(); resourceNode != null; 
				resourceNode = resourceNode.getNextSibling()) {
			
			// Skipping empty nodes (text nodes that are not elements)
			if (resourceNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			if ("icon".equalsIgnoreCase(resourceNode.getNodeName())) {
				resource.setIcon(resourceNode.getTextContent());
			} else if ("limited".equalsIgnoreCase(resourceNode.getNodeName())) {
				resource.setLimited(Boolean.parseBoolean(resourceNode.getTextContent()));
			} else if ("regenerationTime".equalsIgnoreCase(resourceNode.getNodeName())) {
				resource.setRegenerationTime(Integer.parseInt(resourceNode.getTextContent()));
			} else if ("gatherTime".equalsIgnoreCase(resourceNode.getNodeName())) {
				resource.setGatherTime(Integer.parseInt(resourceNode.getTextContent()));
			} else if ("item".equalsIgnoreCase(resourceNode.getTextContent())){
				resource.setItem(
						ItemTable.getInstance().getItem(
								Integer.parseInt(resourceNode.getTextContent())));
			}
		}
		return resource;
	}
	
	public List<ResourceTemplate> getResources() {
		return resourcesInFile;
	}
}
