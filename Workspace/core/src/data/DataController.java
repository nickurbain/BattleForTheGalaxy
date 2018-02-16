package data;

import battle.galaxy.BattleForTheGalaxy;

public class DataController {
	
	BattleForTheGalaxy game;
	
	private String userId;
	private String pass;
	
	public DataController(BattleForTheGalaxy game) {
		this.game = game;
	}

	public void updateGameData(PlayerData playerData, ProjectileData projectileData) {
		String player = game.getJson().toJson(playerData);
		String projectile = "";
		if(projectileData != null) {
			projectile = game.getJson().toJson(projectileData);
		}
		
//		System.out.println("Player: " + player);
//		System.out.println("Projectile: " + projectile);
	}
	
	
	

}
