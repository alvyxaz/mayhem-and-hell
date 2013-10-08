package com.friendlyblob.mayhemandhell.server.ai;

/**
 * Represents an intenion of a character (sort of like a generic
 * action, which can be split into multiple smaller actions, for example:
 * Attacking a character might involve shooting from a distance, melee attack,
 * or casting a spell.)
 * @author Alvys
 *
 */
public enum Intention {
	IDLE,
	ACTIVE,		// Scan attackable targets, walk randomly and etc.
	REST,		// Wait to be attacked
	ATTACK,		// Attack a target
	CAST,		// Cast some sort of spell
	MOVE_TO,	// Move to a specific location
	FOLLOW,		// Follow a target
	PICK_UP,	// Pick up an item and go idle 
	INTERACT	// Move to target, then interact
}
