package data;

import com.badlogic.gdx.math.Vector2;

public class ProjectileData extends EntityData {

	private float lifeTime;
	private int friendly;
	
	public ProjectileData(byte jsonOrigin, byte jsonType, int id, Vector2 position, Vector2 direction, float rotation, float lifeTime, int friendly) {
		super(jsonOrigin, jsonType, id, position, direction, rotation);
		this.lifeTime = lifeTime - 0.2f;
		this.friendly = friendly;
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
	
}
