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
//	private List<Integer> redTeam;
//	private List<Integer> blueTeam;
	private Team redTeam;
	private Team blueTeam;
	
	public TeamDeathmatch() {
		killLimit = 10;
		redTeam = new Team("red");
		blueTeam = new Team("blue");
		setMatchType("TEAMDEATHMATCH");
	}
	
	
	/* TODO
	 * Randomly assigns a team ID.
	 * Works with team balancing.
	 */
	public Integer assignTeamId() {
		return 0;
	}
	
	
	
	@Override
	public void addPlayer(WebSocketSession player) {
		super.addPlayer(player);
		if(redTeam.getMembers().isEmpty()) {
			redTeam.addMember(getPlayer(player));
		}
		else if(blueTeam.getMembers().isEmpty()) {
			blueTeam.addMember(getPlayer(player));
		}
		else {
			if(redTeam.getMembers().size() <= blueTeam.getMembers().size()) {
				redTeam.addMember(getPlayer(player));
			}
			else {
				blueTeam.addMember(getPlayer(player));
			}	
		}
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
		
		return false;
	}
	
	
//	/**
//	 * Registers a hit 
//	 */
//	@Override
//	public void registerHit(Integer playerId, Integer sourceId, boolean causedDeath, Integer dmg) {
//		super.registerHit(playerId, sourceId, causedDeath, dmg);
//		Player player = getPlayerById(playerId);
//		Player enemy = getPlayerById(sourceId);
//		if(causedDeath) {
//			registerKill(player, enemy);
//		}
//	}
//	
	
	@Override
	public void registerKill(Player player, Player enemy) {
		System.out.println("REGISTERKILL IN TEAMDEATHMATCH CLASS");
		super.registerKill(player, enemy);
		// Add a kill to the team kills;
		if(checkEndMatch()) {
			super.endMatch();
		}
	}
	
	
}
