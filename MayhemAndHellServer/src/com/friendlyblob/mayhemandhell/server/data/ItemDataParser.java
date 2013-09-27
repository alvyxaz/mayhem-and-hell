package com.friendlyblob.mayhemandhell.server.data;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javolution.util.FastList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.friendlyblob.mayhemandhell.server.model.items.*;
import com.friendlyblob.mayhemandhell.server.model.stats.Stat;
import com.friendlyblob.mayhemandhell.server.model.stats.StatModifier;
import com.friendlyblob.mayhemandhell.server.model.stats.StatModifier.StatModifierType;
import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;

/**
 * Represents an xml parser that is specific for items.
 * @author Alvys
 *
 */
public class ItemDataParser {

	private final List<Item> itemsInFile = new FastList<>();
	
	public final String folderUrl = "data/items/";
	
	public List<Item> getItems() {
		return itemsInFile;
	}
	
	/**
	 * Reads item data from files in items folder
	 * and adds them to itemsInFile  list
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
				
				NodeList items = doc.getElementsByTagName("item");
				
				for (int i = 0; i < items.getLength(); i++) {
					itemsInFile.add(parseItem(items.item(i)));
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Parses a single item
	 * @param itemNode
	 * @return
	 * @throws InvocationTargetException
	 */
	public Item parseItem(Node itemNode) throws InvocationTargetException {
		int itemId = Integer.parseInt(itemNode.getAttributes().getNamedItem("id").getNodeValue());
		String className = itemNode.getAttributes().getNamedItem("type").getNodeValue();
		String itemName = itemNode.getAttributes().getNamedItem("name").getNodeValue();

		Item item = null;
		
		try {
			item = (Item) Class.forName("com.friendlyblob.mayhemandhell.server.model.items." + className).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		item.itemId = itemId;
		item.name = itemName;
		item.set = new StatsSet();
		item.set.set("itemId", itemId);
		item.set.set("name", itemName);
		
		for (itemNode = itemNode.getFirstChild(); itemNode != null; 
				itemNode = itemNode.getNextSibling()) {
			
			// Skipping empty nodes (text nodes that are not elements)
			if (itemNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			if ("set".equalsIgnoreCase(itemNode.getNodeName())) {
				
				parseSetPair(itemNode, item.set);
			} else if ("modifiers".equalsIgnoreCase(itemNode.getNodeName())) {
				parseModifiers(itemNode, item);
			}
		}
		
		return item;
		
	}
	
	/**
	 * Parses a node of modifiers and adds them to the item.
	 * At the moment of implementation, only EquipableItem can have stat modifiers.
	 * @param modifiersNode
	 * @param item
	 */
	public void parseModifiers(Node modifiersNode, Item item) {
		StatModifier [] modifiers = new StatModifier[5];
		
		for (Node n = modifiersNode.getFirstChild(); n != null; n = n.getNextSibling()) {
			if ("modifier".equals(n.getNodeName())){
				
				// Extracting required values
				Stat stat = Stat.valueOfXml(n.getAttributes().getNamedItem("stat").getNodeValue().trim());
				StatModifierType type = StatModifierType.valueOf(n.getAttributes().getNamedItem("type").getNodeValue().trim().toUpperCase());
				float value = Float.parseFloat(n.getAttributes().getNamedItem("value").getNodeValue().trim());
				
				if (item instanceof EquipableItem) {
					((EquipableItem) item).addModifier(new StatModifier(stat, type, value));
				}
				
			}
		}
	}
	
	/**
	 * Parses a set element, and adds it to item set
	 * @param n
	 * @param set
	 * @throws InvocationTargetException
	 */
	public void parseSetPair(Node n, StatsSet set)  throws InvocationTargetException {
		String name = n.getAttributes().getNamedItem("name").getNodeValue().trim();
		String value = n.getAttributes().getNamedItem("val").getNodeValue().trim();
		char ch = value.isEmpty() ? ' ' : value.charAt(0);
		
		if (Character.isDigit(ch)) {
			set.set(name, String.valueOf(value));
		} else {
			set.set(name, value);
		}
	}
	
}
