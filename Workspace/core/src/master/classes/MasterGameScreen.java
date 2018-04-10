package master.classes;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.FitViewport;

import battle.galaxy.HUDElements;
import data.GameData;
import data.HitData;
import data.JsonHeader;
import data.PlayerData;
import data.ProjectileData;
import entities.EnemyPlayer;
import entities.Player;
import entities.Projectile;
import entities.Reticle;

public abstract class MasterGameScreen extends MasterScreen{
	
	public final static int SCREEN_WIDTH = 1600;
	public final static int SCREEN_HEIGHT = 900;
	public final static int BG_WIDTH = 2560;
	public final static int BG_HEIGHT = 1600;
	
	//Graphical
	private HUDElements hud;
	private Reticle reticle;
	private Vector3 mouse = new Vector3();
	//Map Stuff
	private int mapSize;
	private Vector2[][] backgroundTiles;
	private Vector2[] respawnPoints;
	//Entities
	private Player player;
	private HashMap<Integer, EnemyPlayer> otherPlayers = new HashMap<Integer, EnemyPlayer>();
	private HashMap<Integer, Projectile> projectiles = new HashMap<Integer, Projectile>();
	//Data
	private GameData gameData;
	private int gameType;
	
	/**
	 * Constructor for basic game
	 * @param gameType The type of game this will be
	 * @param mapSize The size of the map based on the game type
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
		backgroundTiles = new Vector2[mapSize][mapSize];
		for(int i = 0; i < mapSize; i++) {
			for(int j = 0; j < mapSize; j++) {
				backgroundTiles[i][j] = new Vector2(BG_WIDTH*i, BG_HEIGHT*j);
			}
		}
		//Setup stage with player and reticle
		gameData = new GameData(joinMatch(), new Vector2(0,0), 0);
		setPlayer(new Player(gameData.getPlayerData().getId()));
		stage.setViewport(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
		setReticle(new Reticle());
		stage.addActor(getPlayer());
		stage.addActor(getReticle());
		getPlayer().setPosition(mapSize*BG_WIDTH/2, mapSize*BG_HEIGHT/2);
		getReticle().setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		
		//Cursor/input
		Cursor customCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("transparent-1px.png")), 0, 0);
		Gdx.graphics.setCursor(customCursor);
		Gdx.input.setCursorCatched(false);
		Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		Gdx.input.setInputProcessor(stage);
		
		gameData = new GameData(getPlayer().getId(), getPlayer().getPosition(), getPlayer().getRotation());
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.05F, 0.05F, 0.05F, 0.05F);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);
		
		getMouse().set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(getMouse());

		game.getBatch().begin();
		for(int i = 0; i < mapSize; i++) {
			for(int j = 0; j < mapSize; j++) {
				game.getBatch().draw(background, backgroundTiles[i][j].x, backgroundTiles[i][j].y);
			}
		}
		game.getBatch().end();

		// Stage
		stage.act(delta);
		stage.draw();
		update(delta);
	}
	
	private int joinMatch() {
		String json =  game.getDataController().sendToServerWaitForResponse("{jsonOrigin:1,jsonType:" + gameType + "}");
		int id = game.getDataController().getJsonController().getJsonReader().parse(json).getInt("matchId");
		return id;
	}
	
	/**
	 * Update all entities and data in the game
	 */
	public abstract void update(float delta);
	
	/**
	 * Update all of gameData's projectiles. Step forward, remove dead, and add new.
	 * @param delta
	 */
	public void updateProjectiles(float delta) {
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
	public void updateEnemies(float delta) {
		for(Iterator<Entry<Integer, PlayerData>> iter = gameData.getEnemies().entrySet().iterator(); iter.hasNext();) {
			PlayerData ed = iter.next().getValue();
			if(!otherPlayers.containsKey(ed.getId())) {
				EnemyPlayer e = new EnemyPlayer(ed);
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
	public void checkCollision() {
		// NEW WAY CHECKS FOR ALL PROJECTILES MAKING CONTACT ONLY WITH PLAYER SHIP
		for(Iterator<Map.Entry<Integer, Projectile>> projIter = projectiles.entrySet().iterator(); projIter.hasNext();) {
			Projectile proj = projIter.next().getValue();
			if(proj.getSource() != getPlayer().getId()) {
				Vector2 dist = new Vector2();
				dist.x = (float) Math.pow(getPlayer().getX() - proj.getX(), 2);
				dist.y = (float) Math.pow(getPlayer().getY() - proj.getY(), 2);
				if(Math.sqrt(dist.x + dist.y) < 50) {
					// The player has been hit with an enemy projectile
					getPlayer().getShip().dealDamage(proj.getDamage());
					System.out.println("GameScreen.checkCollision: player was hit with " + proj.getDamage() + " damage and has " + getPlayer().getShip().getHealth() + " health.");
					
					if(getPlayer().getShip().getHealth() <= 0) {
						// The player has just been killed
						HitData hit = new HitData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_HIT, proj.getSource(), getPlayer().getId(), proj.getDamage(), true);
						game.getDataController().sendToServer(hit);
						getPlayer().getShip().calcStats();
						getPlayer().reset(pickRespawnPoint());
						gameData.getPlayerData().reset();
					}
					else {
						HitData hit = new HitData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_HIT, proj.getSource(), getPlayer().getId(), proj.getDamage(), false);
						game.getDataController().sendToServer(hit);
						System.out.println(getPlayer().getId());
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
	private Vector2 pickRespawnPoint() {
		Vector2 point = new Vector2();
		if(respawnPoints.length > 1) {
			point.set(respawnPoints[(int) Math.random() * (respawnPoints.length - 0)]);
		}else {
			point.set(respawnPoints[0]);
		}
		return point;
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

	/**
	 * @return the hud
	 */
	public HUDElements getHud() {
		return hud;
	}

	/**
	 * @param hud the hud to set
	 */
	public void setHud(HUDElements hud) {
		this.hud = hud;
	}

	/**
	 * @return the reticle
	 */
	public Reticle getReticle() {
		return reticle;
	}

	/**
	 * @param reticle the reticle to set
	 */
	public void setReticle(Reticle reticle) {
		this.reticle = reticle;
	}

	/**
	 * @return the mouse
	 */
	public Vector3 getMouse() {
		return mouse;
	}

	/**
	 * @param mouse the mouse to set
	 */
	public void setMouse(Vector3 mouse) {
		this.mouse = mouse;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

}
