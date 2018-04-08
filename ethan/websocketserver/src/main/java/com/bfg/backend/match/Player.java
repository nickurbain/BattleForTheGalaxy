package com.bfg.backend.match;

/**	
 * Player class stores information about a given player in a match.
 * Used to track ID for a match, kills, deaths, hp, damage delt, and if they are on a team.
 * 
 * @author emball
 *
 */
public class Player {
	private Integer kills;
	private Integer deaths;
	private Integer hitPoints;
	private Integer playerId;
	private Integer damageDealt;
	private String team;
	
	/**
	 * Constructor, takes in a player ID and initialized player stats.
	 * 
	 * @param playerId
	 */
	public Player(Integer playerId) {
		this.playerId = playerId;
		kills = deaths = 0;
		hitPoints = 100;
		damageDealt = 0;
		this.setTeam(null);
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer getId() {
		return playerId;
	}
	
	/**
	 * 
	 * @param damage
	 */
	public void takeDmg(Integer damage) {
		hitPoints -= damage;
		if(hitPoints <= 0) {
			hitPoints = 0;
		}
	}

	/**
	 * 
	 */
	public void respawn() {
		this.hitPoints = 100;
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer getHP() {
		return hitPoints;
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer getKills() {
		return kills;
	}
	
	/**
	 * 
	 */
	public void addKill() {
		this.kills += 1;
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer getDeaths() {
		return deaths;
	}
	
	/**
	 * 
	 */
	public void addDeath() {
		this.deaths += 1;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getDamageDealt() {
		return damageDealt;
	}

	/**
	 * 
	 * @param damageDealt
	 */
	public void addDamageDealt(Integer damageDealt) {
		this.damageDealt += damageDealt;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTeam() {
		return team;
	}

	/**
	 *   
	 * @param team
	 */
	public void setTeam(String team) {
		this.team = team;
	}
}