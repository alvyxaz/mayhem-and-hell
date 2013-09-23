package com.friendlyblob.mayhemandhell.server.network;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import org.mmocore.network.IClientFactory;
import org.mmocore.network.IMMOExecutor;
import org.mmocore.network.IPacketHandler;
import org.mmocore.network.MMOConnection;
import org.mmocore.network.ReceivablePacket;

import com.friendlyblob.mayhemandhell.server.network.GameClient.GameClientState;
import com.friendlyblob.mayhemandhell.server.network.packets.client.*;


public class GamePacketHandler implements IPacketHandler<GameClient>, 
						IClientFactory<GameClient>, IMMOExecutor<GameClient>{

	private static final Logger log = Logger.getLogger(GamePacketHandler.class.getName());
	
	@Override
	public void execute(ReceivablePacket<GameClient> receivedPacket) {
		receivedPacket.getClient().execute(receivedPacket);
	}

	@Override
	public GameClient create(MMOConnection<GameClient> connection) {
		return new GameClient(connection);
	}

	@Override
	public ReceivablePacket<GameClient> handlePacket(ByteBuffer buf,
			GameClient client) {
		
		if (client.dropPacket()){
			return null;
		}
		
		// Operation code
		int opcode = buf.get() & 0xFF;
		
		ReceivablePacket<GameClient> response = null;
		GameClientState state = client.getState();
		
		switch (state) {
			case CONNECTED:
				switch (opcode) {
					case 0x01:
						response = new ClientVersion();
						break;
					case 0x02:
						response = new LoginPacket();
						break;
				}
				break;
			case AUTHORIZED:
				switch (opcode) {
					case 0x01:
						response = new NotifyReadyToPlay();
						break;
				}
				break;
			case IN_GAME:
				switch (opcode) {
					case 0x01:
						response = new RequestMove();
						break;
					case 0x02:
						response = new RequestTarget();
						break;
				}
				break;
		}
		
		// TODO has to be able to go through with null for response
		
		return response;
	} 


}
