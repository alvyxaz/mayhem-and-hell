package com.friendlyblob.mayhemandhell.client;

public interface ActionResolver {
    public void showToast(final CharSequence toastMessage, int toastDuration);
    public void openUri(String uri);
}