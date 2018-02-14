package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BattleForTheGalaxy extends Game {
	SpriteBatch batch;
	SplashScreen splashscreen;
	GameScreen gamescreen;
	Reticle reticle;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		reticle = new Reticle();
		try {
			splashscreen = new SplashScreen(this);
		} catch (UnknownHostException e) {
			System.out.println("I DONT KNOW WHAT IM DOING");
			e.printStackTrace();
			System.exit(0);
		}
		gamescreen = new GameScreen(this);
		setScreen(splashscreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		splashscreen.dispose();
		gamescreen.dispose();
	}
}
