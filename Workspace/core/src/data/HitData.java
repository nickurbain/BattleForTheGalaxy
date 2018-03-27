package data;

public class HitData extends JsonHeader {

	private int projectileId;
	private int playerId;
	private int damage;
	private boolean causedDeath;
	
	public HitData(int originClient, int typeHit, int projectileId, int playerId, int damage, boolean causedDeath) {
		super(originClient, typeHit);
		this.setProjectileId(projectileId);
		this.setPlayerId(playerId);
		this.setCausedDeath(causedDeath);
	}
	
	public HitData() {
		super();
	}

	/**
	 * @return the projectileId
	 */
	public int getProjectileId() {
		return projectileId;
	}

	/**
	 * @param projectileId the projectileId to set
	 */
	public void setProjectileId(int projectileId) {
		this.projectileId = projectileId;
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

	public void setCausedDeath(boolean causedDeath) {
		this.causedDeath = causedDeath;
	}
	
	public boolean getCausedDeath() {
		return causedDeath;
	}
	
}


