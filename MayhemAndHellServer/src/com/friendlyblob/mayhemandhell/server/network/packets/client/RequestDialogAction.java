package com.friendlyblob.mayhemandhell.server.network.packets.client;

import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.model.actors.instances.NpcInstance;
import com.friendlyblob.mayhemandhell.server.model.datatables.DialogTable;
import com.friendlyblob.mayhemandhell.server.model.dialogs.Dialog.DialogPage;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.DialogPageInfo;

public class RequestDialogAction extends ClientPacket {

	@Override
	protected boolean read() {
//		int index = readD();
//		Player player = getClient().getPlayer();
//		if (player.getTarget() instanceof NpcInstance) {
//			int dialog = ((NpcInstance) player.getTarget()).getTemplate().set.getInteger("dialog", -1);
//			if (dialog != -1) {
//				DialogPage page = DialogTable.getInstance().getDialog(dialog).getPage(index);
//				if (page != null) {
//					player.sendPacket(new DialogPageInfo(player.getTarget().getName(), page));
//				}
//				
//			}
//		}
		
		return false;
	}

	@Override
	public void run() {
		
	}

}
