package com.bfg.backend.match;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Team object for tracking teams
 * 
 * @author emball
 *
 */
public class Team {
	private List<Player> members;
	private String teamName;
	private Integer teamKills;
	private Integer teamId;
	private Integer points;
	
	/**
	 * Creates a team with a given teamId
	 * 
	 * @param teamId
	 */
	public Team(Integer teamId) {
		teamKills = 0;
		this.setTeamId(teamId);
		members = new CopyOnWriteArrayList<>();
		points = 0;
	}
	
	
	public void removeAllPlayers() {
		members.clear();
	}
	
	public Player removePlayer(Player player) {
		return members.remove(members.indexOf(player));
	}

	/**
	 * Gets the list of players in the team
	 * 
	 * @return
	 * 		The list of players
	 */
	public List<Player> getMembers() {
		return members;
	}

	/**
	 * Adds a player member to the team.
	 * 
	 * @param member
	 */
	public void addMember(Player member) {
		members.add(member);
	}

	/**
	 * Gets the total team kills
	 * 
	 * @return
	 * 		The integer of team kills registered
	 */
	public Integer getTeamKills() {
		return teamKills;
	}

	/**
	 * Adds a kill to the team kills
	 */
	public void addTeamKill() {
		teamKills += 1;
	}

	/**
	 * Gets the team id
	 * 
	 * @return
	 * 		The team id
	 */
	public Integer getTeamId() {
		return teamId;
	}

	/**
	 * Sets the team id
	 * 
	 * @param teamId
	 */
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public Integer getPoints() {
		return points;
	}

	public void addPoints(Integer points) {
		this.points += points;
	}


	public String getTeamName() {
		return teamName;
	}


	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
}
