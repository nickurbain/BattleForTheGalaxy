package battle.galaxy;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {
	
	public final int SCREEN_WIDTH = 1600;
	public final int SCREEN_HEIGHT = 900;
	
	public final int BG_WIDTH = 2560;
	public final int BG_HEIGHT = 1600;
	
	public final int MAP_WIDTH = 40960;
	public final int MAP_HEIGHT = 25600;
	
	BattleForTheGalaxy game;
	OrthographicCamera camera;
	Stage stage;
	Player player;
	ArrayList<EnemyPlayer> enemies;
	Reticle reticle;
	Texture texture_bg;
	Vector2[][] background;
	Vector3 mouse;
	Cursor customCursor;
	
	public GameScreen(BattleForTheGalaxy game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1600, 900);  // false => y-axis 0 is bottom-left
		
		texture_bg = new Texture(Gdx.files.internal("space-tile.jpg"));
		texture_bg.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother rendering

		mouse = new Vector3();
		background = new Vector2[16][16];
		for(int i = 0; i < background.length; i++) {
			for(int j = 0; j < background[i].length; j++) {
				background[i][j] = new Vector2(BG_WIDTH*i, BG_HEIGHT*j);
			}
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.05F, 0.05F, 0.05F, 0.05F);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(mouse);
		
		game.batch.begin();
			for(int i = 0; i < background.length; i++) {
				for(int j = 0; j < background[i].length; j++) {
					game.batch.draw(texture_bg, background[i][j].x, background[i][j].y);
				}
			}
		game.batch.end();
		
		reticle.update(mouse);
		stage.draw();
		player.updateRotation(delta, reticle);
		stage.act(Gdx.graphics.getDeltaTime());
		
		camera.position.set(player.getX(), player.getY(), 0);
		
		/*
		 * Update entitites
		 */
		if(player.getX() > 40960 || player.getY() > 25600 || player.getX() < 0 || player.getY() < 0) {
			player.health -= 10;
			if(player.health <= 0) {
				System.exit(0);
			}
		}
		
		/*
		 * Keyboard and mouse input will go below
		 */
		
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			System.exit(0);
		}
		
	}
	
	@Override
	public void show() {
		stage = new Stage();
		// Align the screen area with the stage
		stage.setViewport(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
		player = new Player();
		reticle = new Reticle();
		stage.addActor(player);
		stage.addActor(reticle);
		player.setPosition(MAP_WIDTH/2, MAP_HEIGHT/2);
		reticle.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		//First background position
		
		Cursor customCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("transparent-1px.png")), 0, 0);
		Gdx.graphics.setCursor(customCursor);
		Gdx.input.setCursorCatched(false);
		Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
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
		texture_bg.dispose();
		customCursor.dispose();
	}

	@Override
	public void hide() {
		
	}
	
}