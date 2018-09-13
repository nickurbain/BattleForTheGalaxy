package data;

import com.badlogic.gdx.math.Vector2;


/**
 * Data class that encapsulates relevant player data to be sent to the server and other players.
 */
public class PlayerData extends EntityData{
	
	private int health;
	private int shield;
	private int teamNum;
	private String playerName;
	
	/**
	 * Constructor that takes in all arguments. Sets shield and health to 100.
	 * @param jsonOrigin
	 * @param jsonType
	 * @param id the id of the player
	 * @param pos the position of the player
	 * @param direction the direction of the player
	 * @param rotation the rotation of the player
	 */
	public PlayerData(int jsonOrigin, int jsonType, int id, int teamNum, Vector2 pos, Vector2 direction, float rotation, String name) {
		super(jsonOrigin, jsonType, id, pos, direction, rotation);
		setTeamNum(teamNum);
		setPlayerName(name);
		setHealth(100);
		setShield(100);
	}
	
	/**
	 * Empty constructor
	 */
	public PlayerData() {
		super();
	}
	
	/**
	 * Constructor for testing purposes that creates an empty PlayerData object
	 * @param jsonOrigin
	 * @param jsonType
	 */
	public PlayerData(int jsonOrigin, int jsonType) {
		super(jsonOrigin, jsonType, 9);
	}

	/**
	 * Updates the data with data recieved from the server. 
	 * @param position the position of the player
	 * @param direction the direction of the player
	 * @param rotation the rotation of the player
	 * @param health the health of the player
	 * @param shield the shield of the player
	 */
	public void updateData(Vector2 position, Vector2 direction, float rotation, int health, int shield) {
		this.setPosition(position);
		this.setDirection(direction);
		this.setRotation(rotation);
		this.setHealth(health);
		this.setShield(shield);
	}
	
	/**
	 * Updates the data with data received from the server.
	 * @param pd a PlayerData object
	 */
	public void updateData(PlayerData pd) {
		this.setPosition(pd.getPosition());
		this.setDirection(pd.getDirection());
		this.setRotation(pd.getRotation());
		this.setHealth(pd.getHealth());
		this.setShield(pd.getShield());
		this.setTeamNum(pd.getTeamNum());
	}
	
	/**
	 * Used to update a player to be the Juggernaut for Juggernaut game mode.
	 */
	public void makeJuggernaut() {
		setHealth(200);
		setShield(200);
		setTeamNum(0);
	}
	
	/**
	 * Used to update a juggernaut back to defautl for Juggernaut mode.
	 */
	public void removeJuggernaut() {
		setHealth(100);
		setShield(100);
		setTeamNum(1);
	}
	
	/**
	 * Reset the player to (0,0), 100 health and no direction.
	 */
	public void reset() {
		setDirection(new Vector2(0,0));
		setPosition(new Vector2(0,0));
		setHealth(100);
		setShield(100);
	}
	
	/**
	 * Adjust health for a hit from a projectile.
	 * @param e the damage to the player
	 */
	public void hit(HitData e) {
		setHealth(getHealth() - e.getDamage());
		if(getShield() > 0) { // shields exist
			shield -= e.getDamage();
			if(shield < 0) {
				shield = 0;
			}
			
		}
		else { // out of shields
			health -= e.getDamage();
			if(health < 0) {
				health = 0;
			}
		}
	}

	/**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param health the health to set
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * @return the shield
	 */
	public int getShield() {
		return shield;
	}

	/**
	 * @param shield the shield to set
	 */
	public void setShield(int shield) {
		this.shield = shield;
	}

	/**
	 * @return the teamNum
	 */
	public int getTeamNum() {
		return teamNum;
	}

	/**
	 * @param teamNum the teamNum to set
	 */
	public void setTeamNum(int teamNum) {
		this.teamNum = teamNum;
	}

	/**
	 * @return the playerName
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @param playerName the playerName to set
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
}
