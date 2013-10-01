package com.friendlyblob.mayhemandhell.server.data;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.friendlyblob.mayhemandhell.server.model.ZoneTemplate;

import javolution.util.FastList;

public class ZoneDataParser extends DataParser {
	
	private final List<ZoneTemplate> zonesInFile = new FastList<>();
	
	public final String folderUrl = "data/zones/";
	
	public List<ZoneTemplate> getZoneTemplates() {
		return zonesInFile;
	}
	
	public void load() {
		try {
			File folder = new File(folderUrl);
			File [] files = folder.listFiles();
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
				Document doc = dBuilder.parse(files[fileIndex]);
				doc.getDocumentElement().normalize();
				
				NodeList zones = doc.getElementsByTagName("zone");
				
				for (int i = 0; i < zones.getLength(); i++) {
					zonesInFile.add(parseZone(zones.item(i)));
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ZoneTemplate parseZone(Node zoneNode) throws InvocationTargetException {
		int templateId = Integer.parseInt(zoneNode.getAttributes().getNamedItem("id").getNodeValue());
		String zoneName = zoneNode.getAttributes().getNamedItem("name").getNodeValue();
		
		ZoneTemplate zone = new ZoneTemplate();
		
		for (zoneNode = zoneNode.getFirstChild(); zoneNode != null; 
				zoneNode = zoneNode.getNextSibling()) {
			
			// Skipping empty nodes (text nodes that are not elements)
			if (zoneNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			if ("set".equalsIgnoreCase(zoneNode.getNodeName())) {
				parseSetPair(zoneNode, zone.getStatsSet());
			}
		}
		
		return zone;
	}
	
}
