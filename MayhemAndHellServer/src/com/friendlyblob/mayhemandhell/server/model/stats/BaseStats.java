package com.friendlyblob.mayhemandhell.server.model.stats;

/**
 * Represents a base for stat calculations. No matter what level
 * or what effects character has, this base stat remains the same.
 * @author Alvys
 *
 */
public class BaseStats {

	public final int baseWalkingSpeed;
	public final int baseRunningSpeed;
	public final int baseMaxHealth;
	public final int baseMaxEnergy;
	
	/**
	 * Might need to be read from a file or something, instead of hardcoding.
	 * @param set
	 */
	public BaseStats(StatsSet set) {
		baseWalkingSpeed = set.getInteger("baseWalkingSpeed", 100);
		baseRunningSpeed = set.getInteger("baseRunningSpeed", 200);
		baseMaxHealth = set.getInteger("baseMaxHealth", 50);
		baseMaxEnergy = set.getInteger("baseMaxEnergy", 100);
	}
	
}
