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
	private String teamColor;
	private Integer teamKills;
	private Integer teamId;
	
	/**
	 * Creates a team with a given teamId
	 * 
	 * @param teamId
	 */
	public Team(Integer teamId) {
		teamKills = 0;
		this.setTeamId(teamId);
		members = new CopyOnWriteArrayList<>();
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
	 * Gets the team color
	 * 
	 * @return
	 * 		The string of the team color
	 */
	public String getTeamColor() {
		return teamColor;
	}

	/**
	 * Sets the team color
	 * 
	 * @param teamColor
	 */
	public void setTeamColor(String teamColor) {
		this.teamColor = teamColor;
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
}
