package data;

import battle.galaxy.BattleForTheGalaxy;

public class DataController {
	
	BattleForTheGalaxy game;
	
	private String userId;
	private String pass;
	
	GameData gameData;
	
	public DataController(BattleForTheGalaxy game) {
		this.game = game;
	}
	
	public boolean checkLogin(String userId, String pass) {
		this.userId = userId;
		this.pass = pass;
		return true;
	}
	
	public void enterGame() {
		
	}

}
