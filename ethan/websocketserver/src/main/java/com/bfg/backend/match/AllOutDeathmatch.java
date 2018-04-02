package com.bfg.backend.match;

import java.util.List;

public class AllOutDeathmatch extends AbstractMatch{
	public Integer killLimit;
	
	public AllOutDeathmatch() {
		killLimit = 10;
	}
	
	
	/*
	 * Checks if the match has ended
	 */
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
	
	
}
