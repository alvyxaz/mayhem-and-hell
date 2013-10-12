package com.friendlyblob.mayhemandhell.server.ai;

import java.util.Random;

/**
 * Stores information about a simple Ai. Most likely used for
 * an NPC that is instance of NpcAttackable.
 * @author Alvys
 *
 */
public class AiData {
	private boolean aggressive;
	
	private int randomWalkRate = 5;			// The higher, the less likely to walk
	private int maxSingleWalkRadius = 50; 	// Pixels
	
	private static Random random = new Random();
	
	public boolean isAggressive() {
		return false;
	}
	
	public boolean isWalkingRandomly() {
		return randomWalkRate > 0;
	}
	
	public int getRandomWalkRate() {
		return randomWalkRate;
	}
	
	public int getMaxSingleWalkRadius() {
		return maxSingleWalkRadius;
	}

}
