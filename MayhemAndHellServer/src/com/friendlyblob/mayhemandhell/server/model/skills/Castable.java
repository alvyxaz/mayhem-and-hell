package com.friendlyblob.mayhemandhell.server.model.skills;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;

public interface Castable {
	public static final StopCasting STOP_CASTING = new StopCasting();
	
	public int getCastingTime();
	public String getName();
	public void execute(GameCharacter caster, GameObject target);
	
	public int getRange();
	
	/**
	 * "Spell" that represents a casting being cancelled
	 * @author Alvys
	 *
	 */
	public static class StopCasting implements Castable {

		@Override
		public int getCastingTime() {
			return 0;
		}

		@Override
		public String getName() {
			return "";
		}

		@Override
		public void execute(GameCharacter caster, GameObject target) {
		}

		@Override
		public int getRange() {
			return 10;
		}
		
	}
}
