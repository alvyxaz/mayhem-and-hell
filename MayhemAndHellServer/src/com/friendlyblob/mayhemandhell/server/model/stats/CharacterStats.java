package com.friendlyblob.mayhemandhell.server.model.stats;

import com.friendlyblob.mayhemandhell.server.model.actors.GameCharacter;
import com.friendlyblob.mayhemandhell.server.model.stats.StatModifier.StatModifierType;

/**
 * 
 * TODO possible optimization by caching calculations.
 * Calculations are based on a fact that every instance of an item has
 * a different StatModifier, even though it's logically the same. This
 * way we prevent duplicate stats from one item, but we waste some memory.
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
		if (modifier == null || modifierExists(modifier)) {
			return;
		}
		
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
	 * Checks whether a given modifier already exists
	 * @param modifier
	 * @return 
	 */
	public boolean modifierExists(StatModifier modifier) {
		StatModifier [] modifiers = this.modifiers[modifier.getStat().ordinal()];
		for (int i = 0; i < modifiers.length; i++) {
			if (modifiers[i].equals(modifier)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds multiple stat modifiers from a given array
	 * @param modifiers
	 */
	public void addStatModifiers(StatModifier [] modifiers) {
		if (modifiers == null) {
			return;
		}
		for (int i = 0; i < modifiers.length; i++) {
			addStatModifier(modifiers[i]);
		}
	}
	
	/**
	 * Removes multiple stats that are given in an array
	 * @param modifiers
	 */
	public void removeStatModifiers(StatModifier [] modifiers) {
		if (modifiers == null) {
			return;
		}
		
		for (int i = 0; i < modifiers.length; i++) {
			removeStatModifier(modifiers[i]);
		}
	}
	

	/**
	 * Removes modifier from modifiers list
	 * @param modifier Modifier to be removed
	 */
	public synchronized void removeStatModifier (StatModifier modifier) {
		if (modifier == null || !modifierExists(modifier)) {
			return;
		}
		
		final int statId = modifier.getStat().ordinal();
		
		StatModifier [] modifiers = this.modifiers[statId];
		StatModifier [] temp = new StatModifier[modifiers.length -1];
		
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
		return calculateInt(Stat.WALKING_SPEED, activeCharacter.getTemplate().getBaseWalkingSpeed());
	}
	
	public int getMaxHealth() {
		return calculateInt(Stat.MAX_HEALTH, activeCharacter.getTemplate().getBaseMaxHealth());
	}
	
	public int getMaxEnergy() {
		return calculateInt(Stat.MAX_ENERGY, activeCharacter.getTemplate().getBaseMaxEnergy());
	}
	
	public int getMaxMana() {
		return calculateInt(Stat.MAX_MANA, activeCharacter.getTemplate().getBaseMaxMana());
	}
	
}
