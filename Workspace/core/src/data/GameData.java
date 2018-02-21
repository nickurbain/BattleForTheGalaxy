package data;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import battle.galaxy.Projectile;

/*
 * GameData is the class that stores the basic data for the game
 * that is sent/received though the server. 
 */
public class GameData {
	//Data for the client player
	private PlayerData playerData;
	//Data for enemy player
	private ArrayList<PlayerData> enemies;
	//Data for a new projectile to be sent to server
	private ProjectileData newProjectile;
	private ArrayList<ProjectileData> projectiles;
	
	//State that tracks whether changes need to be sent to server
	private boolean playerUpdated;
	//State that tells the game to add/update an enemy
	private boolean enemyAdded;
	
	public GameData(int id, Vector2 position, float rotation) {
		playerData = new PlayerData(id, position, new Vector2(0,0), rotation);
		enemies = new ArrayList<PlayerData>();
		projectiles = new ArrayList<ProjectileData>(); 
		playerUpdated= false;
		enemyAdded = false;
	}
	
	public void sendDataToController(DataController dataController) {
		if(playerUpdated){
			dataController.updateServerData(playerData, newProjectile);
			newProjectile = null;
		}
	}
	
	public void addEnemy(PlayerData enemyData) {
		enemies.add(enemyData);
		enemyAdded = true;
	}
	
	public void removeEnemy(PlayerData enemyData) {
		enemies.remove(enemyData);
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
	
	public void updatePlayer(Vector2 position, Vector2 delta, float rotation) {
		if(delta.x != playerData.getDelta().x || delta.y != playerData.getDelta().y || playerData.getRotation() != rotation) {
			playerData.updateData(position, delta, rotation);
			playerUpdated = true;
		}else {
			playerUpdated = false;
		}
	}
	
	public void getUpdateFromController() {
		
	}

}
