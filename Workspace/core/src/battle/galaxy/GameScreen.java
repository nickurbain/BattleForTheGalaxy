package battle.galaxy;

import java.net.UnknownHostException;
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
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import data.GameData;
import data.HitData;
import data.JsonHeader;
import data.PlayerData;
import data.ProjectileData;
import entities.EnemyPlayer;
import entities.Player;
import entities.Projectile;
import entities.Reticle;

/**
 * First attempt at a basic DeathMatch screen. 
 */
public class GameScreen implements Screen {
	
	public final static int SCREEN_WIDTH = 1600;
	public final static int SCREEN_HEIGHT = 900;
	public final static int MAP_WIDTH = 40960;
	public final static int MAP_HEIGHT = 25600;
	public final static int RESPAWN_X = MAP_WIDTH/2;
	public final static int RESPAWN_Y = MAP_HEIGHT/2;
	
	public final int BG_WIDTH = 2560;
	public final int BG_HEIGHT = 1600;
	
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

	/**
	 * Constructor which takes the game container as an argument
	 * @param game
	 */
	public GameScreen(BattleForTheGalaxy game) {
		this.game = game;
		game.getDataController().sendToServer("{jsonOrigin:1,jsonType:12}");
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
		
		game.getDataController().parseRawData();
		
		/**** START: came from show() ****/
		stage = new Stage();
		// Align the screen area with the stage
		stage.setViewport(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
		player = new Player(game.getDataController().getMatchId());
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
		
		Gdx.input.setInputProcessor(stage);
		
		gameData = new GameData(player.getId(), player.getPosition(), player.getRotation());
		/**** END: came from show() ****/
		
		System.out.println("PLAYER CREATED! ID: " + player.getId());
	}
	
	/**
	 * Render and update/act on all elements of the game.
	 */
	@Override
	public void render(float delta) {
		checkIfGameOver();
		
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
		stage.draw();
		
		//Updating
		reticle.update(mouse);
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
			hud.drawHUD(gameData, player);
		//game.batch.end();
		player.outOfBounds();
		
		//Update gameData from client
		sendProjectile();
		gameData.updatePlayer(player.getPosition(), player.getDirection(), player.getRotation(), player.getShip().getHealth(), player.getShip().getShield());
		//Check for updates from server
		game.getDataController().parseRawData();
		gameData.getUpdateFromController(game.getDataController());
		
		gameData.updateGameTime();
		//Send updates to server
		game.getDataController().sendToServer(gameData.getPlayerData());
		
		/*
		 * Keyboard and mouse input will go below
		 */
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			try {
				game.setScreen(new MainMenu());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				dispose();
			}
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.M)) {
			game.getDataController().sendToServer("{jsonOrigin:0,jsonType:4}");
		}
		
	} // End render function

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
	 * Dispose of the background texture and reset the cursor to default.
	 */
	@Override
	public void dispose() {
		texture_bg.dispose();
		//if(customCursor != null) {
			//customCursor.dispose();
		//}
		Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
	}

	/**
	 * Unused part of screen interface.
	 */
	@Override
	public void hide() {
		
	}
	
	/**
	 * Sends a projectile fired by the player to the server
	 */
	private void sendProjectile() {
		if(player.getNewProjectile() != null) {
			projectiles.put(player.getNewProjectile().getId(), player.getNewProjectile());
			stage.addActor(player.getNewProjectile());
			
			// Add the new Projectile to the gameData list of Projectiles
			gameData.addProjectileFromClient(player.getNewProjectile());
			
			// Send a JSON to the server with the new Projectile data
			game.getDataController().sendToServer(gameData.getProjectileData().get(player.getNewProjectile().getId()));
			
			// Set the player Projectile to NULL
			player.resetNewProjectile();
		}
	}
	
	/**
	 * Update all of gameData's projectiles. Step forward, remove dead, and add new.
	 * @param delta
	 */
	private void updateProjectiles(float delta) {
		//Check gameData for updated projectile information
		if(!gameData.getProjectileData().isEmpty()) {
			for(Iterator<Map.Entry<Integer, ProjectileData>> dataIter = gameData.getProjectileData().entrySet().iterator(); dataIter.hasNext();) {
				ProjectileData pd = dataIter.next().getValue();
				pd.update(delta);
				
				if(pd.isDead()) {
					dataIter.remove();
					gameData.removeProjectile(pd.getId());
					projectiles.remove(pd.getId());
				}
				else if(!projectiles.containsKey(pd.getId())) {
					Projectile p = new Projectile(pd);
					projectiles.put(p.getId(), p);
					System.out.println("Adding projectile: " + p.getId()); //adding projectile
					stage.addActor(p);
				}
				
			}
		}
		//Update the projectiles
		for(Iterator<Map.Entry<Integer, Projectile>> iter = projectiles.entrySet().iterator(); iter.hasNext();) {
			Projectile p = iter.next().getValue();
			if(p.isDead()) { //Projectile is dead
				System.out.println("Removing projectile ID: " + p.getId());
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
		
	}
	
	/**
	 * Check for collisions between enemy projectiles and the player
	 */
	private void checkCollision() {
		// NEW WAY CHECKS FOR ALL PROJECTILES MAKING CONTACT ONLY WITH PLAYER SHIP
		for(Iterator<Map.Entry<Integer, Projectile>> projIter = projectiles.entrySet().iterator(); projIter.hasNext();) {
			Projectile proj = projIter.next().getValue();
			if(proj.getSource() != player.getId()) {
				Vector2 dist = new Vector2();
				dist.x = (float) Math.pow(player.getX() - proj.getX(), 2);
				dist.y = (float) Math.pow(player.getY() - proj.getY(), 2);
				if(Math.sqrt(dist.x + dist.y) < 50) {
					// The player has been hit with an enemy projectile
//					game.dataController.updateServerHit(proj.getSource(), player.getId(), proj.getDamage());
					player.getShip().dealDamage(proj.getDamage());
					System.out.println("GameScreen.checkCollision: player was hit with " + proj.getDamage() + " damage and has " + player.getShip().getHealth() + " health.");
					
					if(player.getShip().getHealth() <= 0) {
						// The player has just been killed
						HitData hit = new HitData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_HIT, proj.getSource(), player.getId(), proj.getDamage(), true);
						game.getDataController().sendToServer(hit);
						player.getShip().calcStats();
						//player.remove();
						//stage.addActor(player);
						player.reset(new Vector2(MAP_WIDTH/2, MAP_HEIGHT/2));
						gameData.getPlayerData().reset();
					}
					else {
						HitData hit = new HitData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_HIT, proj.getSource(), player.getId(), proj.getDamage(), false);
						game.getDataController().sendToServer(hit);
						System.out.println(player.getId());
					}
					gameData.getProjectileData().remove(proj.getId());
					proj.kill();
				}
			}
			
		}
		
	}
	
	/**
	 * Check if the game is over. If it is, go to the match screen.
	 */
	private void checkIfGameOver() {
		if(gameData.isOver()) {
			try {
				game.setScreen(new MatchStatsScreen());
				dispose();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}	
	}
	
}
