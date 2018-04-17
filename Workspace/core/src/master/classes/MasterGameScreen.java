package master.classes;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

import battle.galaxy.HUDElements;
import battle.galaxy.MainMenu;
import battle.galaxy.MatchStatsScreen;
import data.GameData;
import data.HitData;
import data.JsonHeader;
import data.NewMatchData;
import data.PlayerData;
import data.ProjectileData;
import entities.EnemyPlayer;
import entities.Player;
import entities.Projectile;
import entities.Reticle;

/**
 * An abstract class that contains the basic component for a game match.
 */
public abstract class MasterGameScreen extends MasterScreen{
	
	public final static int SCREEN_WIDTH = 1600;
	public final static int SCREEN_HEIGHT = 900;
	public final static int BG_WIDTH = 2560;
	public final static int BG_HEIGHT = 1600;
	
	//Graphical
	protected HUDElements hud;
	protected Reticle reticle;
	protected Vector3 mouse = new Vector3();
	//Map Stuff
	private int mapSize;
	private Vector2[][] backgroundTiles;
	private Vector2[] respawnPoints;
	//Entities
	protected Player player;
	protected HashMap<Integer, EnemyPlayer> otherPlayers = new HashMap<Integer, EnemyPlayer>();
	protected HashMap<Integer, Projectile> projectiles = new HashMap<Integer, Projectile>();
	//Data
	protected GameData gameData;
	private int gameType;
	
