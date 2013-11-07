package com.friendlyblob.mayhemandhell.client.network;

import java.nio.ByteBuffer;

import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;
import com.friendlyblob.mayhemandhell.client.network.packets.server.*;

public class PacketHandler {

	public ReceivablePacket handlePacket(ByteBuffer buf) {
		// Operation code
		int opcode = buf.get() & 0xFF;
		ReceivablePacket response = null;
		
		switch(opcode){
			case 0x01:
				response = new KeyPacket();
				break;
			case 0x02:
				response = new LoginSuccessful();
				break;
			case 0x03:
				response = new CharacterAppeared();
				break;
			case 0x04:
				response = new CharactersInRegion();
				break;
			case 0x05:
				response = new CharactersLeft();
				break;
			case 0x06:
				response = new NotifyCharacterMovement();
				break;
			case 0x07:
				response = new TargetInfoResponse();
				break;
			case 0x08:
				response = new NotifyMovementStop();
				break;
			case 0x09:
				response = new AutoAttack();
				break;
			case 0x0A:
				response = new Attack();
				break;
			case 0X0B:
				response = new CharacterStatusUpdate();
				break;
			case 0X0C:
				response = new DeathNotification();
				break;
			case 0x0D:
				response = new DialogPageInfo();
				break;
			case 0xA0:
				response = new ChatMessageNotify();
				break;
				
			// Don't add any packets below this line
			case 0xFF:
				response = new ActionFailedMessage();
				break;
			default:
				response = new UnknownPacket();
				break;
		}
		
		
		return response;
	} 
	
}
