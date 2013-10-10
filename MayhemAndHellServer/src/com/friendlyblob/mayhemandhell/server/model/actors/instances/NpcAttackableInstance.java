package com.friendlyblob.mayhemandhell.server.model.actors.instances;

import com.friendlyblob.mayhemandhell.server.ai.GameCharacterAi;
import com.friendlyblob.mayhemandhell.server.model.actors.NpcTemplate;

public class NpcAttackableInstance extends NpcInstance {
	
	public NpcAttackableInstance(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	public GameCharacterAi getAi() {
		GameCharacterAi tempAi = ai;
		
		if (tempAi == null) {
			synchronized(this) {
				if (ai == null) {
					ai = new GameCharacterAi(this);
				}
				return ai;
			}
		}
		
		return tempAi;
	}
	
}
