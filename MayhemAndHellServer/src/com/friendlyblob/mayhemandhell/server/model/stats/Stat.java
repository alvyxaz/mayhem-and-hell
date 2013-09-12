package com.friendlyblob.mayhemandhell.server.model.stats;

public enum Stat {

	MAX_HP("maxHp"),
	MAX_ENERGY("maxEnergy"),
	WALKING_SPEED("walkingSpeed"),
	RUNNING_SPEED("runningSpeed");
	
	public static final int COUNT = values().length;
	
	private String value;
	
	private Stat(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
