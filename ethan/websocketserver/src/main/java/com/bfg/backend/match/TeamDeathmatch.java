package com.bfg.backend.match;

import java.util.List;

/**
 * 
 * @author emball
 *
 */
public class TeamDeathmatch extends AbstractMatch {
	private Integer killLimit;
	
	public TeamDeathmatch() {
		killLimit = 10;
		setMatchType("TEAMDEATHMATCH");
	}
	
	
	/* TODO
	 * Randomly assigns a team ID.
	 * Works with team balancing.
	 */
	public Integer assignTeamId() {
		return 0;
	}
	
	/**
	 * Checks if the match has ended
	 */
	@Override
	public boolean checkEndMatch() {
		List<Player> players = getPlayers();
		for(Player player: players) {			
			if(player.getKills() >= killLimit) {
				System.err.println("	KILL LIMIT REACHED! ENDING GAME. WINNER: " + player.getId());
				endMatch();
				return true;
			}
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
		super.registerKill(player, enemy);
		if(checkEndMatch()) {
			super.endMatch();
		}
	}
	
	
}
