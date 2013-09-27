package com.friendlyblob.mayhemandhell.server.model.stats;

import java.util.NoSuchElementException;

public enum Stat {

	MAX_HP("maxHp"),
	MAX_MP("maxMp"),
	MAX_ENERGY("maxEnergy"),
	MAX_WEIGHT("maxWeight"),
	
	MOVEMENT_SPEED("movementSpeed"),

	PHYSICAL_DAMAGE("pDamage"),
	PHYSICAL_DEFENCE("pDefence"),
	MAGIC_DAMAGE("mDamage"),
	MAGIC_DEFENSE("mDefence"),

	STRENGTH("strength"),
	DEXTERITY("dexterity"),
	INTELLIGENCE("intelligence"),
	CONSTITUTION("constitution");
	
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
