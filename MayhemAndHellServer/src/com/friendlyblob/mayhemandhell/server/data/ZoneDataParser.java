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
import com.friendlyblob.mayhemandhell.server.model.ZoneTemplate.Tile;
import com.friendlyblob.mayhemandhell.server.model.ZoneTemplate.TileType;

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
		int mapWidth = Integer.parseInt(zoneNode.getAttributes().getNamedItem("width").getNodeValue());
		int mapHeight = Integer.parseInt(zoneNode.getAttributes().getNamedItem("height").getNodeValue());
		String zoneName = zoneNode.getAttributes().getNamedItem("name").getNodeValue();
		
		ZoneTemplate zone = new ZoneTemplate(mapWidth, mapHeight);
		
		for (zoneNode = zoneNode.getFirstChild(); zoneNode != null; 
				zoneNode = zoneNode.getNextSibling()) {
			
			// Skipping empty nodes (text nodes that are not elements)
			if (zoneNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			if ("set".equalsIgnoreCase(zoneNode.getNodeName())) {
				parseSetPair(zoneNode, zone.getStatsSet());
			}
			
			if ("metadata".equalsIgnoreCase(zoneNode.getNodeName())) {
				parseTileTypes(zoneNode.getTextContent(), zone);
			}
			
		}
		
		return zone;
	}
	
	/**
	 * Fills tile meta data from a string
	 * @param data string of data in csv encoding
	 */
	public void parseTileTypes(String data,  ZoneTemplate zone) {
		Tile[] tiles = zone.getTiles();
		String [] lines = data.split("\n");
		// Going backwards, because tiles from source go from top to bottom
		for (int i = lines.length-1; i >= 0; i--) {
			String [] strTiles = lines[i].split(",");
			for (int x = 0; x < strTiles.length; x++) {
				try {
					TileType type = Integer.parseInt(strTiles[x]) > 0 ? 
							TileType.COLLISION : TileType.NORMAL; // TODO more than one type
					tiles[zone.tileAtIndex(x, zone.getMapHeight()-i)].setType(type);
				} catch (Exception e) {
					
				}
			}
		}
		zone.establishNodeConnections();
	}
	
}
