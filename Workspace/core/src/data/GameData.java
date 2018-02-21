package data;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import battle.galaxy.Projectile;

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
	//State that tracks whether changes need to be sent to server
	DataState state;
	
	/**
	 * Constructor w/ params based off of players initial construction
	 * @param id the player id
	 * @param position the position of the player
	 * @param rotation the rotation of the player
	 */
	public GameData(int id, Vector2 position, float rotation) {
		playerData = new PlayerData(id, position, new Vector2(0,0), rotation);
		enemies = new ArrayList<PlayerData>();
		projectiles = new ArrayList<ProjectileData>(); 
		state = new DataState(DataState.STAGNANT);
	}
	
	/**
	 * Sends data to the DataController if the data has been updated
	 * 
	 * @param dataController the Game's DataControllerss
	 */
	public void sendDataToController(DataController dataController) {
		if(state.getState() == DataState.CLIENT_UPDATED){
			dataController.updateServerData(playerData, newProjectile);
			newProjectile = null;
		}
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
				p.updateData(enemyData.getPosition(), enemyData.getDirection(), enemyData.getRotation());
				return;
			}
		}
		enemies.add(enemyData);
		state.setState(DataState.SERVER_UPDATED);
	}
	
	/**
	 * Used to remove EnemyData from enemies. Called from DataController.
	 * 
	 * @param id the id of the PlayerData to be removed
	 * 
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
		ProjectileData projectileData = new ProjectileData(projectile.getPosition(), projectile.getDirection(), projectile.getRotation(), projectile.getLifeTime(), false);
		projectiles.add(projectileData);
	}
	
	public void removeProjectile(ProjectileData projectileData) {
		projectiles.remove(projectileData);
	}
	
	public void newProjectile(Projectile projectile) {
		newProjectile = new ProjectileData(projectile.getPosition(), projectile.getDirection(), projectile.getRotation(), projectile.getLifeTime(), false);
	}
	
	/**
	 * Updates the player's PlayerData iff some of the data has been changed
	 * 
	 * @param position the position of the player
	 * @param direction the direction of the player
	 * @param rotation the rotation of the player
	 */
	public void updatePlayer(Vector2 position, Vector2 direction, float rotation) {
		if(direction.x != playerData.getDirection().x || direction.y != playerData.getDirection().y || playerData.getRotation() != rotation) {
			playerData.updateData(position, direction, rotation);
			state.setState(DataState.CLIENT_UPDATED);
		}else {
			state.setState(DataState.STAGNANT);
		}
	}
	
	public void getUpdateFromController() {
		//TODO
	}
	
	public int getState() {
		return state.getState();
	}
	
	public void setState(int state) {
		this.state.setState(state);
	}

}
