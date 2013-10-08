package com.friendlyblob.mayhemandhell.server.ai;

/**
 * Represents a general type of event 
 * (actor can get stunned by multiple sources, but the event is the same)
 * @author Alvys
 *
 */
public enum Event {
	
	/**
	 * Most likely to analyze what to do next (after a certain step is completed and etc.)
	 */
	THINK,
	/**
	 * Character get's this notification after being attacked (hit by
	 * a melee attack or a spell).
	 */
	ATTACKED, 
	/**
	 * Actor is in a "Stunned" state 
	 */
	STUNNED,		// Cannot do anything
	SLEEPING,		// Cannot do anything, but effect stops after taking damage
	ROOTED,			// Cannot move
	SILENCED,		// Cannot cast spells
	EVADED,			// Evaded an attack
	READY_TO_ACT,	// Previous action is completed, ready for a another
	USSER_COMMAND,	// Change weapon, etc.
	ARRIVED,		// Arrived at the destination
	/**
	 * Used to cancel spell casting, movement or some other stuff.
	 * For example, if actor is stunned, he should cancel he's attack,
	 * and continue attacking after he breaks out of stun.
	 */
	CANCEL,
	DEAD,
	CONFUSED,
	AGGRESSION,		// Aggression towars a target changed
	AFRAID			// Running to random directions
	
}
