package com.friendlyblob.mayhemandhell.client.animations;

public interface AnimationHandler {
	
	/**
	 * Called when animation is finished. Looped animation will never
	 * call this method.
	 */
	public void onAnimationFinished();
	
}
