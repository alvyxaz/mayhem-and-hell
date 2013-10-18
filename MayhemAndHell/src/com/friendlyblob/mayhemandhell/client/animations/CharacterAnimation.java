package com.friendlyblob.mayhemandhell.client.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.friendlyblob.mayhemandhell.client.animations.Animation.AnimationData;

public class CharacterAnimation implements AnimationHandler {

	private Animation[] animations;
	
	private Animation lastLooped;
	private Animation current;
	
	public enum CharacterAnimationType {
		DEFAULT,
		
		// Walking
		WALKING_UP,
		WALKING_DOWN,
		WALKING_LEFT,
		WALKING_RIGHT,
		
		// Attack
		ATTACK_UP,
		ATTCK_DOWN,
		ATTACK_LEFT,
		ATTACK_RIGHT,
		
		// Idle
		IDLE_UP,
		IDLE_DOWN,
		IDLE_LEFT,
		IDLE_RIGHT,
		
		// Death
		DEATH
	}
	
	public CharacterAnimation() {
		animations = new Animation[CharacterAnimationType.values().length];
	}
	
	public boolean test = false;
	
	public void setAnimation(CharacterAnimationType type, Animation animation) {
		animations[type.ordinal()] = animation;
		if (type.DEFAULT == type) {
			test = true;
		}
	}
	
	/**
	 * Play a certain type of animation
	 * @param type
	 */
	public void play(CharacterAnimationType type) {
		play(animations[type.ordinal()]);
	}
	
	/**
	 * Sets a new animation to current
	 * @param animation
	 */
	public void play(Animation animation) {
		if (current == animation && !current.data.looped) {
			animation.restart();
			return;
		}
		current = animation;
	}
	
	/**
	 * @param deltaTime
	 * @return a frame of an animation that is being played
	 */
	public TextureRegion getFrame(float deltaTime) {
		if (current == null) {
			current = animations[CharacterAnimationType.DEFAULT.ordinal()];
		}
		return current.getFrame(deltaTime);
	}
	
	@Override
	public void onAnimationFinished() {
		// Play the last looped animation (idle after attack and etc.)
		play(lastLooped);
	}
	
}
