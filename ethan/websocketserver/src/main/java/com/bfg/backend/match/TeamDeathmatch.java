package com.bfg.backend.match;

import java.util.List;

import org.springframework.web.socket.WebSocketSession;

/**
 * 
 * @author emball
 *
 */
public class TeamDeathmatch extends AbstractMatch {
	private Integer killLimit;
	private Team redTeam;
	private Team blueTeam;
	
	public TeamDeathmatch() {
		killLimit = 10;
		redTeam = new Team("red");
		blueTeam = new Team("blue");
		setMatchType("TEAMDEATHMATCH");
	}
	
	// TODO Need to add the player to super, and then send them the welcome message
	@Override
	public void addPlayer(WebSocketSession player) {
		super.addPlayerStd(player);
		Player p = getPlayer(player);
		
		if(redTeam.getMembers().isEmpty()) {
			redTeam.addMember(p);
			addTeamtoPlayer(p, "red");
		}
		else if(blueTeam.getMembers().isEmpty()) {
			blueTeam.addMember(p);
			addTeamtoPlayer(p, "blue");
		}
		else {
			if(redTeam.getMembers().size() <= blueTeam.getMembers().size()) {
				redTeam.addMember(p);
				addTeamtoPlayer(p, "red");
			}
			else {
				blueTeam.addMember(p);
				addTeamtoPlayer(p, "blue");
			}	
		}
		
		super.welcomeMessage(player);
		super.addClientToBC(player);
	}
	
	public void addTeamtoPlayer(Player player, String team) {
		player.setTeam(team);
	}
	
	
	/**
	 * Checks if the match has ended
	 */
	@Override
	public boolean checkEndMatch() {
		if(redTeam.getTeamKills() >= killLimit) {
			System.err.println("KILL LIMIT REACHED! ENDING GAME. WINNER: RED TEAM");
			endMatch();
			return true;

		}
		
		if(blueTeam.getTeamKills() >= killLimit) {
			System.err.println("KILL LIMIT REACHED! ENDING GAME. WINNER: BLUE TEAM");
			endMatch();
			return true;
		}
		
		System.out.println("BLUE TEAM TOTAL KILLS: " + blueTeam.getTeamKills() + "\nRED TEAM TOTAL KILLS: " + redTeam.getTeamKills());
		return false;
	}
	
	@Override
	public void registerKill(Player player, Player enemy) {
		System.out.println("REGISTERKILL IN TEAMDEATHMATCH CLASS");
		super.registerKill(player, enemy);
		if(enemy.getTeam().equals("red")) {
			redTeam.addTeamKill();
		}
		else {
			blueTeam.addTeamKill();
		}
		// Add a kill to the team kills;
		if(checkEndMatch()) {
			super.endMatch();
		}
	}
	
//	/**
//	* Registers a hit 
//	*/
//	@Override
//	public void registerHit(Integer playerId, Integer sourceId, boolean causedDeath, Integer dmg) {
////		super.registerHit(playerId, sourceId, causedDeath, dmg);
//		Player player = getPlayerById(playerId);
//		Player enemy = getPlayerById(sourceId);
//		player.takeDmg(dmg);
//		enemy.addDamageDealt(dmg);
//		if(causedDeath) {
//			registerKill(player, enemy);
//		}
//	}
}
