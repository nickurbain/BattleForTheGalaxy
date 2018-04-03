package com.bfg.backend.match;

import java.util.List;

public class AllOutDeathmatch extends AbstractMatch{
	private Integer killLimit;
	
	public AllOutDeathmatch() {
		killLimit = 10;
		setMatchName("AllOutDeathmatch");
	}
	
	
	
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
	
	
	@Override
	public void registerHit(Integer playerId, Integer sourceId, boolean causedDeath, Integer dmg) {
		super.registerHit(playerId, sourceId, causedDeath, dmg);
		Player player = getPlayerById(playerId);
		Player enemy = getPlayerById(sourceId);
		if(causedDeath) {
			registerKill(player, enemy);
		}
	}
	
	
	@Override
	public void registerKill(Player player, Player enemy) {
		super.registerKill(player, enemy);
		checkEndMatch();
	}
	
	
}
