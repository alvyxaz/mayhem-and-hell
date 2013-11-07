package com.friendlyblob.mayhemandhell.server.network.packets.server;

import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogLink;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogPage;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;

public class DialogPageInfo extends ServerPacket {

	private String npcName;
	private DialogPage page;
	
	public DialogPageInfo(String npcName, DialogPage page) {
		this.npcName = npcName;
		this.page = page;
	}
	
	@Override
	protected void write() {
		writeC(0x0D);
		writeS(npcName);							// NPC talking
		writeS(page.getText());						// Page text
		
		DialogLink[] links = page.getLinks();		
		
		writeD(links.length);						// Number of links
		
		for (int i = 0; i < links.length; i++) {
			writeD(links[i].getType().getVal());	// Type id
			writeS(links[i].getText());				// Link text
		}
	}

}
