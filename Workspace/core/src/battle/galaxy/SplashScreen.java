package battle.galaxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SplashScreen implements Screen {
	
	BattleForTheGalaxy game;
	OrthographicCamera camera;
	Texture bg_texture;
	Sprite bg_sprite;
	
	String playerID = "";
	
	public SplashScreen(BattleForTheGalaxy game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1600, 900);  // false => y-axis 0 is bottom-left
		
		bg_texture = new Texture(Gdx.files.internal("supernova-background.jpg"));
		bg_texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother textures
		bg_sprite = new Sprite(bg_texture);
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.05F, 0.05F, 0.05F, 0.05F);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
			game.batch.draw(bg_texture, 0, 0);
		game.batch.end();
		
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.setScreen(game.gamescreen);
			dispose();
		}		
		
		if(playerID.isEmpty()) {
			
		}
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void hide() {
		
	}

}
