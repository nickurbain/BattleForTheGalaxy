package data;

public class PlayerMatchStat {
	private int kills;
	private int deaths;
	private int hitPoints;
	private int playerId;
	private int damageDealt;
	
	public PlayerMatchStat(int playerId) {
		this.playerId = playerId;
		kills = deaths = 0;
		hitPoints = 100;
		damageDealt = 0;
	}
	
	public PlayerMatchStat(int kills, int deaths, int hitPoints, int playerId, int damageDealt) {
		this.playerId = playerId;
		this.kills = kills;
		this.deaths = deaths;
		this.hitPoints = hitPoints;
		this.damageDealt = damageDealt;
	}
	
	public int getId() {
		return playerId;
	}
	
	public void takeDmg(int damage) {
		hitPoints -= damage;
	}

	public void respawn() {
		this.hitPoints = 100;
	}
	
	public int getHP() {
		return hitPoints;
	}
	
	public int getKills() {
		return kills;
	}
	
	public void addKill() {
		this.kills += 1;
	}
	
	public int getDeaths() {
		return deaths;
	}
	
	public void addDeath() {
		this.deaths += 1;
	}

	public int getDamageDealt() {
		return damageDealt;
	}

	public void addDamageDealt(int damageDealt) {
		this.damageDealt += damageDealt;
	}
}