	/**
	 * Constructor for basic game
	 * @param gameType The type of game this will be
	 * @param mapSize The size of the map based on the game type
	 * @param respawnPoints The points where a player can respawn at
	 * @throws UnknownHostException
	 */
	public MasterGameScreen(int gameType, int mapSize, Vector2[] respawnPoints) throws UnknownHostException {
		super("space-tile.jpg", "clean-crispy-ui.json");
		this.setGameType(gameType);
		this.setMapSize(mapSize);
		this.respawnPoints = new Vector2[respawnPoints.length];
		for(int i = 0; i < respawnPoints.length; i++) {
			this.respawnPoints[i] = respawnPoints[i];
		}
		//Setup the background
		backgroundTiles = new Vector2[mapSize/BG_WIDTH][mapSize/BG_HEIGHT];
		for(int i = 0; i < mapSize/BG_WIDTH; i++) {
			for(int j = 0; j < mapSize/BG_HEIGHT; j++) {
				backgroundTiles[i][j] = new Vector2(BG_WIDTH*i, BG_HEIGHT*j);
			}
		}
		//Setup stage with player and reticle
		gameData = new GameData(joinMatch());
		player = new Player(gameData.getPlayerData().getId(), gameData.getTeamNum(), pickRespawnPoint());
		stage.setViewport(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
		reticle = new Reticle();
		stage.addActor(player);
		stage.addActor(reticle);
		player.setPosition(mapSize/2, mapSize/2);
		reticle.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		hud = new HUDElements(game.getBatch(), game.getSkin());
		//Cursor/input
		Cursor customCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("transparent-1px.png")), 0, 0);
		Gdx.graphics.setCursor(customCursor);
		Gdx.input.setCursorCatched(false);
		Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		Gdx.input.setInputProcessor(stage);
	}
	
	/**
	 * Draw the game and update entities/server.
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.05F, 0.05F, 0.05F, 0.05F);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);
		
		mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(mouse);

		game.getBatch().begin();
		for(int i = 0; i < mapSize/BG_WIDTH; i++) {
			for(int j = 0; j < mapSize/BG_HEIGHT; j++) {
				game.getBatch().draw(background, backgroundTiles[i][j].x, backgroundTiles[i][j].y);
			}
		}
		game.getBatch().end();
		
		camera.position.set(player.getX(), player.getY(), 0);
		//hudCamera.position.set(player.getX(), player.getY(),0);
		
		// Stage
		stage.act(delta);
		stage.draw();
		update(delta);
		hud.drawHUD(gameData, player);
		
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			try {
				game.setScreen(new MainMenu());
				Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				super.dispose();
			}
		}
	}
	
	/**
	 * Send a message to the server to connect to a match and get a matchId.
	 * @return the matchId for this client
	 */
	protected NewMatchData joinMatch() {
		NewMatchData matchData = (NewMatchData) game.getDataController().sendToServerWaitForResponse("{jsonOrigin:1,jsonType:12,matchType:" + gameType + "}", false);
		if(gameType == 0) {
			matchData.setTeamNum(-1);
		}
		return matchData;
	}
	
	/**
	 * Update all entities and data in the game
	 */
	public abstract void update(float delta);
	
	/**
	 * Send updates to the server
	 */
	protected void updateServer() {
		sendProjectile();
		game.getDataController().sendToServer(gameData.getPlayerData());
	}
	
	/**
	 * Update from the server
	 */
	protected void updateFromServer() {
		game.getDataController().parseRawData();
		gameData.getUpdateFromController(game.getDataController());
		checkIfOver();
	}
	
	protected void updatePlayerData(float delta) {
		gameData.updatePlayer(player.getPosition(), player.getDirection(), player.getRotation(), player.getShip().getHealth(), player.getShip().getShield());
		player.updateRotation(delta, reticle);
	}
	
	/**
	 * Sends a projectile fired by the player to the server
	 */
	protected void sendProjectile() {
		if(player.getNewProjectile() != null) {
			addProjectile(player.getNewProjectile());
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
	 * Add a projectile from the player to the list of projectiles
	 * @param projectile the projectile to be added.
	 */
	protected void addProjectile(Projectile projectile) {
		if(projectile != null) {
			projectiles.put(projectile.getId(), projectile);
			stage.addActor(projectile);
		}
	}
	
	/**
	 * Update all of gameData's projectiles. Step forward, remove dead, and add new.
	 * @param delta
	 */
	protected void updateProjectiles(float delta) {
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
					//System.out.println("Adding projectile: " + p.getId()); //adding projectile
					stage.addActor(p);
				}
				
			}
		}
		//Update the projectiles
		for(Iterator<Map.Entry<Integer, Projectile>> iter = projectiles.entrySet().iterator(); iter.hasNext();) {
			Projectile p = iter.next().getValue();
			if(p.isDead()) { //Projectile is dead
				//System.out.println("Removing projectile ID: " + p.getId());
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
	protected void updateEnemies(float delta) {
		for(Iterator<Entry<Integer, PlayerData>> iter = gameData.getEnemies().entrySet().iterator(); iter.hasNext();) {
			PlayerData ed = iter.next().getValue();
			if(!otherPlayers.containsKey(ed.getId())) {
				EnemyPlayer e = new EnemyPlayer(ed, player.getTeam());
				System.out.println("Enemy: " + e.getTeamNum() + " Player: " + player.getTeam());
				//e.setPosition(e.getX(), e.getY() + 150);	//ECHO SERVER TESTING
				otherPlayers.put(e.getId(), e);	
				stage.addActor(e);
			}else{
				otherPlayers.get(ed.getId()).updateEnemy(ed);
			}
		}
	}
	
	/**
	 * Check if a enemy projectile collides with the player
	 */
	protected void checkCollision() {
		// NEW WAY CHECKS FOR ALL PROJECTILES MAKING CONTACT ONLY WITH PLAYER SHIP
		for(Iterator<Map.Entry<Integer, Projectile>> projIter = projectiles.entrySet().iterator(); projIter.hasNext();) {
			Projectile proj = projIter.next().getValue();
			if(proj.getSource() != player.getId() && proj.getSourceTeam() != player.getTeam()) {
				Vector2 dist = new Vector2();
				dist.x = (float) Math.pow(player.getX() - proj.getX(), 2);
				dist.y = (float) Math.pow(player.getY() - proj.getY(), 2);
				if(Math.sqrt(dist.x + dist.y) < 50) {
					// The player has been hit with an enemy projectile
					player.getShip().dealDamage(proj.getDamage());
					System.out.println("GameScreen.checkCollision: player was hit with " + proj.getDamage() + " damage and has " + player.getShip().getHealth() + " health.");
					if(player.getShip().getHealth() <= 0) {
						// The player has just been killed
						HitData hit = new HitData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_HIT, proj.getSource(), player.getId(), proj.getDamage(), true);
						game.getDataController().sendToServer(hit);
						player.getShip().calcStats();
						player.reset(pickRespawnPoint());
						gameData.getPlayerData().reset();
					} else {
						//Player was not killed
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
	 * Pick a random point in respawn points to spawn at
	 * @return point The coordinates of the respawn point
	 */
	protected Vector2 pickRespawnPoint() {
		Vector2 point = new Vector2();
		if(respawnPoints.length > 1) {
			point.set(respawnPoints[(int) Math.random() * respawnPoints.length]);
		}else {
			point.set(respawnPoints[0]);
		}
		return point;
	}
	
	/**
	 * Check if the game is over and end it if it is
	 */
	protected void checkIfOver() {
		if(gameData.isOver()) {
			try {
				game.setScreen(new MatchStatsScreen());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} finally {
				dispose();
			}
		}
	}

	/**
	 * @return the mapSize
	 */
	public int getMapSize() {
		return mapSize;
	}

	/**
	 * @param mapSize the mapSize to set
	 */
	public void setMapSize(int mapSize) {
		this.mapSize = mapSize;
	}

	/**
	 * @return the gameType
	 */
	public int getGameType() {
		return gameType;
	}

	/**
	 * @param gameType the gameType to set
	 */
	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

	/**
	 * @return the respawnPoints
	 */
	public Vector2[] getRespawnPoints() {
		return respawnPoints;
	}

	/**
	 * @param respawnPoints the respawnPoints to set
	 */
	public void setRespawnPoints(Vector2[] respawnPoints) {
		this.respawnPoints = respawnPoints;
	}

}
