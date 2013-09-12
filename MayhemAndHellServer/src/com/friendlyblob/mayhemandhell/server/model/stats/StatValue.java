package com.friendlyblob.mayhemandhell.server.model.stats;

/**
 * Represents a value of the stat.
 * This class is used to store all the calculation values made by StatModifier
 * 
 * @author Alvys
 *
 */
public class StatValue {
	private int baseStat;				// Base value
	private float baseMultiplier = 1;	// Base value multiplier
	private int addition = 0;			// Get's added after baseMultiplier
	private float endMultiplier = 1;	// Multiplies the final outcome
	
	public StatValue (int baseStat) {
		this.baseStat = baseStat;
	}
	
	public int getValue() {
		return (int)(((baseStat * baseMultiplier) + addition) * endMultiplier);
	}
	
	/**
	 * Adds a value to baseStat. This change to baseStat WILL affect further 
	 * calculations that are based on baseStat
	 * 
	 * @param offset 
	 */
	public void addToBase(int offset) {
		baseStat += offset;
	}
	
	/**
	 * Increases baseStat multiplier. After it is applied to baseStat,
	 * changes WILL affect further calculations that are based on baseStat.
	 * 
	 * @param offset
	 */
	public void addToBaseMultiplier(float offset) {
		baseMultiplier += offset;
	}
	
	/**
	 * Increases addition to value. This stat DOES NOT change
	 * baseStat, but it WILL have an affect to endMultiplier result.
	 */
	public void addToAddition(int offset) {
		addition += offset;
	}
	
	/**
	 * Increases the endMultiplier. This does not affect any other calculations.
	 */
	public void addToEndMultiplier(float offset) {
		endMultiplier += offset;
	}
}
