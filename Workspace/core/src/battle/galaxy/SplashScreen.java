package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The splash screen displayed when you first launch the game.
 */
public class SplashScreen implements Screen {
	
	BattleForTheGalaxy game;
	OrthographicCamera camera;
	Texture bg_texture;
	Sprite bg_sprite;
	Stage stage;
	
	Label title;
	
	/**
	 * Constructor that takes the incoming game and sets up UI elements
	 * @param incomingGame
	 * @throws UnknownHostException
	 */
	public SplashScreen(BattleForTheGalaxy incomingGame) throws UnknownHostException {
		this.game = incomingGame;
		stage = new Stage();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1600, 900);  // false => y-axis 0 is bottom-left
		
		bg_texture = new Texture(Gdx.files.internal("supernova-background.jpg"));
		bg_texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother textures
		bg_sprite = new Sprite(bg_texture);
		
		// Initialize Title text
		title = new Label("Battle for the Galaxy", game.skin);
		title.setFontScale(2f);
		title.setPosition(1600/2 - 2*title.getWidth()/2, 900 - 2*title.getHeight() - 200);
		
		
		stage.addActor(title);
		Gdx.input.setInputProcessor(stage);
	
	}
	
	/**
	 * Draw the background and the title. Listen for keyboard input to go to LoginScreen.
	 */
	@Override 
	public void render(float delta) {
		Gdx.gl.glClearColor(0.05F, 0.05F, 0.05F, 0.05F);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		game.batch.draw(bg_texture, 0, 0);
		game.batch.end();
		
		//Stage
		stage.draw();
		
		if(Gdx.input.isKeyJustPressed(Keys.ANY_KEY)) {
			try {
				game.setScreen(new LoginScreen());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dispose();
		}	
	}
	
	/**
	 * Unused part of screen interface.
	 */
	@Override
	public void show() {

	}
	/**
	 * Unused part of screen interface.
	 */
	@Override
	public void resize(int width, int height) {
		
	}
	/**
	 * Unused part of screen interface.
	 */
	@Override
	public void pause() {
		
	}
	/**
	 * Unused part of screen interface.
	 */
	@Override
	public void resume() {
		
	}
	/**
	 * Unused part of screen interface.
	 */
	@Override
	public void dispose() {
		
	}
	/**
	 * Unused part of screen interface.
	 */
	@Override
	public void hide() {
		
	}

}

