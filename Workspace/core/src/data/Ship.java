package data;

public class Ship {
	
	private int health;
	private int shield;
	private int damage;
	private int velocity;
	
	public Ship() {
		health = 100;
		shield = 100;
		damage = 30;
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

	public void damage(int damage) {
		this.health -= damage;
	}

	public int getDamage() {
		return damage;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * @return the velocity
	 */
	public int getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

}
