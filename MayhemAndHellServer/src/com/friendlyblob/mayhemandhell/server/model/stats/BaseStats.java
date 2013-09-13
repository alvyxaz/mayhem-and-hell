package com.friendlyblob.mayhemandhell.server.model.stats;

/**
 * Represents a base for stat calculations. No matter what level
 * or what effects character has, this base stat remains the same.
 * @author Alvys
 *
 */
public class BaseStats {

	public final int baseMaxHealth;
	public final int baseMaxMana;
	public final int baseMaxEnergy;
	public final int baseMaxWeight;
	
	public final int baseMovementSpeed;

	public final int basePhysicalDamage;
	public final int basePhysicalDefence;
	public final int baseMagicDamage;
	public final int baseMagicDefence;

	
	// ATTRIBUTES
	public final int strength;
	public final int dexterity;
	public final int intelligence;
	public final int vitality;
	
	/**
	 * Might need to be read from a file or something, instead of hardcoding.
	 * @param set
	 */
	public BaseStats(StatsSet set) {
		baseMaxHealth = set.getInteger("baseMaxHealth", 50);
		baseMaxMana = set.getInteger("baseMaxEnergy", 100);
		baseMaxEnergy = set.getInteger("baseMaxEnergy", 100);
		baseMaxWeight = set.getInteger("baseMaxWeight", 100);
		
		baseMovementSpeed = set.getInteger("baseWalkingSpeed", 100);
		
		basePhysicalDamage = set.getInteger("basePhysicalDamage", 10);
		basePhysicalDefence = set.getInteger("basePhysicalDefence", 10);
		baseMagicDamage = set.getInteger("baseMagicDamage", 10);
		baseMagicDefence = set.getInteger("baseMagicDefence", 10);
		
		// ATTRIBUTES
		strength = set.getInteger("strength", 10);
		dexterity = set.getInteger("dexterity", 10);
		intelligence = set.getInteger("intelligence", 10);
		vitality = set.getInteger("vitality", 10);
		
	}
	
}
