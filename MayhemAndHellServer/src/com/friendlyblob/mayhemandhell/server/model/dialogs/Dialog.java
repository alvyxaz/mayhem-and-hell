package com.friendlyblob.mayhemandhell.server.model.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.friendlyblob.mayhemandhell.server.model.actors.NpcTemplate;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.model.actors.instances.NpcInstance;
import com.friendlyblob.mayhemandhell.server.model.quests.Quest;

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
	private Quest quest;
	private boolean questDialog;
	
	public Dialog(int id) {
		this.id = id;
		pages = new DialogPage[0];
	}
	
	public void markAsQuestDialog() {
		questDialog = true;
	}
	
	public Quest getQuest() {
		return quest;
	}

	public void setQuest(Quest quest) {
		this.quest = quest;
		markAsQuestDialog();
	}

	public void addPage(DialogPage page) {
		if (page != null) {
			pages = Arrays.copyOf(pages, pages.length+1);
			page.setDialog(this);
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
	
	public List<DialogLink> getLinksInPage(int page, Player player) {
		if (page >= 0 && page < pages.length){
			return pages[page].getLinks(player);
		}
		
		return null;
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
		private Dialog dialog;
		private boolean acceptEnabled;
		
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
		
		public List<DialogLink> getLinks(Player player) {
			return getLinks(player, true);
		}
		
		public List<DialogLink> getLinks(Player player, boolean includeQuests) {
			List<DialogLink> links = new ArrayList<DialogLink>();
			DialogLink[] generalLinks = getLinks();
			
			// Adding general links
			if (generalLinks != null) {
				for(DialogLink link : generalLinks) {
					links.add(link);
				}
			}
			
			if (!includeQuests) {
				return links;
			}
			
			NpcTemplate npc = null;
			
			if (player.getTarget() instanceof NpcInstance) {
				npc = (NpcTemplate) ((NpcInstance)player.getTarget()).getTemplate();
			}
			
			if (npc == null || (dialog != null && dialog.questDialog)) {
				return links;
			}
			
			// If it's the main page, add quest related links 
			// TODO find a way to not do this when current dialog is quest dialog
			if (id == 0) {
				List<Quest> questList = npc.getQuestsToStart(player);
				if (questList != null) {
					for(Quest quest : questList) {
						links.add(quest.getQuestStartLink());
					}
				}
			}
			
			return links;
		}
		
		public DialogLink[] getLinks() {
			return links;
		}
		
		public DialogLink getLink(int index) {
			if (index >= 0 && index < links.length){
				return links[index];
			}
			return null;
		}
		
		public void setLinks(DialogLink[] links) {
			this.links = links;
		}
		
		public Dialog getDialog() {
			return dialog;
		}
		
		public void setDialog(Dialog dialog) {
			this.dialog = dialog;
		}
		
		public boolean isAcceptEnabled() {
			return acceptEnabled;
		}
		
		public void setAcceptEnabled(boolean acceptEnabled) {
			this.acceptEnabled = acceptEnabled;
		}
		
	}
	
	public static enum DialogLinkType {
		UNKNOWN(0),
		PAGE(1),
		QUEST_START(2),
		QUEST_COMPLETE(3),
		SHOP(4);
		
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
