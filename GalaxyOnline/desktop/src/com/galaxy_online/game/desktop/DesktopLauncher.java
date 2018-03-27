package com.galaxy_online.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.galaxy_online.game.GalaxyOnline;
//import com.galaxy_online.game.GalaxyOnline2;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GalaxyOnline(), config);
	}
}
