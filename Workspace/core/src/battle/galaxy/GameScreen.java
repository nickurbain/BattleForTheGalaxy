package battle.galaxy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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

import data.GameData;
import data.PlayerData;
import data.ProjectileData;
import entities.EnemyPlayer;
import entities.Player;
import entities.Projectile;
import entities.Reticle;

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
	private Player player;
	private HashMap<Integer, Projectile> projectiles = new HashMap<Integer, Projectile>(); //ArrayList for all projectiles
	private HashMap<Integer, EnemyPlayer> enemies = new HashMap<Integer, EnemyPlayer>();
	
	private GameData gameData;

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
		updateProjectiles(delta);	
		updateEnemies(delta);
		checkCollision();
		stage.act(Gdx.graphics.getDeltaTime());
		
		camera.position.set(player.getX(), player.getY(), 0);
		hudCamera.position.set(player.getX(), player.getY(),0);
		
		//Draw UI
		game.batch.setProjectionMatrix(hudCamera.combined);
		game.batch.begin();
			hud.drawHUD(gameData);
		//game.batch.end();
		player.outOfBounds();
		
		//Update gameData from client
		sendProjectile();
		gameData.updatePlayer(player.getPosition(), player.getDirection(), player.getRotation(), player.getShip().getHealth(), player.getShip().getShield(), player.getShip().getHull());
		//Check for updates from server
		game.dataController.parseRawData();
		gameData.getUpdateFromController(game.dataController);
		
		gameData.updateGameTime();
		//Send updates to server
		gameData.sendDataToController(game.dataController);
		
		/*
		 * Keyboard and mouse input will go below
		 */
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			System.exit(0);
		}
		
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
		game.dataController.setId(player.getId()); 
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
	
	private void sendProjectile() {
		if(player.getNewProjectile() != null) {
			projectiles.put(player.getNewProjectile().getId(), player.getNewProjectile());
			stage.addActor(player.getNewProjectile());
			
			// Add the new Projectile to the gameData list of Projectiles
			gameData.addProjectileFromClient(player.getNewProjectile());
			
			// Send a JSON to the server with the new Projectile data
			gameData.sendNewProjectileToController(game.dataController, player.getNewProjectile().getId());
			
			// Set the player Projectile to NULL
			player.setNewProjectile();
		}
	}
	
	private void updateProjectiles(float delta) {
		//Check gameData for updated projectile information
		if(!gameData.getProjectileData().isEmpty()) {
			for(Iterator<Map.Entry<Integer, ProjectileData>> dataIter = gameData.getProjectileData().entrySet().iterator(); dataIter.hasNext();) {
				ProjectileData pd = dataIter.next().getValue();
				pd.update(delta);
				if(!projectiles.containsKey(pd.getId()) && !pd.isDead()) { //Projectile does not exist and isn't dead: create it
					Projectile p = new Projectile(pd);
					projectiles.put(p.getId(), p);
					System.out.println("Adding projectile: " + p.getId()); //adding projectile
					stage.addActor(p);
				}else if (pd.isDead()) {
					dataIter.remove(); 
				}
			}
		}
		//Update the projectiles
		for(Iterator<Map.Entry<Integer, Projectile>> iter = projectiles.entrySet().iterator(); iter.hasNext();) {
			Projectile p = iter.next().getValue();
			if(p.isDead()) { //Projectile is dead
				System.out.println("Removing projectile");
				p.remove();
				iter.remove();
				gameData.getProjectileData().remove(p.getId());
			}
		}
		
	}
	
	/**
	 * Scan through gameData and check if there are new enemies and add them as an EnemyPlayer.
	 * Also check for updated enemy data in gameData and update the enemies.
	 * @param delta
	 */
	private void updateEnemies(float delta) {
		for(Iterator<Entry<Integer, PlayerData>> iter = gameData.getEnemies().entrySet().iterator(); iter.hasNext();) {
			PlayerData ed = iter.next().getValue();
			if(!enemies.containsKey(ed.getId())) {
				EnemyPlayer e = new EnemyPlayer(ed);
				//e.setPosition(e.getX(), e.getY() + 150);	//ECHO SERVER TESTING
				enemies.put(e.getId(), e);	
				stage.addActor(e);
			}else{
				enemies.get(ed.getId()).updateEnemy(ed);
			}
		}
		
		for(Iterator<Entry<Integer, EnemyPlayer>> iter = enemies.entrySet().iterator(); iter.hasNext();) {
			EnemyPlayer e = iter.next().getValue();
			if(!e.isConnected()) {
				gameData.removeEnemy(e.getId());
				iter.remove();
			}
		}
		
	}
	
	private void checkCollision() {
		for(Iterator<Map.Entry<Integer, Projectile>> projIter = projectiles.entrySet().iterator(); projIter.hasNext();) {
			Projectile p = projIter.next().getValue();
			for(Iterator<Map.Entry<Integer, EnemyPlayer>> playerIter = enemies.entrySet().iterator(); playerIter.hasNext();){
				EnemyPlayer pl = playerIter.next().getValue();
				if(p.getFriendly() != pl.getId()) {
					Vector2 dist = new Vector2();
					dist.x = (float) Math.pow(pl.getX() - p.getX(), 2);
					dist.y = (float) Math.pow(pl.getY() - p.getY(), 2);
					if(Math.sqrt(dist.x + dist.y) < 50) {
						System.out.println("HIT");
						p.kill();
						pl.kill();
					}
				}
			}	
		}
	}
	
}
