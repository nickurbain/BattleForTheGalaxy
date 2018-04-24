package com.bfg.backend.match;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.enums.MatchType;

public class AllianceDeathmatch extends AbstractMatch {
	
	private List<Team> alliances;
	private Integer killLimit;
	
	AllianceDeathmatch() {
		setMatchType(MatchType.ALLIANCEDEATHMATCH);
		alliances = new CopyOnWriteArrayList<>();
		killLimit = 10;
		startTimer(180);
	}
	
	
	@Override
	public void addPlayer(WebSocketSession player) {
		super.addPlayerStd(player);
		Player p = getPlayer(player);
		
		/* TODO
		 * Check which alliance a user is in, use that to add them to a team.
		 */
		
		super.welcomeMessage(player);
//		super.welcomeMessageWithTeam(player, p.getTeam());	// TODO No team assigned at this point yet
//		super.addClientToBC(player);
	}
	
	public void addPlayerToAlliances(Player p) {
		if(alliances.isEmpty()) {
			alliances.add(new Team(p.getTeam()));
		}
	}
	
	/**
	 * Checks if the match has ended
	 */
	@Override
	public boolean checkEndMatch() {
		if(alliances.get(0).getTeamKills() >= killLimit) {
			System.err.println("KILL LIMIT REACHED! ENDING GAME. WINNER: RED TEAM");
			endMatch();
			return true;

		}
		
		if(alliances.get(1).getTeamKills() >= killLimit) {
			System.err.println("KILL LIMIT REACHED! ENDING GAME. WINNER: BLUE TEAM");
			endMatch();
			return true;
		}
		
		System.out.println("BLUE TEAM TOTAL KILLS: " + alliances.get(1).getTeamKills() + "\nRED TEAM TOTAL KILLS: " + alliances.get(0).getTeamKills());
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
			alliances.get(0).addTeamKill();
		}
		else {
			alliances.get(1).addTeamKill();
		}
		// Add a kill to the team kills;
		if(checkEndMatch()) {
			super.endMatch();
		}
	}
	
	
	
	
}
