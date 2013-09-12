package com.friendlyblob.mayhemandhell.client;

public class AdsDesktop implements AdsInterface {

	@Override
	public boolean adsVisible() {
		return false;
	}

	@Override
	public void showAds() {
	}

	@Override
	public void hideAds() {
	}

	@Override
	public void showInterstitial() {
		System.out.println("SHOW INTER");
	}

}
