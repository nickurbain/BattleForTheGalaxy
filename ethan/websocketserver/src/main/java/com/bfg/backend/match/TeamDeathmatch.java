package com.bfg.backend.match;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.enums.MatchType;

/**
 * TeamDeathmatch extends AbstractMatch
 * Contains logic specific for TeamDeathmatch
 * 
 * @author emball
 *
 */
public class TeamDeathmatch extends AbstractMatch {
	private Integer killLimit;
//	private Team redTeam;	// Team 0
//	private Team blueTeam;	// Team 1
	private List<Team> teams;
	
	/**
	 * TeamDeathmatch constructor does initializtion
	 */
	public TeamDeathmatch() {
		setMatchType(MatchType.TEAMDEATHMATCH);
		killLimit = 3;
		teams = new CopyOnWriteArrayList<>();
		teams.add(new Team(0));		// red
		teams.add(new Team(1));		// blue
		teams.get(0).setTeamName("red");
		teams.get(1).setTeamName("blue");
		startTimer(180);
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
		if(teams.get(0).getTeamKills() >= killLimit) {
			System.err.println("KILL LIMIT REACHED! ENDING GAME. WINNER: RED TEAM");
			endMatch();
			return true;

		}
		
		if(teams.get(1).getTeamKills() >= killLimit) {
			System.err.println("KILL LIMIT REACHED! ENDING GAME. WINNER: BLUE TEAM");
			endMatch();
			return true;
		}
		
		System.out.println("BLUE TEAM TOTAL KILLS: " + teams.get(1).getTeamKills() + "\nRED TEAM TOTAL KILLS: " + teams.get(0).getTeamKills());
		return false;
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
		System.out.println("REGISTERKILL IN TEAMDEATHMATCH CLASS");
		super.registerKill(player, enemy);
		// TODO
		if(enemy.getTeam() == 0) {
			teams.get(0).addTeamKill();
		}
		else {
			teams.get(1).addTeamKill();
		}
		// Add a kill to the team kills;
		if(checkEndMatch()) {
			super.endMatch();
		}
	}
}
