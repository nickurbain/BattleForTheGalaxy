package com.bfg.backend.match;

public class Player {
	private Integer kills;
	private Integer deaths;
	private Integer hitPoints;
	private Integer playerId;
	private Integer damageDealt;
	
	public Player(Integer playerId) {
		this.playerId = playerId;
		kills = deaths = 0;
		hitPoints = 100;
		damageDealt = 0;
	}
	
	public Integer getId() {
		return playerId;
	}
	
	public void takeDmg(Integer damage) {
		hitPoints -= damage;
		if(hitPoints <= 0) {
			hitPoints = 0;
		}
	}

	public void respawn() {
		this.hitPoints = 100;
	}
	
	public Integer getHP() {
		return hitPoints;
	}
	
	public Integer getKills() {
		return kills;
	}
	
	public void addKill() {
		this.kills += 1;
	}
	
	public Integer getDeaths() {
		return deaths;
	}
	
	public void addDeath() {
		this.deaths += 1;
	}

	public Integer getDamageDealt() {
		return damageDealt;
	}

	public void addDamageDealt(Integer damageDealt) {
		this.damageDealt += damageDealt;
	}
}