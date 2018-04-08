package com.bfg.backend.match;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Team {
	private List<Player> members;
	private String teamColor;
	private Integer teamKills;
	
	public Team(String teamColor) {
		teamKills = 0;
		this.teamColor = teamColor;
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
}
