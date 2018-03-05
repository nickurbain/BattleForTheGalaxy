package battle.galaxy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import data.GameData;
import data.PlayerData;
import entities.EnemyPlayer;
import entities.Player;
import entities.Projectile;

public class GameScreen implements Screen {
	
	public final static int SCREEN_WIDTH = 1600;
	public final static int SCREEN_HEIGHT = 900;
	
	public final int BG_WIDTH = 2560;
	public final int BG_HEIGHT = 1600;
	
	public final int MAP_WIDTH = 40960;
	public final int MAP_HEIGHT = 25600;
	
	BattleForTheGalaxy game;
	OrthographicCamera camera;
	OrthographicCamera hudCamera;
	Stage stage;
	Reticle reticle;
	Texture texture_bg;
	Vector2[][] background;
	Vector3 mouse;
	Cursor customCursor;
	private HUDElements hud;
	
	//Entities
	Player player;
	ArrayList<Projectile> projectiles = new ArrayList<Projectile>(); //ArrayList for all projectiles
	ArrayList<EnemyPlayer> enemies = new ArrayList<EnemyPlayer>();
	
	GameData gameData;
	EnemyPlayer enemy;
	
	public GameScreen(BattleForTheGalaxy game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1600, 900);  // false => y-axis 0 is bottom-left
		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(false, 1600,900);
		
		texture_bg = new Texture(Gdx.files.internal("space-tile.jpg"));
		texture_bg.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother rendering

		mouse = new Vector3();
		background = new Vector2[16][16];
		for(int i = 0; i < background.length; i++) {
			for(int j = 0; j < background[i].length; j++) {
				background[i][j] = new Vector2(BG_WIDTH*i, BG_HEIGHT*j);
			}
		}
		
		hud = new HUDElements(game);
	}

	@Override
	public void render(float delta) {
		//Setup
		Gdx.gl.glClearColor(0.05F, 0.05F, 0.05F, 0.05F);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		hudCamera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(mouse);
		
		//Drawing
		game.batch.begin();
			for(int i = 0; i < background.length; i++) {
				for(int j = 0; j < background[i].length; j++) {
					game.batch.draw(texture_bg, background[i][j].x, background[i][j].y);
				}
			}
		game.batch.end();
		//Updating
		reticle.update(mouse);
		stage.draw();
		player.updateRotation(delta, reticle);
		stage.act(Gdx.graphics.getDeltaTime());
		
		camera.position.set(player.getX(), player.getY(), 0);
		hudCamera.position.set(player.getX(), player.getY(),0);
		//Draw UI
		game.batch.setProjectionMatrix(hudCamera.combined);
		game.batch.begin();
			hud.drawHUD(gameData);
		//game.batch.end();
		/*
		 * Update entitites
		 */
		player.outOfBounds();
		
		if(player.getNewProjectile() != null) {
			projectiles.add(player.getNewProjectile());
			//gameData.newProjectile(player.getNewProjectile());
			stage.addActor(player.getNewProjectile());
			player.setNewProjectile();
		}
		
		updateProjectiles(delta);
		
		updateEnemies(delta);
		
		
		/*
		 * Keyboard and mouse input will go below
		 */
		
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			System.exit(0);
		}
		
		
		// Update JSON with new Player location
		gameData.updatePlayer(player.getPosition(), player.getDirection(), player.getRotation(), player.getHealth(), player.getShield(), player.getHull());
		//Check for updates from server
		game.dataController.parseRawData();
		gameData.getUpdateFromController(game.dataController);
		if(enemy != null) {
			for(PlayerData p: gameData.getEnemies()) {
				if(enemy.getId() == p.getId()) {
					enemy.updateEnemy(p.getPosition(), p.getDirection(), p.getRotation());
					enemy.setPosition(enemy.getX(), enemy.getY() + 150);
				}
			}
		}else {
			for(PlayerData p: gameData.getEnemies()) {
				enemy = new EnemyPlayer(p.getId(), p.getPosition(), p.getDirection(), p.getRotation());
				stage.addActor(enemy);
			}
		}
		gameData.updateGameTime();
		//Last thing todo
		gameData.sendDataToController(game.dataController);
		
	} // End render function
	
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
		
		gameData = new GameData(player.getId(), player.getPosition(), player.getRotation());
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
	
	private void updateProjectiles(float delta) {
		for(Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext();) {
			Projectile p = iter.next();
			if(p.remove()) {
				iter.remove();
			}else {
				p.act(delta);
			}
		}
	}
	
	private void updateEnemies(float delta) {
		
		for(Iterator<EnemyPlayer> iter = enemies.iterator(); iter.hasNext();) {
			EnemyPlayer p = iter.next();
			p.act(delta);
		}
	}
	
}
