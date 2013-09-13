package com.friendlyblob.mayhemandhell.server.model.stats;

public enum Stat {

	MAX_HP("maxHp"),
	MAX_MP("maxMp"),
	MAX_ENERGY("maxEnergy"),
	MAX_WEIGHT("maxWeight"),
	
	MOVEMENT_SPEED("movementSpeed"),

	PHYSICAL_DAMAGE("physicalDamage"),
	PHYSICAL_DEFENCE("physicalDefence"),
	MAGIC_DAMAGE("magicDamage"),
	MAGIC_DEFENSE("magicDefense"),

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
}
