package com.bfg.backend;

public class Player {
	private Integer kills;
	private Integer deaths;
	private Integer hitpoints;
	private Integer playerId;
	
	public Player(Integer playerId) {
		this.playerId = playerId;
		kills = deaths = 0;
		hitpoints = 100;
	}
	
	public void takeDmg(Integer damage) {
		hitpoints += damage;
	}
	
	public Integer getId() {
		return playerId;
	}
	
	public Integer getKills() {
		return kills;
	}
	
	public Integer getDeaths() {
		return deaths;
	}
	
	public Integer getHP() {
		return hitpoints;
	}
}
