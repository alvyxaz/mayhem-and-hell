package com.friendlyblob.mayhemandhell.client.network.packets.server;

import com.friendlyblob.mayhemandhell.client.entities.gui.Dialog;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;

public class DialogPageInfo extends ReceivablePacket{

	private String name;
	private String text;
	private String[] linkTexts;
	private int[] linkTypes;
	private int pageId;
	private boolean acceptEnabled;
	
	@Override
	public boolean read() {
		
		name = readS();
		text = readS();
		pageId = readD();
		acceptEnabled = readC() == 1 ? true: false;
		
		int linkCount = readD();

		linkTexts = new String[linkCount];
		linkTypes = new int[linkCount];
		
		for (int i = 0; i < linkCount; i++) {
			linkTypes[i] = readD();
			linkTexts[i] = readS();
		}
		
		return true;
	}

	@Override
	public void run() {
		Dialog dialog = GameWorld.getInstance().game.screenGame.guiManager.dialog;
		dialog.updateDialog(name, text, linkTexts, linkTypes, pageId);
		dialog.setShowAccept(acceptEnabled);
	}

}
