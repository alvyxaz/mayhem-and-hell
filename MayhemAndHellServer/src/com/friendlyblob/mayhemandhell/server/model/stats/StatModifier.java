package com.friendlyblob.mayhemandhell.server.model.stats;

/**
 * StatModifier represents a basic type of calculation needed to 
 * modify a Stat.
 * It's hardcoded for INCREMENT and MULTIPLY types, so if adding
 * a new type, make sure to go through multiplier application code.
 * 
 * @author Alvys
 *
 */
public class StatModifier {
	
	private float offset;
	private boolean disabled;
	private StatModifierType type;
	private Stat stat;
	private int priority;	// The higher, the sooner applied
	
	public enum StatModifierType {
		INCREMENT_BASE(4),	// Adds a modifier offset 
		MULTIPLY_BASE(3),	// Multiplies final baseStat
		INCREMENT(2),		// Addition after baseStat is fully set
		MULTIPLY(1);		// Final multiplier
		
		private int priority;
		
		StatModifierType(int priority) {
			this.priority = priority;
		}
		
		public int getPriority() {
			return priority;
		}
	}
	
	public StatModifier (Stat stat, StatModifierType type, float value) {
		this.offset = value;
		this.type = type;
		this.stat = stat;
		this.priority = type.getPriority();
	}
	
	public void apply(StatValue value) {
		switch (type) {
			case INCREMENT_BASE:
				value.addToBase((int)offset);
				break;
			case MULTIPLY_BASE:
				value.addToBaseMultiplier(offset);
				break;
			case INCREMENT:
				value.addToAddition((int)offset);
				break;
			case MULTIPLY:
				value.addToEndMultiplier(offset);
				break;
		}
	}
	
	public int getPriority() {
		return priority;
	}
	
	public Stat getStat() {
		return stat;
	}

	
	public void disable() {
		this.disabled = true;
	}
	
}
