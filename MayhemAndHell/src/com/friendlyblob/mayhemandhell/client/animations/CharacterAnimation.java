package com.friendlyblob.mayhemandhell.client.animations;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.friendlyblob.mayhemandhell.client.animations.Animation.AnimationData;

public class CharacterAnimation extends AnimationHandler {

	private HashMap<String, Animation> animations;
	
	private Animation lastLooped;
	private Animation current;
	
	private boolean twoDirectional = false;
	
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
	
	public boolean isTwoDirectional() {
		return twoDirectional;
	}
	
	public CharacterAnimation() {
		animations = new HashMap<String, Animation>();
	}
	
	public void setAnimation(String name, Animation animation) {
		if (name == CharacterAnimationType.WALKING_UP.toString()) {
			twoDirectional = true;
		}
		animations.put(name, animation);
	}
	
	/**
	 * Play a certain type of animation
	 * @param type
	 */
	public void play(String name) {
		play(animations.get(name));
	}
	
	public void play(CharacterAnimationType type) {
		play(animations.get(type.toString()));
	}
	
	/**
	 * Sets a new animation to current
	 * @param animation
	 */
	public void play(Animation animation) {
		if (animation == null) {
			return;
		}
		
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
			current = animations.get(CharacterAnimationType.DEFAULT.toString());
		}
		return current.getFrame(deltaTime);
	}
	
	@Override
	public void onAnimationFinished() {
		// Play the last looped animation (idle after attack and etc.)
		play(lastLooped);
	}
	
	public int getFrameWidth() {
		if (current == null) {
			return 0;
		}
		return current.getFrame().getRegionWidth();
	}
	
}
