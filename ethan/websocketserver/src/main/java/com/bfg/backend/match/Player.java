package com.bfg.backend.match;

/**
 * Player class stores information about a given player in a match. Used to
 * track ID for a match, kills, deaths, hp, damage delt, and if they are on a
 * team.
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
	 *            The player id used to set the players statistics
	 */
	public Player(Integer playerId) {
		this.playerId = playerId;
		kills = deaths = 0;
		hitPoints = 100;
		damageDealt = 0;
		this.setTeam(null);
	}

	/**
	 * Returns the player id
	 * 
	 * @return the player id
	 */
	public Integer getId() {
		return playerId;
	}

	/**
	 * Subtracts the hit points from the health
	 * 
	 * @param damage
	 *            The amount of damage taken
	 */
	public void takeDmg(Integer damage) {
		hitPoints -= damage;
		if (hitPoints <= 0) {
			hitPoints = 0;
		}
	}

	/**
	 * The amount of hit points player will respawn with
	 */
	public void respawn() {
		this.hitPoints = 100;
	}

	/**
	 * Retrieve the amount of hit points for a player
	 * 
	 * @return the amount of hit points
	 */
	public Integer getHP() {
		return hitPoints;
	}

	/**
	 * Retrieve the amount of kills for a player
	 * 
	 * @return the amount of kills for a player
	 */
	public Integer getKills() {
		return kills;
	}

	/**
	 * Increment the player kill score
	 */
	public void addKill() {
		this.kills += 1;
	}

	/**
	 * Retrieve the amount of deaths for a player
	 * 
	 * @return the amount of deaths
	 */
	public Integer getDeaths() {
		return deaths;
	}

	/**
	 * Increments the players death count
	 */
	public void addDeath() {
		this.deaths += 1;
	}

	/**
	 * Retrieve the amount of damage dealt by a player
	 * 
	 * @return the damage dealt
	 */
	public Integer getDamageDealt() {
		return damageDealt;
	}

	/**
	 * Increments the amount of damage done
	 * 
	 * @param damageDealt
	 *            The amount of damage done
	 */
	public void addDamageDealt(Integer damageDealt) {
		this.damageDealt += damageDealt;
	}

	/**
	 * The team that this player is associated with
	 * 
	 * @return the team
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * Sets the team this player is associated with
	 * 
	 * @param team
	 *            The team to set for the player
	 */
	public void setTeam(String team) {
		this.team = team;
	}
}