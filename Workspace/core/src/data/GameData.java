package data;

import java.util.ArrayList;
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
	private ProjectileData newProjectile;
	private ArrayList<ProjectileData> projectiles;
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
		projectiles = new ArrayList<ProjectileData>(); 
	}
	
	/**
	 * Sends data to the DataController if the data has been updated
	 * 
	 * @param dataController the Game's DataControllerss
	 */
	public void sendDataToController(DataController dataController) {
		dataController.updateServerData(playerData, null);
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
	
	public void addProjectile(Projectile projectile) {
		ProjectileData projectileData = new ProjectileData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_PROJECTILE, 
				projectile.getPosition(), projectile.getDirection(), projectile.getRotation(), projectile.getLifeTime(), false);
		projectiles.add(projectileData);
	}
	
	public void removeProjectile(ProjectileData projectileData) {
		projectiles.remove(projectileData);
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
			}
		}
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
