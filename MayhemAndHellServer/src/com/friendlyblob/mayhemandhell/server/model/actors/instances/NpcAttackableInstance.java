package com.friendlyblob.mayhemandhell.server.model.actors.instances;

import com.friendlyblob.mayhemandhell.server.ai.AttackableAi;
import com.friendlyblob.mayhemandhell.server.ai.GameCharacterAi;
import com.friendlyblob.mayhemandhell.server.model.actors.NpcTemplate;

public class NpcAttackableInstance extends NpcInstance {
	
	public NpcAttackableInstance(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	@Override
	public synchronized void attachAi() {
		if (ai == null) {
			ai = new AttackableAi(this);
		}
	}
	
}
