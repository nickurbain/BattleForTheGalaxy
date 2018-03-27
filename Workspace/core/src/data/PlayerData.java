package data;

import com.badlogic.gdx.math.Vector2;

public class PlayerData extends EntityData{
	
	private int health;
	private int shield;
	
	public PlayerData(int jsonOrigin, int jsonType, int id, Vector2 pos, Vector2 direction, float rotation) {
		super(jsonOrigin, jsonType, id, pos, direction, rotation);
		health = 100;
		shield = 100;
	}
	
	public PlayerData() {
		super();
	}

	public void updateData(Vector2 position, Vector2 direction, float rotation, int health, int shield) {
		this.setPosition(position);
		this.setDirection(direction);
		this.setRotation(rotation);
		this.health = health;
		this.shield = shield;
	}
	
	public void updateData(PlayerData pd) {
		this.setPosition(pd.getPosition());
		this.setDirection(pd.getDirection());
		this.setRotation(pd.getRotation());
		this.health = pd.getHealth();
		this.shield = pd.getShield();
	}

	public void hit(HitData e) {
		this.health -= e.getDamage();
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
	
}
