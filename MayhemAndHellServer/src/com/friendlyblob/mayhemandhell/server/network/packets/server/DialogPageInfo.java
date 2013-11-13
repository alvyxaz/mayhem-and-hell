package com.friendlyblob.mayhemandhell.server.network.packets.server;

import java.util.List;

import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogLink;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogPage;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class DialogPageInfo extends ServerPacket {

	private String npcName;
	private DialogPage page;
	private List<DialogLink> links;
	
	public DialogPageInfo(String npcName, DialogPage page, List<DialogLink> links) {
		this.npcName = npcName;
		this.page = page;
		this.links = links;
	}
	
	@Override
	protected void write() {
		writeC(0x0D);
		writeS(npcName);							// NPC talking
		writeS(page.getText());						// Page text
		writeD(page.getId());						// Current page id
		
		if (links == null) {
			writeD(0);
			return;
		}
		
		writeD(links.size());						// Number of links
		
		for (int i = 0; i < links.size(); i++) {
			writeD(links.get(i).getType().getVal());	// Type id
			writeS(links.get(i).getText());				// Link text
		}
	}

}
