package com.friendlyblob.mayhemandhell.server.model.quests;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.friendlyblob.mayhemandhell.server.data.DialogDataParser;
import com.friendlyblob.mayhemandhell.server.model.actors.NpcTemplate;
import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.model.datatables.DialogTable;
import com.friendlyblob.mayhemandhell.server.model.datatables.NpcTable;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogLink;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogLinkType;
import com.friendlyblob.mayhemandhell.server.scripting.Script;
import com.friendlyblob.mayhemandhell.server.scripting.ScriptManager;

public class Quest extends Script{

	private static final String DIALOGS_FILE = "dialogs.xml";
	
	private int questId;
	private String name;
	
	private DialogLink questStartLink;
	private DialogLink questCompleteLink;
	
	private Dialog startDialog;
	private Dialog completeDialog;
	
	private HashMap<Integer, Dialog> dialogs = new HashMap<>();
	
	public Quest(int questId, String name, String filePath) {
		this.questId = questId;
		this.name = name;
		this.setScriptFile(new File(filePath));
		questStartLink = new DialogLink(DialogLinkType.QUEST_START, questId, name);
		questCompleteLink = new DialogLink(DialogLinkType.QUEST_COMPLETE, questId, name); 
		
		initializeDialogs();
	}
	
	public void initializeDialogs() {
		String scriptPath = this.getScriptFile().getPath();
		int cutFrom = scriptPath.lastIndexOf(File.separatorChar);
		String dialogsPath = scriptPath.substring(0, cutFrom+1);
		List<Dialog> tempDialogs = DialogDataParser.getInstance()
				.parseDialogsFile(new File(dialogsPath + DIALOGS_FILE));
		
		if (tempDialogs != null) {
			for(Dialog dialog : tempDialogs) {
				dialog.setQuest(this);
				dialogs.put(dialog.getId(), dialog);
			}
		}
	}
	
	public int getQuestId() {
		return questId;
	}
	
	public Dialog getDialog(int id) {
		return dialogs.get(id);
	}
	
	public void setStartDialog(Dialog dialog) {
		startDialog = dialog;
		dialog.markAsQuestDialog();
	}
	
	public void setCompleteDialog(Dialog dialog) {
		completeDialog = dialog;
		dialog.markAsQuestDialog();
	}
	
	public void setStartDialog(int id) {
		if (dialogs.containsKey(id)){
			startDialog = dialogs.get(id);
		}
	}
	
	public void setCompleteDialog(int id) {
		if (dialogs.containsKey(id)){
			completeDialog = dialogs.get(id);
		}
	}
	
	public Dialog getDialogStart() {
		return startDialog;
	}

	public Dialog getDialogComplete() {
		return completeDialog;
	}

	public DialogLink getQuestCompleteLink() {
		return questCompleteLink;
	}
	
	public DialogLink getQuestStartLink() {
		return questStartLink;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Adds an Npc that starts this quest
	 * @param npcId
	 */
	public void addStartNpc(int npcId) {
		addEventToNpc(QuestEventType.QUEST_START, npcId);
	}
	
	/**
	 * Adds an npc that needs to be killed. 
	 * @param npcId
	 */
	public void addKillNpc(int npcId) {
		addEventToNpc(QuestEventType.QUEST_START, npcId);
	}
	
	/**
	 * Adds an event to NPC
	 * @param eventType
	 * @param npcId
	 */
	public void addEventToNpc(QuestEventType eventType, int npcId) {
		NpcTemplate npc = NpcTable.getInstance().getTemplate(npcId);
		if (npc != null) {
			npc.addQuestEvent(eventType, this);
		}
	}
	
	public static enum QuestEventType {
		QUEST_START,
		ON_TALK,
		ON_ATTACK,
		ON_KILL,
		ON_SPAWN,
	}
	
	public void startQuest(Player player) {
		QuestState state = player.getQuestState(getQuestId());
		if (state == null) {
			state = new QuestState(this);
		}
	}

	@Override
	public boolean unload() {
		return false;
	}

	@Override
	public String getScriptName() {
		return null;
	}

	@Override
	public ScriptManager getScriptManager() {
		return null;
	}
	
}
