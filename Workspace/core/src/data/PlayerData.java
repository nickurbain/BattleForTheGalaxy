package data;

import com.badlogic.gdx.math.Vector2;

public class PlayerData extends EntityData{
	
	private int health;
	private int shield;
	private int hull;
	
	public PlayerData(byte jsonOrigin, byte jsonType, int id, Vector2 pos, Vector2 direction, float rotation) {
		super(jsonOrigin, jsonType, id, pos, direction, rotation);
		health = 100;
		shield = 100;
		hull = 100;
	}
	
	public PlayerData() {
		super();
	}

	public void updateData(Vector2 position, Vector2 direction, float rotation, int health, int shield, int hull) {
		this.setPosition(position);
		this.setDirection(direction);
		this.setRotation(rotation);
		this.health = health;
		this.shield = shield;
		this.hull = hull;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getShield() {
		return shield;
	}

	public void setShield(int shield) {
		this.shield = shield;
	}

	public int getHull() {
		return hull;
	}

	public void setHull(int hull) {
		this.hull = hull;
	}
	
}
