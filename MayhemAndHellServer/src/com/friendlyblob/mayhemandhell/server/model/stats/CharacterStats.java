package com.friendlyblob.mayhemandhell.server.model.stats;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.stats.StatModifier.StatModifierType;

/**
 * 
 * TODO possible optimization by caching calculations
 * 
 * @author Alvys
 *
 */
public class CharacterStats {

	private GameCharacter activeCharacter;
	
	// Array of modifiers for every stat
	private StatModifier [][] modifiers;
	private StatModifier [] emptyModifiers = new StatModifier[0];
	
	public CharacterStats(GameCharacter character) {
		this.activeCharacter = character;
		modifiers = new StatModifier[Stat.COUNT][0];
	}
	
	/**
	 * Applies modifiers to integer stat and returns it's value
	 * 
	 * @param stat Stat to be returned
	 * @param baseStat base stat of the character
	 * @return integer value of the stat
	 */
	public int calculateInt(Stat stat, int baseStat) {
		int ordinal = stat.ordinal();
		
		StatValue value = new StatValue(baseStat);
		
		for (int i = 0; i < modifiers[ordinal].length; i++) {
			modifiers[ordinal][i].apply(value);
		}
		
		return value.getValue();
		
	}

	
	/**
	 * Adds a stat modifier to modifier list
	 * @param modifier Modifier to be added
	 */
	public synchronized void addStatModifier(StatModifier modifier) {
		final int statId = modifier.getStat().ordinal();
		
		StatModifier [] modifiers = this.modifiers[statId];
		StatModifier [] temp = new StatModifier[modifiers.length +1];
		
		int i;
		int priority = modifier.getPriority();
		
		for(i = 0; (i < modifiers.length) && (priority >= modifiers[i].getPriority()); i++) {
			temp[i] = modifiers[i];
		}
		
		temp[i] = modifier;
		
		for (; i < modifiers.length; i++) {
			temp[i + 1] = modifiers[i];
		}
		
		this.modifiers[statId] = temp;
	}
	

	/**
	 * Removes modifier from modifiers list
	 * @param modifier Modifier to be removed
	 */
	public synchronized void removeStatModifier (StatModifier modifier) {
		final int statId = modifier.getStat().ordinal();
		
		StatModifier [] modifiers = this.modifiers[statId];
		StatModifier [] temp = new StatModifier[modifiers.length +1];
		
		int i;
		
		for (i = 0; (i < modifiers.length) && (modifier != modifiers[i]); i++) {
			temp[i] = modifiers[i];
		}
		
		if (i == modifiers.length) {
			this.modifiers[statId] = temp;
			return;
		}
		
		for (i++; i < modifiers.length; i++) {
			temp[i - 1] = modifiers[i];
		}
		
		if (temp.length == 0) {
			this.modifiers[statId] = emptyModifiers;
		} else {
			this.modifiers[statId] = temp;
		}
	}

	public int getWalkingSpeed() {
		return calculateInt(Stat.WALKING_SPEED, activeCharacter.getBaseStats().baseWalkingSpeed);
	}
	
}
