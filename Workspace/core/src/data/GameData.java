package data;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class GameData {

	private PlayerData playerData;
	private ArrayList<PlayerData> enemies;
	private ArrayList<ProjectileData> projectiles;
	
	public GameData(Vector2 position, float rotation) {
		playerData = new PlayerData(position, new Vector2(0,0), rotation, true);
		enemies = new ArrayList<PlayerData>();
		projectiles = new ArrayList<ProjectileData>(); 
	}
	
	public void addEnemy(PlayerData enemyData) {
		enemies.add(enemyData);
	}
	
	public void removeEnemy(PlayerData enemyData) {
		enemies.remove(enemyData);
	}
	
	public void addProjectile(ProjectileData projectileData) {
		projectiles.add(projectileData);
	}
	
	public void removeProjectile(ProjectileData projectileData) {
		projectiles.remove(projectileData);
	}
	
	public void updatePlayer(Vector2 position, Vector2 delta, float rotation) {
		playerData.updateData(position, delta, rotation);
	}
	
	
}
