package com.bfg.backend.match;

import java.util.List;

/**
 * AllOutDeathmatch extends AbstractMatch
 * It is a free-for-all style deathmatch.
 * First player to 10 kills wins the match.
 * 
 * @author emball
 *
 */
public class AllOutDeathmatch extends AbstractMatch{
	private Integer killLimit;	// Limit of kills before the match ends
	
	/**
	 * Constructor. Initializes the kill limit to 10 and sets the match type
	 */
	public AllOutDeathmatch() {
		killLimit = 10;
		setMatchType("ALLOUTDEATHMATCH");
	}
	
	/**
	 * Overrides super's checkEndMatch to check the kills against the killLimit for each player to see if anyone has won.
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
	
	/*
	@Override
	public void registerHit(Integer playerId, Integer sourceId, boolean causedDeath, Integer dmg) {
		super.registerHit(playerId, sourceId, causedDeath, dmg);
		Player player = getPlayerById(playerId);
		Player enemy = getPlayerById(sourceId);
		if(causedDeath) {
			registerKill(player, enemy);
		}
	}
	*/
	
	/**
	 * Sent by the destroyed player, registers their death and the kill for the enemy who had the last hit.
	 */
	@Override
	public void registerKill(Player player, Player enemy) {
		super.registerKill(player, enemy);
		if(checkEndMatch()) {
			super.endMatch();
		}
	}
}