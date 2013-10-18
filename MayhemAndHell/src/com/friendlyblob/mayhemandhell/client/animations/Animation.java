package com.friendlyblob.mayhemandhell.client.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Represents a basic animation instance
 * @author Alvys
 *
 */
public class Animation {
	// General animation data
	private AnimationHandler handler;
	
	// Animation state data
	private float currentTime;
	private int currentFrame;

	public AnimationData data;
	
	public Animation(AnimationData animationData, AnimationHandler handler) {
		this.data = animationData;
		this.handler = handler;
		currentTime = 0;
		currentFrame = 0;
	}
	
	/**
	 * Returns a frame that should be displayed after deltaTime from
	 * last frame. 
	 * @param deltaTime
	 * @return
	 */
	public TextureRegion getFrame(float deltaTime) {
		currentTime += deltaTime;
		if (currentTime > this.data.duration) {
			
			if (!this.data.looped) {
				currentTime = 0;
				currentFrame = 0;
				handler.onAnimationFinished();
			}
			
			currentTime -= this.data.duration;
		}
		return getFrame();
	}
	
	/**
	 * Restores animation to the start
	 */
	public void restart() {
		currentTime = 0;
		currentFrame = 0;
	}

	
	/**
	 * Returns current frame, does not affect animation flow.
	 * @return
	 */
	public TextureRegion getFrame() {
		for (int i = 0; i < data.frameStartMarks.length; i++) {
			if (data.frameStartMarks[i]*this.data.duration <= currentTime) {
				currentFrame = i;
			} else {
				return data.frames[currentFrame];
			}
		}
		return data.frames[currentFrame];
	}
	
	/**
	 * Repesents a static animation data holder
	 * @author Alvys
	 *
	 */
	public static class AnimationData {
		public String type;
		public TextureRegion[] frames;
		public float[] frameStartMarks;
		public boolean looped;
		public float duration;
		public int frameCount;
	}
	
}
