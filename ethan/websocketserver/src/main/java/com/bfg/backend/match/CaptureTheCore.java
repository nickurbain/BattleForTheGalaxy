package com.bfg.backend.match;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.enums.MatchType;
import com.google.gson.JsonObject;

public class CaptureTheCore extends AbstractMatch {

	private List<Team> teams;
	private Integer pointLimit;
	
	public CaptureTheCore() {
		setMatchType(MatchType.CAPTURETHECORE);
		pointLimit = 3;
		teams = new CopyOnWriteArrayList<>();
		teams.add(new Team(0));
		teams.add(new Team(1));
	}
	
	/**
	 * Adds a player to the match
	 */
	@Override
	public void addPlayer(WebSocketSession player) {
		super.addPlayerStd(player);
		Player p = getPlayer(player);
		
		if(teams.get(0).getMembers().isEmpty()) {
			teams.get(0).addMember(p);
			addTeamtoPlayer(p, 0);
		}
		else if(teams.get(1).getMembers().isEmpty()) {
			teams.get(1).addMember(p);
			addTeamtoPlayer(p, 1);
		}
		else {
			if(teams.get(0).getMembers().size() <= teams.get(1).getMembers().size()) {
				teams.get(0).addMember(p);
				addTeamtoPlayer(p, 0);
			}
			else {
				teams.get(1).addMember(p);
				addTeamtoPlayer(p, 1);
			}	
		}
		
		super.welcomeMessageWithTeam(player, p.getTeam());
		super.addClientToBC(player);
	}
	
	/**
	 * Adds the team to the player
	 * 
	 * @param player
	 * @param team
	 */
	public void addTeamtoPlayer(Player player, Integer team) {
		player.setTeam(team);
	}
	
	
	/**
	 * Checks if the match has ended
	 */
	@Override
	public boolean checkEndMatch() {	
		if(teams.get(0).getPoints() >= pointLimit) {
			System.err.println("CAPTURE LIMIT REACHED! ENDING GAME. WINNER: TEAM 0");
			endMatch();
			return true;

		}
		
		if(teams.get(1).getPoints() >= pointLimit) {
			System.err.println("CAPTURE LIMIT REACHED! ENDING GAME. WINNER: TEAM 1");
			endMatch();
			return true;
		}
		
		System.out.println("TEAM 1 TOTAL CAPTURES: " + teams.get(1).getPoints() + "\nTEAM 0 TOTAL CAPTURES: " + teams.get(0).getPoints());
		return true;
	}
	
	@Override
	public void registerHit(Integer playerId, Integer sourceId, boolean causedDeath, Integer dmg) {
		Player player = getPlayerById(playerId);
		Player enemy = getPlayerById(sourceId);
		if(player.getTeam() == enemy.getTeam()) {	// Check for team hit
			return;
		}
		
		player.takeDmg(dmg);
		enemy.addDamageDealt(dmg);
		if (causedDeath) {
			registerKill(player, enemy);
		}
	}
	
	/**
	 * Registers a kill
	 */
	@Override
	public void registerKill(Player player, Player enemy) {
		super.registerKill(player, enemy);
		if(enemy.getTeam() == 0) {
			teams.get(0).addTeamKill();
		}
		else {
			teams.get(1).addTeamKill();
		}
	}
	
	
	@Override
	public void registerScore(JsonObject jsonObj) {
		Player player = getPlayerById(jsonObj.get("playerId").getAsInt());
		
		if(player.getId() != -1) {
			int teamNum = jsonObj.get("teamNum").getAsInt();
			Boolean captured = jsonObj.get("captured").getAsBoolean();
			Team team;
			if(teamNum == 0) {
				team = teams.get(1);
			}
			else {
				team = teams.get(0);
			}
			
			
			if(captured) {
				team.addPoints(1);
				player.addPoints(100);
			} else {
				player.addPoints(10);
			}
			
			checkEndMatch();
		}
	}
	
}
