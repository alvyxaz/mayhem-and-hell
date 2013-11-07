package com.friendlyblob.mayhemandhell.server.model.dialogs;

import java.util.Arrays;

/**
 * Represents a whole dialog. Dialogs are made of pages, 
 * each of which can have links to other pages, thus
 * making dialog chains. Links can have various types
 * @author Alvys
 *
 */
public class Dialog {
	private int id;
	private DialogPage[] pages;
	
	public Dialog(int id) {
		this.id = id;
		pages = new DialogPage[0];
	}
	
	public void addPage(DialogPage page) {
		if (page != null) {
			pages = Arrays.copyOf(pages, pages.length+1);
			pages[pages.length-1] = page;
		}
	}
	
	public DialogPage getPage(int index) {
		if (index >= 0 && index < pages.length){
			return pages[index];
		}
		return null;
	}
	
	public int getId() {
		return id;
	}
	
	/**
	 * Represents a single page in a dialog.
	 * @author Alvys
	 *
	 */
	public static class DialogPage {
		private int id;
		private String text;
		private DialogLink[] links;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public DialogLink[] getLinks() {
			return links;
		}
		public void setLinks(DialogLink[] links) {
			this.links = links;
		}
		
		
	}
	
	public static enum DialogLinkType {
		UNKNOWN(0),
		PAGE(1),
		QUEST(2),
		SHOP(3);
		
		private int val;
		
		DialogLinkType(int value) {
			this.val = value;
		}

		DialogLinkType getTypeFromVal(int val) {
			for (DialogLinkType type: DialogLinkType.values()) {
				if (type.val == val) {
					return type;
				}
			}
			return DialogLinkType.UNKNOWN;
		}
		
		public int getVal() {
			return val;
		}
	}
	
	/**
	 * Represents a link in the doalog window.
	 * @author Alvys
	 *
	 */
	public static class DialogLink {
		private DialogLinkType type;
		private int target;
		private String text;
		
		public DialogLink(DialogLinkType type, int target, String text) {
			this.target = target;
			this.type = type;
			this.text = text;
		}

		public void setType(DialogLinkType type) {
			this.type = type;
		}

		public void setTarget(int target) {
			this.target = target;
		}

		public void setText(String text) {
			this.text = text;
		}

		public DialogLinkType getType() {
			return type;
		}

		public int getTarget() {
			return target;
		}

		public String getText() {
			return text;
		}
		
		
	}

}
