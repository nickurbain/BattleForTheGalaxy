package data;

import com.badlogic.gdx.math.Vector2;

import battle.galaxy.GameScreen;

/**
 * Data class that encapsulates relevant player data to be sent to the server and other players.
 */
public class PlayerData extends EntityData{
	
	private int health;
	private int shield;
	
	/**
	 * Constructor that takes in all arguments. Sets shield and health to 100.
	 * @param jsonOrigin
	 * @param jsonType
	 * @param id the id of the player
	 * @param pos the position of the player
	 * @param direction the direction of the player
	 * @param rotation the rotation of the player
	 */
	public PlayerData(int jsonOrigin, int jsonType, int id, Vector2 pos, Vector2 direction, float rotation) {
		super(jsonOrigin, jsonType, id, pos, direction, rotation);
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
	 * Updates the data with data recieved from the server.
	 * @param pd a PlayerData object
	 */
	public void updateData(PlayerData pd) {
		this.setPosition(pd.getPosition());
		this.setDirection(pd.getDirection());
		this.setRotation(pd.getRotation());
		this.setHealth(pd.getHealth());
		this.setShield(pd.getShield());
	}
	
	/**
	 * Reset the player to (0,0), 100 health and no direction.
	 */
	public void reset() {
		setDirection(new Vector2(0,0));
		setPosition(new Vector2(GameScreen.RESPAWN_X, GameScreen.RESPAWN_Y));
		setHealth(100);
		
	}
	
	/**
	 * Adjust health for a hit from a projectile.
	 * @param e the damage to the player
	 */
	public void hit(HitData e) {
		this.setHealth(this.getHealth() - e.getDamage());
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
	
}
