package data;

public class Ship {
	
	private int health;
	private int shield;
	private int hull;
	
	public Ship() {
		health = 100;
		shield = 100;
		hull = 100;
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

	public void damage(int damage) {
		this.health -= damage;
	}

}
