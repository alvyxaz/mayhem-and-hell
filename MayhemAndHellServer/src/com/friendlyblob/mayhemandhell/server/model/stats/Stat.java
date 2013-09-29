package com.friendlyblob.mayhemandhell.server.model.stats;

import java.util.NoSuchElementException;

public enum Stat {

	// Vital stats
	MAX_HEALTH("maxHp"),
	MAX_MANA("maxMp"),
	MAX_ENERGY("maxEnergy"),
	MAX_WEIGHT("maxWeight"),
	
	// Movement stats
	WALKING_SPEED("walkingSpeed"),
	RUNNING_SPEED("runningSpeed"),

	// Core stats
	STRENGTH("strength"),
	DEXTERITY("dexterity"),
	INTELLIGENCE("intelligence"),
	VITALITY("vitality"),
	TOUGHNESS("toughness"),
	
	// Main damage
	PHYSICAL_DAMAGE("pDamage"),
	MAGIC_DAMAGE("mDamage"),
	RANGE_DAMAGE("rDamage"),
	
	// Main defence
	PHYSICAL_DEFENCE("pDefence"),
	MAGIC_DEFECE("mDefence"),
	RANGE_DEFENCE("rDefence"),
	
	// Elemental resistance
	FIRE_RESISTANCE("fireResistance"),
	FROST_RESISTANCE("frostResistance"),
	ELECTRICITY_RESISTANCE("electricityResistance"),
	POISON_RESISTANCE("poisonResistance"),
	HOLY_RESISTANCE("holyResistance"),
	
	// Elemental damage
	FIRE_DAMAGE("fireDamage"),
	FROST_DAMAGE("frostDamage"),
	ELECTRIC_DAMAGE("electricDamage"),
	POISON_DAMAGE("poisonDamage"),
	HOLY_DAMAGE("holyDamage");

	public static final int COUNT = values().length;
	
	private String value;
	
	private Stat(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static Stat valueOfXml(String value) {
		for (Stat s : values()) {
			if (s.getValue().equals(value)) {
				return s;
			}
		}
		
		throw new NoSuchElementException("Unknown name '" + value + "' for enum BaseStats");
	}
}
