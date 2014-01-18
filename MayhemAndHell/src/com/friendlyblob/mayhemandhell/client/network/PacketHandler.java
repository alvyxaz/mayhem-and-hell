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
				response = new ObjectAppeared();
				break;
			case 0x04:
				response = new ObjectsInRegion();
				break;
			case 0x05:
				response = new ObjectsLeft();
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
			case 0x0F:
				response = new EventNotification();
				break;
			case 0x10:
				response = new StartCasting();
				break;
			case 0x11:
				response = new ShowShop();
				break;
			case 0x12:
				response = new UpdateCharacterHint();
				break;
			case 0x13:
				response = new UpdateInventorySlot();
				break;
			case 0x14:
				response = new RegistrationSuccessful();
				break;
			case 0x15:
				response = new RegistrationFailure();
				break;
			case 0x16:
				response = new LoginFailure();
				break;
				
			case 0xA0:
				response = new ChatMessageNotification();
				break;
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
