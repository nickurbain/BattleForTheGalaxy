package data;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import battle.galaxy.Projectile;

public class GameData {

	private PlayerData playerData;
	private ArrayList<PlayerData> enemies;
	private ProjectileData newProjectile;
	private ArrayList<ProjectileData> projectiles;
	
	private boolean playerUpdated;
	
	public GameData(Vector2 position, float rotation) {
		playerData = new PlayerData(position, new Vector2(0,0), rotation, true);
		enemies = new ArrayList<PlayerData>();
		projectiles = new ArrayList<ProjectileData>(); 
		playerUpdated= false;
	}
	
	public void sendDataToController(DataController dataController) {
		if(playerUpdated) {
			dataController.updateGameData(playerData, newProjectile);
			newProjectile = null;
		}
	}
	
	public void addEnemy(PlayerData enemyData) {
		enemies.add(enemyData);
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

}
