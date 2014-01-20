package com.friendlyblob.mayhemandhell.server.data;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javolution.util.FastList;

import com.friendlyblob.mayhemandhell.server.model.actors.NpcTemplate;
import com.friendlyblob.mayhemandhell.server.model.datatables.ItemTable;
import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;

/**
 * Takes care of parsing npc templates from xml file
 * @author Alvys
 *
 */
public class NpcDataParser extends DataParser {
	
	private final List<NpcTemplate> npcTemplates = new FastList<>();
	
	public final String folderUrl = "data/npcs/";
	
	// Saving a reference to currently parsing npc so 
	// it does not have to be dragged around method parameters
	private NpcTemplate currentNpc;
	
	/**
	 * Reads npc data from files in npcs folder
	 * and adds them to npcTemplates list
	 */
	public void load() { 
		try {
			File folder = new File(folderUrl);
			File [] files = folder.listFiles();
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
				Document doc = dBuilder.parse(files[fileIndex]);
				doc.getDocumentElement().normalize();

				NodeList npcs = doc.getElementsByTagName("npc");
				
				for (int i = 0; i < npcs.getLength(); i++) {
					npcTemplates.add(parseNpc(npcs.item(i)));
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public NpcTemplate parseNpc(Node npcNode) throws InvocationTargetException {
		int npcId = Integer.parseInt(npcNode.getAttributes().getNamedItem("id").getNodeValue());
		String npcName = npcNode.getAttributes().getNamedItem("name").getNodeValue();
		String type = npcNode.getAttributes().getNamedItem("type").getNodeValue();
		int npcSprite = Integer.parseInt(npcNode.getAttributes().getNamedItem("sprite").getNodeValue());
		
		NpcTemplate npc = new NpcTemplate();
		currentNpc = npc;
		
		npc.npcId = npcId;
		npc.name = npcName;
		npc.setType(type);
		npc.setSprite(npcSprite);
		
		for (npcNode = npcNode.getFirstChild(); npcNode != null; 
				npcNode = npcNode.getNextSibling()) {
			
			// Skipping empty nodes (text nodes that are not elements)
			if (npcNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			if ("set".equalsIgnoreCase(npcNode.getNodeName())) {
				parseSetPair(npcNode, npc.set);
			} else if ("itemDropList".equalsIgnoreCase(npcNode.getNodeName())) {
				parseItemDropList(npcNode);
			}
		}
		
		npc.parseSetData();
		
		return npc;
	}
	
	private void parseItemDropList(Node dropListNode) {
		for (Node n = dropListNode.getFirstChild(); n != null; n = n.getNextSibling()) {
			// Skipping empty nodes (text nodes that are not elements)
			if (n.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			if ("item".equalsIgnoreCase(n.getNodeName())) {
				int itemId = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
				int min = Integer.parseInt(n.getAttributes().getNamedItem("min").getNodeValue());
				int max = Integer.parseInt(n.getAttributes().getNamedItem("max").getNodeValue());
				float chance = Float.parseFloat(n.getAttributes().getNamedItem("chance").getNodeValue());
				
				currentNpc.addItemDrop(ItemTable.getInstance().getItem(itemId), chance, min, max);
			}
		}
	}
	
	public List<NpcTemplate> getNpcs() {
		return npcTemplates;
	}
	
}
