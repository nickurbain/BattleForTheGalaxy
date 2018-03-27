package com.bfg.backend;

public class Player {
	private Integer kills;
	private Integer deaths;
	private Integer hitPoints;
	private Integer playerId;
	
	public Player(Integer playerId) {
		this.playerId = playerId;
		kills = deaths = 0;
		hitPoints = 100;
	}
	
	public Integer getId() {
		return playerId;
	}
	
	public void takeDmg(Integer damage) {
		hitPoints -= damage;
	}

	public void addHP(Integer hitPoints) {
		this.hitPoints += hitPoints;
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
}