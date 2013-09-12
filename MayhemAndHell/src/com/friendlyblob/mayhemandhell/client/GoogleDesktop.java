package com.friendlyblob.mayhemandhell.client;

public class GoogleDesktop implements GoogleInterface{

	public MyGame game;
	
	@Override
	public void Login() {
	}

	@Override
	public void LogOut() {
	}

	@Override
	public boolean isSignedIn() {
		return true;
	}

	public void submitScore(int score){
		System.out.println("Desktop: submitScore: " +score);
	}

	@Override
	public void getScores() {
		System.out.println("Desktop: getScores()");
	}

	@Override
	public void getScoresData() {
		System.out.println("Desktop: getScoresData()");
	}

	@Override
	public void unlockAchievement(String id, String friendly) {
		System.out.println("Unlocked ---- " + friendly + " ----");
	}

	@Override
	public void incrementAchievements(int steps, String id, String friendly) {
		System.out.println("Incremented -" + friendly + " - by " + steps);
	}
	
	@Override
	public void showAchievements() {
		System.out.println("Requesting achievements");
	}

}
