package com.friendlyblob.mayhemandhell.server.model.actors;

import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;

public class CharacterTemplate {

	// Vital stats
	private final int baseMaxHealth;
	private final int baseMaxMana;
	private final int baseMaxEnergy;
	private final int baseMaxWeight;
	
	// Movement
	private final int baseWalkingSpeed;
	private final int baseRunningSpeed;
	
	// Core stats
	private final int baseStrength;
	private final int baseDexterity;
	private final int baseIntelligence;
	private final int baseVitality;
	private final int baseToughness;
	
	// Main damage
	private final int basePhysicalDamage;
	private final int baseMagicDamage;
	private final int baseRangeDamage;
	
	// Main defence
	private final int basePhysicalDefence;
	private final int baseMagicalDefence;
	private final int baseRangeDefence;
	
	// Elemental resistance
	private final int baseFireResistance;
	private final int baseFrostResistance;
	private final int baseElectricityResistance;
	private final int basePoisonResistance;
	private final int baseHolyResistance;
	
	// Elemental damage
	private final int baseFireDamage;
	private final int baseFrostDamage;
	private final int baseElectricDamage;
	private final int basePoisonDamage;
	private final int baseHolyDamage;
	
	public CharacterTemplate(StatsSet set) {
		baseMaxHealth = set.getInteger("baseMaxHealth", 100);
		baseMaxMana = set.getInteger("baseMaxMana", 100);
		baseMaxEnergy = set.getInteger("baseMaxEnergy", 100);
		baseMaxWeight = set.getInteger("baseMaxWeight", 100);
		
		baseWalkingSpeed = set.getInteger("baseWalkingSpeed", 100);
		baseRunningSpeed = set.getInteger("baseRunningSpeed", 100);
		
		baseStrength = set.getInteger("baseStrength", 10);
		baseDexterity = set.getInteger("baseDexterity", 10);
		baseIntelligence = set.getInteger("baseIntelligence", 10);
		baseVitality = set.getInteger("baseVitality", 10);
		baseToughness = set.getInteger("baseToughness", 10);
		
		basePhysicalDamage = set.getInteger("basePhysicalDamage", 10);
		baseMagicDamage = set.getInteger("baseMagicDamage", 10);
		baseRangeDamage = set.getInteger("baseRangeDamage", 10);
		
		basePhysicalDefence = set.getInteger("basePhysicalDefence", 0);
		baseMagicalDefence = set.getInteger("baseMagicalDefence", 0);
		baseRangeDefence = set.getInteger("baseRangeDefence", 0);
		
		baseFireResistance = set.getInteger("baseFireResistance", 0);
		baseFrostResistance = set.getInteger("baseFrostResistance", 0);
		baseElectricityResistance = set.getInteger("baseElectricityResistance", 0);
		basePoisonResistance = set.getInteger("basePoisonResistance", 0);
		baseHolyResistance = set.getInteger("baseHolyResistance", 0);
		
		baseFireDamage = set.getInteger("baseFireDamage", 0);
		baseFrostDamage = set.getInteger("baseFrostDamage", 0);
		baseElectricDamage = set.getInteger("baseElectricDamage", 0);
		basePoisonDamage = set.getInteger("basePoisonDamage", 0);
		baseHolyDamage = set.getInteger("baseHolyDamage", 0);
	}

	/**
	 * @return the baseMaxHealth
	 */
	public int getBaseMaxHealth() {
		return baseMaxHealth;
	}

	/**
	 * @return the baseMaxMana
	 */
	public int getBaseMaxMana() {
		return baseMaxMana;
	}

	/**
	 * @return the baseMaxEnergy
	 */
	public int getBaseMaxEnergy() {
		return baseMaxEnergy;
	}

	/**
	 * @return the baseMaxWeight
	 */
	public int getBaseMaxWeight() {
		return baseMaxWeight;
	}

	/**
	 * @return the baseWalkingSpeed
	 */
	public int getBaseWalkingSpeed() {
		return baseWalkingSpeed;
	}

	/**
	 * @return the baseRunningSpeed
	 */
	public int getBaseRunningSpeed() {
		return baseRunningSpeed;
	}

	/**
	 * @return the baseStrength
	 */
	public int getBaseStrength() {
		return baseStrength;
	}

	/**
	 * @return the baseDexterity
	 */
	public int getBaseDexterity() {
		return baseDexterity;
	}

	/**
	 * @return the baseIntelligence
	 */
	public int getBaseIntelligence() {
		return baseIntelligence;
	}

	/**
	 * @return the baseVitality
	 */
	public int getBaseVitality() {
		return baseVitality;
	}

	/**
	 * @return the baseToughness
	 */
	public int getBaseToughness() {
		return baseToughness;
	}

	/**
	 * @return the basePhysicalDamage
	 */
	public int getBasePhysicalDamage() {
		return basePhysicalDamage;
	}

	/**
	 * @return the baseMagicDamage
	 */
	public int getBaseMagicDamage() {
		return baseMagicDamage;
	}

	/**
	 * @return the baseRangeDamage
	 */
	public int getBaseRangeDamage() {
		return baseRangeDamage;
	}

	/**
	 * @return the basePhysicalDefence
	 */
	public int getBasePhysicalDefence() {
		return basePhysicalDefence;
	}

	/**
	 * @return the baseMagicalDefence
	 */
	public int getBaseMagicalDefence() {
		return baseMagicalDefence;
	}

	/**
	 * @return the baseRangeDefence
	 */
	public int getBaseRangeDefence() {
		return baseRangeDefence;
	}

	/**
	 * @return the baseFireResistance
	 */
	public int getBaseFireResistance() {
		return baseFireResistance;
	}

	/**
	 * @return the baseFrostResistance
	 */
	public int getBaseFrostResistance() {
		return baseFrostResistance;
	}

	/**
	 * @return the baseElectricityResistance
	 */
	public int getBaseElectricityResistance() {
		return baseElectricityResistance;
	}

	/**
	 * @return the basePoisonResistance
	 */
	public int getBasePoisonResistance() {
		return basePoisonResistance;
	}

	/**
	 * @return the baseHolyResistance
	 */
	public int getBaseHolyResistance() {
		return baseHolyResistance;
	}

	/**
	 * @return the baseFireDamage
	 */
	public int getBaseFireDamage() {
		return baseFireDamage;
	}

	/**
	 * @return the baseFrostDamage
	 */
	public int getBaseFrostDamage() {
		return baseFrostDamage;
	}

	/**
	 * @return the baseElectricDamage
	 */
	public int getBaseElectricDamage() {
		return baseElectricDamage;
	}

	/**
	 * @return the basePoisonDamage
	 */
	public int getBasePoisonDamage() {
		return basePoisonDamage;
	}

	/**
	 * @return the baseHolyDamage
	 */
	public int getBaseHolyDamage() {
		return baseHolyDamage;
	}
	
	
	
}
