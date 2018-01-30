package battle.galaxy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BattleForTheGalaxy extends Game {
	SpriteBatch batch;
	SplashScreen splashscreen;
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		splashscreen = new SplashScreen(this);
		setScreen(splashscreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
