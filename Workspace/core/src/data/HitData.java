package data;

/**
 * Contains information about a projectile's collision with a player. Extends jsonHeader
 */
public class HitData extends JsonHeader {

	private int sourceId;
	private int playerId;
	private int damage;
	private boolean causedDeath;
	
	/**
	 * Constructor
	 * @param originClient
	 * @param typeHit
	 * @param sourceId the id of the player who fired the projectile
	 * @param playerId the id of the player hit by the projectile
	 * @param damage the damage the projectile did
	 * @param causedDeath if the projectile killed the player it hit or not
	 */
	public HitData(int originClient, int jsonType, int sourceId, int playerId, int damage, boolean causedDeath) {
		super(originClient, jsonType);
		this.sourceId = sourceId;
		this.playerId = playerId;
		this.causedDeath = causedDeath;
	}
	
	public HitData() {
		super();
	}

	/**
	 * @return the projectileId
	 */
	public int getSourceId() {
		return sourceId;
	}

	/**
	 * Sets the sourceId of the Hit
	 * @param sourceId The playerId of the player that shot the projectile
	 */
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * @return the playerId
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * @param playerId the playerId to set
	 */
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
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
	
	/**
	 * Set causedDeath of this Hit, meaning that the player hit has gone below 0 health
	 * @param causedDeath True if the Hit caused a death, false if hit was only wounding
	 */
	public void setCausedDeath(boolean causedDeath) {
		this.causedDeath = causedDeath;
	}
	
	/**
	 * Get causedDeath of this Hit
	 * @return causedDeath True if the Hit caused a death, false if hit was only wounding
	 */
	public boolean getCausedDeath() {
		return causedDeath;
	}
	
}


