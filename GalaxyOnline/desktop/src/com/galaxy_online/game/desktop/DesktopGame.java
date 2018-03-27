package com.galaxy_online.game.desktop;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopGame {
	public static void main (String[] args) {
		new LwjglApplication(new Game(), "Game Menu", 1500, 1500);
	}
}
