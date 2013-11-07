package com.friendlyblob.mayhemandhell.server.model.datatables;

import java.util.Map;

import javolution.util.FastMap;

import com.friendlyblob.mayhemandhell.server.data.DialogDataParser;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog;

public class DialogTable {
	private static DialogTable instance;
	
	private Map<Integer, Dialog> dialogs;
	
	public static void initialize() {
		instance = new DialogTable();
	}
	
	public DialogTable() {
		dialogs = new FastMap<>();
		load();
	}
	
	public void load() {
		DialogDataParser parser = new DialogDataParser();
		parser.load();
		
		for (Dialog dialog : parser.getDialogs()) {
			dialogs.put(dialog.getId(), dialog);
		}
	}
	
	public Dialog getDialog(int index) {
		return dialogs.get(index);
	}
	
	public static DialogTable getInstance() {
		return instance;
	}
	
}
