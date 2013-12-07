package com.friendlyblob.mayhemandhell.server.data;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.friendlyblob.mayhemandhell.server.model.actors.NpcTemplate;
import com.friendlyblob.mayhemandhell.server.model.datatables.ItemTable;
import com.friendlyblob.mayhemandhell.server.model.instances.ItemInstance;
import com.friendlyblob.mayhemandhell.server.model.items.Item;
import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;

/**
 * Takes care of parsing item instances from xml file
 * @author Vytautas
 *
 */
public class ShopDataParser {
	
	private final Map<Integer, List<Item>> shops = new FastMap<>();
	
	public final String folderUrl = "data/shops/";
	
	/**
	 * Parses shop data (with items they carry)
	 * from the shops folder
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
				
				NodeList shopList = doc.getElementsByTagName("shop");
				
				for (int i = 0; i < shopList.getLength(); i++) {
					parseShop(shopList.item(i));
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void parseShop(Node shopNode) throws InvocationTargetException {
		int shopId = Integer.parseInt(shopNode.getAttributes().getNamedItem("id").getNodeValue());
				
		for (shopNode = shopNode.getFirstChild(); shopNode != null; 
				shopNode = shopNode.getNextSibling()) {
			
			// Skipping empty nodes (text nodes that are not elements)
			if (shopNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			if ("items".equalsIgnoreCase(shopNode.getNodeName())) {
				shops.put(shopId, parseShopItemList(shopNode));
			}
		}
	}
	
	private List<Item> parseShopItemList(Node shopItemListNode) {
		List<Item> itemList = new FastList<>();
		
		for (Node n = shopItemListNode.getFirstChild(); n != null; n = n.getNextSibling()) {
			// Skipping empty nodes (text nodes that are not elements)
			if (n.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			if ("item".equalsIgnoreCase(n.getNodeName())) {
				int itemId = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
				Item item = ItemTable.getInstance().getItem(itemId);
				
				if (item != null) {
					itemList.add(item);
				}
			}
		}
		
		return itemList;
	}
	
	public Map<Integer, List<Item>> getShops() {
		return shops;
	}
	
}
