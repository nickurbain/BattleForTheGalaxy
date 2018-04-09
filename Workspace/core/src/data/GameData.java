package data;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;

import entities.Projectile;

/*
 * GameData is the class that stores the basic data for the game
 * that is sent/received though the server. 
 */
public class GameData{
	//Data for the client player
	private PlayerData playerData;
	//Data for enemy player
	private HashMap<Integer, PlayerData> enemies;
	//Data for a new projectile to be sent to server
	//private ProjectileData newProjectile;
	private HashMap<Integer, ProjectileData> projectilesData;
	//Time remaining in the game
	private long startTime = System.currentTimeMillis();
	private long gameTime = 0;
	
	private String recentKill = "";
	
	/**
	 * Constructor w/ params based off of players initial construction
	 * @param id the player id
	 * @param position the position of the player
	 * @param rotation the rotation of the player
	 */
	public GameData(int id, Vector2 position, float rotation) {
		playerData = new PlayerData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_PLAYER, id, position, new Vector2(0,0), rotation);
		enemies = new HashMap<Integer, PlayerData>();
		projectilesData = new HashMap<Integer, ProjectileData>(); 
	}
	
	/**
	 * Sends data to the DataController if the data has been updated
	 * 
	 * @param dc the Game's DataControllerss
	 */
	public void sendDataToController(DataController dc) {
		dc.updateServerPlayerData(playerData);
	}
	
	/**
	 * Send the new Projectile to the DataController on demand (when a new Projectile has been created)
	 * @param dc The game's DataController object
	 * @param id The ID of the newly created Projectile
	 */
	public void sendNewProjectileToController(DataController dc, int id) {
		// update the server with the last (newest) projectile
		dc.updateServerProjectileData(projectilesData.get(id));
	}
	
	/**
	 * Updates PlayerData of enemies if a PlayerData with the given id
	 * already exist, if not, creates new enemy.
	 * 
	 * @param enemyData data for an enemy from the server
	 */
	public void updateEnemy(PlayerData enemyData) {
		if(!enemies.containsKey(enemyData.getId())) {
			enemies.put(enemyData.getId(), enemyData);
		}else {
			enemies.get(enemyData.getId()).updateData(enemyData);
		}
	}
	
	public void updateEnemy(HitData e) {
		if(enemies.containsKey(e.getPlayerId())) {
			PlayerData pd = enemies.get(e.getPlayerId());
			enemies.get(e.getPlayerId()).hit(e);
			if(e.getCausedDeath()) {
				pd.reset();
			}
		}
		
		if(e.getCausedDeath()) {
			recentKill = "Player " + Integer.toString(e.getSourceId()) + " has killed Player " + Integer.toString(e.getPlayerId());
			System.out.println(recentKill);
		}
		
	}
	
	/**
	 * Used to remove EnemyData from enemies. Called from DataController.
	 * 
	 * @param id the id of the PlayerData to be removed
	 */
	public void removeEnemy(int id) {
		if(enemies.containsKey(id)) {
			enemies.remove(id);
		}
	}
	
	/**
	 * Returns a list of enemy data
	 * @return list of enemy data
	 */
	public HashMap<Integer, PlayerData> getEnemies(){
		return enemies;
	}
	
	/**
	 * Adds a new Projectile to the GameData ArrayList of Projectiles.
	 * 
	 * @param projectile the Projectile to be added.
	 */
	public void addProjectileFromClient(Projectile projectile) {
		ProjectileData projectileData = new ProjectileData(projectile);
		projectilesData.put(projectileData.getId(), projectileData);
	}
	
	public void removeProjectile(int id) {
		projectilesData.remove(id);
	}
	
	/**
	 * Adds a new projectile from the server
	 * 
	 * @param pd ProjectileData provided from JSON file received from the server
	 */
	public void addProjectileFromServer(ProjectileData pd) {
		projectilesData.put(pd.getId(), pd);
	}
	
	/**
	 * Updates the player's PlayerData iff some of the data has been changed
	 * 
	 * @param position the position of the player
	 * @param direction the direction of the player
	 * @param rotation the rotation of the player
	 */
	public void updatePlayer(Vector2 position, Vector2 direction, float rotation, int health, int shield) {
		if(direction.x != playerData.getDirection().x || direction.y != playerData.getDirection().y || playerData.getRotation() != rotation) {
			playerData.updateData(position, direction, rotation, health, shield);
		}
	}
	
	public PlayerData getPlayerData() {
		return playerData;
	}
	
	/**
	 * Pull parsed data from the DataController and update in-game entities with it 
	 * @param dataController
	 */
	public void getUpdateFromController(DataController dataController) {
		for(Iterator<Object> iter = dataController.getRxFromServer().iterator(); iter.hasNext();) {
			JsonHeader e =  (JsonHeader) iter.next();
			switch(e.getJsonType()) {
				case JsonHeader.TYPE_PLAYER:
					updateEnemy((PlayerData) e);
					iter.remove();
					break;
				case JsonHeader.TYPE_PROJECTILE:
					addProjectileFromServer((ProjectileData) e);
					iter.remove();
					break;
				case JsonHeader.TYPE_HIT:
					updateEnemy((HitData) e);
					iter.remove();
					break;
			}
		}
	}

	public HashMap<Integer, ProjectileData> getProjectileData(){
		return projectilesData;
	}
	
	public long getGameTime() {
		return gameTime;
	}

	public void setGameTime(long gameTime) {
		this.gameTime = gameTime;
	}
	
	public void updateGameTime() {
		gameTime = System.currentTimeMillis() - startTime;
	}
	
	public String getRecentKill() {
		return recentKill;
	}

}
