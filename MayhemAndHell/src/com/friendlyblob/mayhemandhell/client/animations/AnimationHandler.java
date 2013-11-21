package com.friendlyblob.mayhemandhell.client.animations;

public abstract class AnimationHandler {
	
	/**
	 * Called when animation is finished. Looped animation will never
	 * call this method.
	 */
	public abstract void onAnimationFinished();
	
}
