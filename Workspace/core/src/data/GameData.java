package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;

import controllers.DataController;
import entities.Player;
import entities.Projectile;

/**
 * GameData stores information pertaining to entities in the game for 
 * sending/recieving from the server.
 */
public class GameData{
	//Data for the client player
	private PlayerData playerData;
	//Data for enemy player
	private HashMap<Integer, PlayerData> enemies;
	//Data for a new projectile to be sent to server
	//private ProjectileData newProjectile;
	private HashMap<Integer, ProjectileData> projectilesData;
	//Data for CoreUpdates
	private ArrayList<CoreData> coreUpdates;
	//Time remaining in the game
	private long gameTime;
	private long startTime;
	private boolean isOver = false;
	private int matchId;
	private int teamNum;
	
	private String recentKill = "";
	private int score;
	
	/**
	 * Constructor w/ params based off of players initial construction
	 * @param matchData the match information
	 */
	public GameData(NewMatchData matchData, String username) {
		setMatchId(matchData.getMatchId());
		setTeamNum(matchData.getTeamNum());
		setGameTime(matchData.getTime() * 1000);
		startTime = System.currentTimeMillis();
		playerData = new PlayerData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_PLAYER, matchId, teamNum, new Vector2(0,0), new Vector2(0,0), 0, username);
		score = 0;
		enemies = new HashMap<Integer, PlayerData>();
		projectilesData = new HashMap<Integer, ProjectileData>(); 
		setCoreUpdates(new ArrayList<CoreData>());
	}
	
	/**
	 * Updates PlayerData of enemies if a PlayerData with the given id
	 * already exist, if not, creates new enemy.
	 * @param enemyData Data for an enemy from the server
	 */
	public void updateEnemy(PlayerData enemyData) {
		if(!enemies.containsKey(enemyData.getId())) {
			enemies.put(enemyData.getId(), enemyData);
		}else {
			enemies.get(enemyData.getId()).updateData(enemyData);
		}
	}
	
	/**
	 * Updates an enemy from a HitData
	 * @param e The HitData to update the enemy with
	 */
	public void updateEnemy(HitData hit) {
		if(enemies.containsKey(hit.getPlayerId())) {
			PlayerData pd = enemies.get(hit.getPlayerId());
			enemies.get(hit.getPlayerId()).hit(hit);
			if(hit.getCausedDeath()) {
				pd.reset();
			}
		}
		
		if(hit.getCausedDeath()) {
			recentKill = "Player " + Integer.toString(hit.getSourceId()) + " has killed Player " + Integer.toString(hit.getPlayerId());
			System.out.println(recentKill);
		}
		
	}
	
	/**
	 * Update an enemy or the player to be the Juggernaut.
	 * @param json
	 */
	public void updateToJuggernaut(JuggernautData jugData) {
		if(jugData.getPrevId() != -1) {
			if(enemies.containsKey(jugData.getPrevId())) {
				System.out.println("GameData updateToJuggernaut: Removing Juggernaut");
				enemies.get(jugData.getPrevId()).removeJuggernaut();
			}else {
				System.out.println("GameData updateToJuggernaut: Removing Juggernaut from Player");
				playerData.removeJuggernaut();
			}
		}
		
		if(enemies.containsKey(jugData.getCurrId())) {
			System.out.println("GameData updateToJuggernaut: Making Juggernaut");
			enemies.get(jugData.getCurrId()).makeJuggernaut();
		}else {
			playerData.makeJuggernaut();
			System.out.println("GameData updateToJuggernaut: Making Juggernaut Player");
		}
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
	
	/**
	 * Remove a projectile from gameData
	 * @param id The id of the projectile to be removeds
	 */
	public void removeProjectile(int id) {
		projectilesData.remove(id);
	}
	
	/**
	 * Used to remove EnemyData from enemies. Called from DataController.
	 * @param id the id of the PlayerData to be removed
	 */
	public void removeEnemy(int id) {
		if(enemies.containsKey(id)) {
			enemies.remove(id);
		}
	}
	
	/**
	 * Update the player's PlayerData
	 * @param player the player entity
	 */
	public void updatePlayer(Player player) {
		playerData.updateData(player.getPosition(), player.getDirection(), player.getRotation(), player.getShip().getHealth(), player.getShip().getShield());
	}
	
	/**
	 * Pull parsed data from the DataController and update in-game entities with it 
	 * @param dataController
	 */
	public void getUpdateFromController(DataController dataController) {
		for(Iterator<Object> iter = dataController.getRxFromServer().iterator(); iter.hasNext();) {
			Object o = iter.next();
			try {
				JsonHeader e =  (JsonHeader) o;
				switch(e.getJsonOrigin()) {
				case JsonHeader.ORIGIN_SERVER:
					updateGameStatus(e);
					iter.remove();
					break;
				case JsonHeader.ORIGIN_CLIENT:
					updateEntities(e);
					iter.remove();
					break;
			}
			}catch (ClassCastException c){
				System.out.println("GAME OVER: " + (String) o);
				setOver(true);
				iter.remove();
			}
		}
	}
	
	/**
	 * Update game parameters from the server
	 * @param json The json string containing updates
	 */
	public void updateGameStatus(JsonHeader json) {
		switch(json.getJsonType()) {
			case JsonHeader.TYPE_MATCH_NEW:
				break;
			case JsonHeader.TYPE_MATCH_END:
				setOver(true);
				break;
			case JsonHeader.SELECT_JUGGERNAUT:
				System.out.println("New Juggernaut");
				updateToJuggernaut((JuggernautData) json);
				break;
		}
	}

	/**
	 * Updates entities from the server
	 * @param json The json string containing the updates
	 */
	public void updateEntities(JsonHeader json) {
		switch(json.getJsonType()) {
			case JsonHeader.TYPE_PLAYER:
				if(((PlayerData) json).getId() != matchId) {
					updateEnemy((PlayerData) json);
				}
				break;
			case JsonHeader.TYPE_PROJECTILE:
				projectilesData.put(((ProjectileData) json).getId(), (ProjectileData) json);
				break;
			case JsonHeader.TYPE_HIT:
				updateEnemy((HitData) json);
				break;
			case JsonHeader.TYPE_CORE_UPDATE:
				coreUpdates.add((CoreData) json);
				break;
		}
	}

	/**
	 * Returns the player data
	 * @return player The player data
	 */
	public PlayerData getPlayerData() {
		return playerData;
	}
	
	/**
	 * Returns a list of enemy data
	 * @return list of enemy data
	 */
	public HashMap<Integer, PlayerData> getEnemies(){
		return enemies;
	}
	
	/**
	 * Gets the HashMap of projectile data
	 * @return projectilesData The HashMap containing projectile data
	 */
	public HashMap<Integer, ProjectileData> getProjectileData(){
		return projectilesData;
	}
	
	/**
	 * Gets the time the user has been playing for
	 * @return gameTime The time the user has been playing for
	 */
	public long getGameTime() {
		return gameTime;
	}

	/**
	 * Sets the game time
	 * @param gameTime The time to set to
	 */
	public void setGameTime(int gameTime) {
		this.gameTime = gameTime;
	}
	
	/**
	 * Updates the game time based on current system time and start time
	 */
	public void updateGameTime() {
		long currTime = System.currentTimeMillis();
		gameTime = gameTime - (System.currentTimeMillis() - startTime);
		startTime = currTime;
	}
	
	/**
	 * The most recent kill, used for kill feed.
	 * @return
	 */
	public String getRecentKill() {
		return recentKill;
	} 

	/**
	 * Gets whether the game is over or not
	 * @return isOver
	 */
	public boolean isOver() {
		return isOver;
	}

	/**
	 * @param isOver the isOver to set
	 */
	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void addScore(int amount) {
		this.score += amount;
	}
	
	/**
	 * @param amount the score to set
	 */
	public void setScore(int amount) {
		this.score = amount;
	}

	/**
	 * @return the matchId
	 */
	public int getMatchId() {
		return matchId;
	}

	/**
	 * @param matchId the matchId to set
	 */
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}

	/**
	 * @return the teamId
	 */
	public int getTeamNum() {
		return teamNum;
	}

	/**
	 * @param teamId the teamId to set
	 */
	public void setTeamNum(int teamNum) {
		this.teamNum = teamNum;
	}

	/**
	 * @return the coreUpdates
	 */
	public ArrayList<CoreData> getCoreUpdates() {
		return coreUpdates;
	}

	/**
	 * @param coreUpdates the coreUpdates to set
	 */
	public void setCoreUpdates(ArrayList<CoreData> coreUpdates) {
		this.coreUpdates = coreUpdates;
	}

}
