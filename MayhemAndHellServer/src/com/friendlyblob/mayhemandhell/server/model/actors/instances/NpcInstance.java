package com.friendlyblob.mayhemandhell.server.model.actors.instances;

import com.friendlyblob.mayhemandhell.server.model.Spawn;
import com.friendlyblob.mayhemandhell.server.model.actors.CharacterTemplate;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.actors.NpcTemplate;

public class NpcInstance extends GameCharacter {

	private Spawn spawn;
	
	public NpcInstance(int objectId, NpcTemplate template) {
		super(objectId, template);

		this.setName(template.name + "-" + objectId);
		this.setType(GameObjectType.HOSTILE_NPC); // TODO ability to change to either friendly or hostile
	}
	
	public Spawn getSpawn() {
		return spawn;
	}
	
	public void setSpawn(Spawn spawn) {
		this.spawn = spawn;
	}
	
}
