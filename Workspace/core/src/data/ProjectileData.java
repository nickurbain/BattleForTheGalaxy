package data;

import com.badlogic.gdx.math.Vector2;

public class ProjectileData extends EntityData {

	private float lifeTime;
	private boolean friendly;
	
	public ProjectileData(Vector2 position, Vector2 direction, float rotation, float lifeTime, boolean friendly) {
		super(0, position, direction, rotation);
		this.lifeTime = lifeTime; 
		this.friendly = friendly;
	}
	
	public float getlifeTime() {
		return lifeTime;
	}
	
	public boolean isFriendly() {
		return friendly;
	}
	
}
