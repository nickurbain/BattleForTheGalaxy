package data;

public class HitData extends JsonHeader {

	private int projectileId;
	private int playerId;
	private int damage;
	
	public HitData(int originClient, int typeDeath, int projectileId, int playerId, int damage) {
		super(originClient, typeDeath);
		this.setProjectileId(projectileId);
		this.setPlayerId(playerId);
		
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
	}}
