package data;

/**
 * Class which contains relevant ship information for a player.
 * This includes health, shield, projectile range/damage, speed, and armor.
 */
public class Ship {
	
	//BLASTER
	public final static int BLASTER_DEFAULT = 0;
	public final static int BLASTER_SHORT = 1;
	public final static int BLASTER_LONG = 2;
	//SHIELD
	public final static int SHIELD_DEFAULT = 0;
	public final static int SHIELD_LIGHT = 1;
	public final static int SHIELD_HEAVY = 2;
	//ARMOR
	public final static int ARMOR_DEFAULT = 0;
	public final static int ARMOR_LIGHT = 1;
	public final static int ARMOR_HEAVY = 2;
	//THRUSTER
	public final static int THRUSTER_DEFAULT = 0;
	public final static int THRUSTER_LIGHT = 1;
	public final static int THRUSTER_HEAVY = 2;
	
	private transient int health;
	private transient int damage;
	private transient float distance;
	private transient int velocity;
	private transient int shieldVal;
	private transient int armorVal;
	
	private int blasterType;
	private int shieldType;
	private int armorType;
	private int thrusterType;
	
	/**
	 * Constructor which creates a default ship
	 */
	public Ship() {
		blasterType = BLASTER_DEFAULT;
		shieldType = SHIELD_DEFAULT;
		armorType = ARMOR_DEFAULT;
		thrusterType = THRUSTER_DEFAULT;
	}
	
	/**
	 * Constructor that creates a ship from another ship
	 * @param ship the ship to create froms
	 */
	public Ship(Ship ship){
		this.blasterType = ship.getBlasterType();
		this.shieldType = ship.getShieldType();
		this.armorType = ship.getArmorType();
		this.thrusterType = ship.getThrusterType();
	}
	
	/**
	 * Calculate health, blaster damage/distance, shield, armor and velocity
	 */
	public void calcStats() {
		health = 100;
		//SHIELD
		switch(blasterType) {
			case BLASTER_DEFAULT:
				damage = 30;
				distance = 1;
				break;
			case BLASTER_SHORT:
				damage = 40;
				distance = 0.5f;
				break;
			case BLASTER_LONG:
				damage = 20;
				distance = 3;		
		}
		//SHIELD
		switch(shieldType) {
			case SHIELD_DEFAULT:
				shieldVal = 100;
				break;
			case SHIELD_LIGHT:
				shieldVal = 75;
				break;
			case SHIELD_HEAVY:
				shieldVal = 125;
				break;
		}
		//ARMOR
		switch(armorType) {
			case ARMOR_DEFAULT:
				armorVal = 100;
				break;
			case ARMOR_LIGHT:
				armorVal = 50;
				break;
			case ARMOR_HEAVY:
				armorVal = 150;
				break;
		}
		//THRUSTER
		switch(thrusterType) {
			case THRUSTER_DEFAULT:
				velocity =  600;
				break;
			case THRUSTER_LIGHT:
				velocity = 800;
				break;
			case THRUSTER_HEAVY:
				velocity = 400;
				break;
		}
	}
	
	/**
	 * @return health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param health health to set
	 */
	public void setHealth(int health) {
		this.health = health;
	}
	
	/**
	 * @return shield value
	 */
	public int getShield() {
		return shieldVal;
	}

	/**
	 * @param shieldVal the shield value to set
	 */
	public void setShield(int shieldVal) {
		this.shieldVal = shieldVal;
	}

	public void dealDamage(int damage) {
		this.health -= damage;
	}

	/**
	 * @return damage the damage of a projectile
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * @param damage the damage of a projectile to set
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	/**
	 * @return the distance
	 */
	public float getRange() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setRange(float distance) {
		this.distance = distance;
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

	/**
	 * @return the armorVal
	 */
	public int getArmorVal() {
		return armorVal;
	}

	/**
	 * @param armorVal the armorVal to set
	 */
	public void setArmorVal(int armorVal) {
		this.armorVal = armorVal;
	}

	/**
	 * @return the blaster
	 */
	public int getBlasterType() {
		return blasterType;
	}

	/**
	 * @param blaster the blaster to set
	 */
	public void setBlasterType(int blaster) {
		this.blasterType = blaster;
	}

	/**
	 * @return the shieldType
	 */
	public int getShieldType() {
		return shieldType;
	}

	/**
	 * @param shieldType the shieldType to set
	 */
	public void setShieldType(int shieldType) {
		this.shieldType = shieldType;
	}

	/**
	 * @return the armorType
	 */
	public int getArmorType() {
		return armorType;
	}

	/**
	 * @param armorType the armorType to set
	 */
	public void setArmorType(int armorType) {
		this.armorType = armorType;
	}

	/**
	 * @return the thrusterType
	 */
	public int getThrusterType() {
		return thrusterType;
	}

	/**
	 * @param thrusterType the thrusterType to set
	 */
	public void setThrusterType(int thrusterType) {
		this.thrusterType = thrusterType;
	}

}
