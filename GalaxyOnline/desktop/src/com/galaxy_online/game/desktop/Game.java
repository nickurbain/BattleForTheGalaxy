package com.galaxy_online.game.desktop;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Screen;

public class Game implements ApplicationListener {
	public void create () {
		this.setScreen(new Splash(this));
	}

	public void setScreen(Screen screenType) {
		// TODO Auto-generated method stub
		
	}

	public void render () {
	}

	public void resize (int width, int height) {
	}

	public void pause () {
	}

	public void resume () {
	}

	public void dispose () {
	}
}