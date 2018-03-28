package data;

public class HitData extends JsonHeader {

	private int sourceId;
	private int playerId;
	private int damage;
	private boolean causedDeath;
	
	public HitData(int originClient, int typeHit, int sourceId, int playerId, int damage, boolean causedDeath) {
		super(originClient, typeHit);
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
	 * @param projectileId the projectileId to set
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
	 * 
	 * @param causedDeath
	 */
	public void setCausedDeath(boolean causedDeath) {
		this.causedDeath = causedDeath;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getCausedDeath() {
		return causedDeath;
	}
	
}


