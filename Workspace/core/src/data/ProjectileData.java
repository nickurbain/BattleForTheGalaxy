package data;

import com.badlogic.gdx.math.Vector2;

import entities.Projectile;

public class ProjectileData extends EntityData {

	private float lifeTime;
	private int friendly;
	private int damage;
	
	public ProjectileData(int jsonOrigin, int jsonType, int id, Vector2 position, Vector2 direction, float rotation, float lifeTime, int friendly, int damage) {
		super(jsonOrigin, jsonType, id, position, direction, rotation);
		this.lifeTime = lifeTime - 0.2f;
		this.friendly = friendly;
		this.damage = damage;
	}
	
	public ProjectileData(Projectile projectile) {
		super(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_PROJECTILE, projectile.getId(), projectile.getPosition(), projectile.getDirection(), projectile.getRotation());
		this.lifeTime = projectile.getLifeTime();
		this.friendly = projectile.getFriendly();
		this.damage = projectile.getDamage();
	}
	
	public ProjectileData() {
		super();
	}
	
	public float getlifeTime() {
		return lifeTime;
	}
	
	public int getFriendly() {
		return friendly;
	}
	
	public void setFriendly(int friendly) {
		this.friendly = friendly;
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
	
	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getDamage() {
		return damage;
	}
	
}
