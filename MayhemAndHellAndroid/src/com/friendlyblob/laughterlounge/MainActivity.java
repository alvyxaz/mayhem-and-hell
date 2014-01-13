package com.friendlyblob.laughterlounge;

// tag:^(?!.*(Netlink|HWC|gralloc)).*$

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.friendlyblob.mayhemandhell.client.ActionResolverDesktop;
import com.friendlyblob.mayhemandhell.client.GoogleDesktop;
import com.friendlyblob.mayhemandhell.client.MyGame;

public class MainActivity extends AndroidApplication {

	public void onCreate (android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize(new MyGame(new GoogleDesktop(), new ActionResolverDesktop()), true);
	}

}
