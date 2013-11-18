package com.friendlyblob.mayhemandhell.server.data;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javolution.util.FastList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogLink;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogLinkType;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogPage;

public class DialogDataParser extends DataParser {

	private final List<Dialog> dialogsInFile = new FastList<>();
	
	public final String folderUrl = "data/dialogs/";
	
	public List<Dialog> getDialogs() {
		return dialogsInFile;
	}
	
	/**
	 * Reads item data from files in items folder
	 * and adds them to itemsInFile  list
	 */
	public void load() {
		File folder = new File(folderUrl);
		File [] files = folder.listFiles();
		
		for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
			dialogsInFile.addAll(parseDialogsFile(files[fileIndex]));
		}
	}
	
	public List<Dialog> parseDialogsFile(File file) {
		List<Dialog> list = new FastList<Dialog>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			
			NodeList dialogs = doc.getElementsByTagName("dialog");
			
			for (int i = 0; i < dialogs.getLength(); i++) {
				list.add(parseDialog(dialogs.item(i)));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Parses a single item
	 * @param dialogNode
	 * @return
	 * @throws InvocationTargetException
	 */
	public Dialog parseDialog(Node dialogNode) throws InvocationTargetException {
		int dialogId = Integer.parseInt(dialogNode.getAttributes().getNamedItem("id").getNodeValue());

		Dialog dialog = new Dialog(dialogId);
		
		for (dialogNode = dialogNode.getFirstChild(); dialogNode != null; 
				dialogNode = dialogNode.getNextSibling()) {
			
			// Skipping empty nodes (text nodes that are not elements)
			if (dialogNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			if ("page".equalsIgnoreCase(dialogNode.getNodeName())) {
				dialog.addPage(parsePage(dialogNode));
			}
		}
		return dialog;
		
	}
	
	public DialogPage parsePage(Node pageNode) {
		DialogPage page = new DialogPage();
		page.setId(Integer.parseInt(pageNode.getAttributes().getNamedItem("id").getNodeValue()));
		
		Node leftButtonNode= pageNode.getAttributes().getNamedItem("leftButton");
		if (leftButtonNode != null) {
			page.setLeftButton(true, leftButtonNode.getNodeValue());
		}
		
		for (pageNode = pageNode.getFirstChild(); pageNode != null; pageNode = pageNode.getNextSibling()) {
			// Skipping empty nodes (text nodes that are not elements)
			if (pageNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			if ("text".equals(pageNode.getNodeName())) {
				page.setText(pageNode.getTextContent());
			} else if("links".equalsIgnoreCase(pageNode.getNodeName())) {
				page.setLinks(parseLinks(pageNode));
			}
		}
		return page;
	}
	
	public DialogLink[] parseLinks(Node linkNode) {
		DialogLink links[] = new DialogLink[0];
		for (linkNode = linkNode.getFirstChild(); linkNode != null; linkNode = linkNode.getNextSibling()) {
			if ("link".equalsIgnoreCase(linkNode.getNodeName())) {
				links = Arrays.copyOf(links, links.length+1);
				links[links.length-1] = new DialogLink(
						DialogLinkType.valueOf(linkNode.getAttributes().getNamedItem("type").getNodeValue().trim().toUpperCase()),
						Integer.parseInt(linkNode.getAttributes().getNamedItem("target").getNodeValue().trim()),
						linkNode.getTextContent()
						);
			}
		}
		return links;
	}
	
	public static DialogDataParser getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public static class SingletonHolder {
		public static final DialogDataParser INSTANCE = new DialogDataParser();
	}
}
