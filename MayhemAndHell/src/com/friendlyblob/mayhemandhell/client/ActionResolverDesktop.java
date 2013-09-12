package com.friendlyblob.mayhemandhell.client;

public class ActionResolverDesktop implements ActionResolver {

	@Override
	public void showToast(CharSequence toastMessage, int toastDuration) {
		System.out.println("Toast: " + toastMessage);
		
	}

	@Override
	public void openUri(String uri) {
		// TODO Auto-generated method stub
		
	}

}
