package data;

import com.badlogic.gdx.math.Vector2;

import entities.Projectile;

/**
 * Data class that encapsulates relevant projectile data to be sent to the server and other players.
 */
public class ProjectileData extends EntityData {

	private float lifeTime;
	private int source;
	private int damage;
	
	/**
	 * Constructor for ProjectileData.
	 * 
	 * @param jsonOrigin Json Origin is Client.
	 * @param jsonType Json Type is Projectile.
	 * @param id This Projectile's unique identifier.
	 * @param position Position of this Projectile (distinction between position/direction is unclear to me at the time of writing).
	 * @param direction Direction of this Projectile (distinction between position/direction is unclear to me at the time of writing).
	 * @param rotation Rotation of this Projectile in degrees.
	 * @param lifeTime Amount of seconds this Projectile will stay on screen.
	 * @param source Player ID of the player that fired this Projectile.
	 * @param damage Amount of damage this Projectile deals on a registered hit.
	 */
	public ProjectileData(int jsonOrigin, int jsonType, int id, Vector2 position, Vector2 direction, float rotation, float lifeTime, int source, int damage) {
		super(jsonOrigin, jsonType, id, position, direction, rotation);
		this.lifeTime = lifeTime - 0.2f;
		this.setSource(source);
		this.setDamage(damage);
	}
	
	/**
	 * Constructor
	 * @param projectile a Projectile object
	 */
	public ProjectileData(Projectile projectile) {
		super(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_PROJECTILE, projectile.getId(), projectile.getPosition(), projectile.getDirection(), projectile.getRotation());
		this.lifeTime = projectile.getLifeTime();
		this.setSource(projectile.getSource());
		this.setDamage(projectile.getDamage());
	}
	
	/**
	 * Empty constructor
	 */
	public ProjectileData() {
		super();
	}
	
	/**
	 * Update the lifeTime of the projectile
	 * @param delta the time passed
	 */
	public void update(float delta) {
		lifeTime -= delta;
	}
	
	// For testing with the Echo server -- delete!
	public void adjustPositionForTest() {
		this.setPosition(new Vector2(getPosition().x, getPosition().y + 150));
	}
	
	/**
	 * Check if the projectile is dead, i.e. liftime <= 0.
	 * @return isDead
	 */
	public boolean isDead() {
		if(this.lifeTime <= 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the projectile's lifeTime to 0
	 */
	public void setDead() {
		lifeTime = 0;
	}
	
	/**
	 * @return the lifeTime
	 */
	public float getLifeTime() {
		return lifeTime;
	}

	/**
	 * @param time the time to set
	 */
	public void setLifeTime(float time) {
		this.lifeTime = time;
	}

	/**
	 * @return the source
	 */
	public int getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(int source) {
		this.source = source;
	}

	/**
	 * @return the damage
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * @param damage the damage to set
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
}
