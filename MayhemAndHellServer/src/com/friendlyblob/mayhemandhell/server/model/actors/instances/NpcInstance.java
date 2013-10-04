package com.friendlyblob.mayhemandhell.server.model.actors.instances;

import com.friendlyblob.mayhemandhell.server.model.actors.CharacterTemplate;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.NpcTemplate;

public class NpcInstance extends GameCharacter {

	public NpcInstance(int objectId, NpcTemplate template) {
		super(objectId, template);
		
		this.setName(template.name + "-" + objectId);
		this.setType(GameObjectType.HOSTILE_NPC); // TODO ability to change to either friendly or hostile
	}
	
}