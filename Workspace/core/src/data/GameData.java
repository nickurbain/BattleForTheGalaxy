package data;

import java.util.ArrayList;
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
	private ArrayList<PlayerData> enemies;
	//Data for a new projectile to be sent to server
//	private ProjectileData newProjectile;
	private HashMap<Integer, ProjectileData> projectilesData;
	//Time remaining in the game
	private long startTime = System.currentTimeMillis();
	private long gameTime = 0;
	
	/**
	 * Constructor w/ params based off of players initial construction
	 * @param id the player id
	 * @param position the position of the player
	 * @param rotation the rotation of the player
	 */
	public GameData(int id, Vector2 position, float rotation) {
		playerData = new PlayerData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_PLAYER, id, position, new Vector2(0,0), rotation);
		enemies = new ArrayList<PlayerData>();
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
	 * 
	 * @param proj the newly created Projectile
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
		for(PlayerData p: enemies) {
			if(enemyData.getId() == p.getId()) {
				p.updateData(enemyData.getPosition(), enemyData.getDirection(), enemyData.getRotation(), enemyData.getHealth(), enemyData.getShield(), enemyData.getHull());
				return;
			}
		}
		enemies.add(enemyData);
	}
	
	/**
	 * Used to remove EnemyData from enemies. Called from DataController.
	 * 
	 * @param id the id of the PlayerData to be removed
	 */
	public void removeEnemy(int id) {
		for(PlayerData p: enemies) {
			if(p.getId() == id) {
				enemies.remove(p);
				return;
			}
		}
	}
	
	public ArrayList<PlayerData> getEnemies(){
		return enemies;
	}
	
	/**
	 * Adds a new Projectile to the GameData ArrayList of Projectiles.
	 * 
	 * @param projectile the Projectile to be added.
	 */
	public void addProjectile(Projectile projectile) {
		ProjectileData projectileData = new ProjectileData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_PROJECTILE, projectile.getId(), 
				projectile.getPosition(), projectile.getDirection(), projectile.getRotation(), projectile.getLifeTime(), false);
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
	public void updateProjectile(ProjectileData pd) {
		projectilesData.put(pd.getId(), pd);
	}
	
	/**
	 * Updates the player's PlayerData iff some of the data has been changed
	 * 
	 * @param position the position of the player
	 * @param direction the direction of the player
	 * @param rotation the rotation of the player
	 */
	public void updatePlayer(Vector2 position, Vector2 direction, float rotation, int health, int shield, int hull) {
		if(direction.x != playerData.getDirection().x || direction.y != playerData.getDirection().y || playerData.getRotation() != rotation) {
			playerData.updateData(position, direction, rotation, health, shield, hull);
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
			EntityData e = (EntityData) iter.next();
			if(e.getJsonType() == JsonHeader.TYPE_PLAYER) {
				updateEnemy((PlayerData) e);
			}else if (e.getJsonType() == JsonHeader.TYPE_PROJECTILE) {
				//TODO
				updateProjectile((ProjectileData) e);
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

}
