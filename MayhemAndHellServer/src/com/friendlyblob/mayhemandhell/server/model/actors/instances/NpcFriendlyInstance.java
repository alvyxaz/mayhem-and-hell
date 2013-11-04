package com.friendlyblob.mayhemandhell.server.model.actors.instances;

import com.friendlyblob.mayhemandhell.server.model.actors.NpcTemplate;

public class NpcFriendlyInstance extends NpcInstance {

	public NpcFriendlyInstance(int objectId, NpcTemplate template) {
		super(objectId, template);
		this.setType(GameObjectType.FRIENDLY_NPC);
		this.restoreVitals();
	}

}
