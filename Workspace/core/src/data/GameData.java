package data;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;

import controllers.DataController;
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
	//Time remaining in the game
	private long startTime = System.currentTimeMillis();
	private long gameTime = 0;
	private boolean isOver = false;
	private int matchId;
	private int teamId;
	
	private String recentKill = "";
	private int score;
	
	/**
	 * Constructor w/ params based off of players initial construction
	 * @param id The player id
	 * @param position The position of the player
	 * @param rotation The rotation of the player
	 */
	public GameData(NewMatchData matchData, Vector2 position, float rotation) {
		setMatchId(matchData.getMatchId());
		setTeamId(matchData.getTeamId());
		playerData = new PlayerData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_PLAYER, matchId, position, new Vector2(0,0), rotation);
		score = 0;
		enemies = new HashMap<Integer, PlayerData>();
		projectilesData = new HashMap<Integer, ProjectileData>(); 
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
	 * Updates the player's PlayerData iff some of the data has been changed
	 * @param position the position of the player
	 * @param direction the direction of the player
	 * @param rotation the rotation of the player
	 */
	public void updatePlayer(Vector2 position, Vector2 direction, float rotation, int health, int shield) {
		if(direction.x != playerData.getDirection().x || direction.y != playerData.getDirection().y || playerData.getRotation() != rotation) {
			playerData.updateData(position, direction, rotation, health, shield);
		}
	}
	
	/**
	 * Pull parsed data from the DataController and update in-game entities with it 
	 * @param dataController
	 */
	public void getUpdateFromController(DataController dataController) {
		for(Iterator<Object> iter = dataController.getRxFromServer().iterator(); iter.hasNext();) {
			JsonHeader e =  (JsonHeader) iter.next();
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
		}
	}
	
	/**
	 * Updates entities from the server
	 * @param json The json string containing the updates
	 */
	public void updateEntities(JsonHeader json) {
		switch(json.getJsonType()) {
			case JsonHeader.TYPE_PLAYER:
				updateEnemy((PlayerData) json);
				break;
			case JsonHeader.TYPE_PROJECTILE:
				projectilesData.put(((ProjectileData) json).getId(), (ProjectileData) json);
				break;
			case JsonHeader.TYPE_HIT:
				updateEnemy((HitData) json);
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
	public void setGameTime(long gameTime) {
		this.gameTime = gameTime;
	}
	
	/**
	 * Updates the game time based on current system time and start time
	 */
	public void updateGameTime() {
		gameTime = System.currentTimeMillis() - startTime;
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
	public int getTeamId() {
		return teamId;
	}

	/**
	 * @param teamId the teamId to set
	 */
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

}
