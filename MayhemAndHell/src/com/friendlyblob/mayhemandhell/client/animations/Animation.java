package com.friendlyblob.mayhemandhell.client.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Represents a basic animation
 * @author Alvys
 *
 */
public class Animation {
	// Frame data
	private TextureRegion[] frames;
	private float[] frameStartMarks;
	
	// General animation data
	private AnimationHandler handler;
	private boolean looped;
	private float animationDuration;
	private int frameCount;
	
	// Animation state data
	private float currentTime;
	private int currentFrame;

	public Animation(float animationDuration, AnimationHandler handler, int frameCount) {
		this.animationDuration = animationDuration;
		this.handler = handler;
		this.frameCount = frameCount;
		currentTime = 0;
		currentFrame = 0;
		frames = new TextureRegion[frameCount];
		frameStartMarks = new float[frameCount];
	}
	
	/**
	 * Returns a frame that should be displayed after deltaTime from
	 * last frame. 
	 * @param deltaTime
	 * @return
	 */
	public TextureRegion getFrame(float deltaTime) {
		currentTime += deltaTime;
		if (currentTime > animationDuration) {
			
			if (!looped) {
				currentTime = 0;
				currentFrame = 0;
				handler.onAnimationFinished();
			}
			
			currentTime -= animationDuration;
		}
		return null;
	}
	
	/**
	 * Ads a new frame to the next free spot.
	 * @param texture
	 * @param startMark
	 */
	public void addFrame(TextureRegion texture, float startMark) {
		for (int i = 0; i < frameStartMarks.length; i++) {
			if (frames[i] == null) {
				frames[i] = texture;
				frameStartMarks[i] = startMark;
				return;
			}
		}
	}
	
	/**
	 * Returns current frame, does not affect animation flow.
	 * @return
	 */
	public TextureRegion getFrame() {
		for (int i = 0; i < frameStartMarks.length; i++) {
			if (frameStartMarks[i]*animationDuration >= currentTime) {
				currentFrame = i;
			} else {
				return frames[currentFrame];
			}
		}
		return frames[0];
	}
	
}
