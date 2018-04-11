package com.bfg.backend.match;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Team object for tracking
 * 
 * @author emball
 *
 */
public class Team {
	private List<Player> members;
	private String teamColor;
	private Integer teamKills;
	private Integer teamId;
	
	public Team(Integer teamId) {
		teamKills = 0;
		this.setTeamId(teamId);
		members = new CopyOnWriteArrayList<>();
	}

	public List<Player> getMembers() {
		return members;
	}

	public void addMember(Player member) {
		members.add(member);
	}

	public String getTeamColor() {
		return teamColor;
	}

	public void setTeamColor(String teamColor) {
		this.teamColor = teamColor;
	}

	public Integer getTeamKills() {
		return teamKills;
	}

	public void addTeamKill() {
		teamKills += 1;
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
}
