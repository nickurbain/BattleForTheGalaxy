package data;

import com.badlogic.gdx.math.Vector2;

import entities.Projectile;

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
		this.source = source;
		this.damage = damage;
	}
	
	public ProjectileData(Projectile projectile) {
		super(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_PROJECTILE, projectile.getId(), projectile.getPosition(), projectile.getDirection(), projectile.getRotation());
		this.lifeTime = projectile.getLifeTime();
		this.source = projectile.getSource();
		this.damage = projectile.getDamage();
	}
	
	public ProjectileData() {
		super();
	}
	
	public float getlifeTime() {
		return lifeTime;
	}
	
	public int getSource() {
		return source;
	}
	
	public void setSource(int source) {
		this.source = source;
	}
	
	public void update(float delta) {
		lifeTime -= delta;
	}
	
	// For testing with the Echo server -- delete!
	public void adjustPositionForTest() {
		this.setPosition(new Vector2(getPosition().x, getPosition().y + 150));
	}

	public boolean isDead() {
		if(this.lifeTime <= 0) {
			return true;
		}
		return false;
	}
	
	public void setDead() {
		lifeTime = 0;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getDamage() {
		return damage;
	}
	
}
