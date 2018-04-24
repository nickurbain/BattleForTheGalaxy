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
		startTimer(240);
	}
	
	
	@Override
	public void addPlayerAlliance(WebSocketSession player, String allianceName) {
		super.addPlayerStd(player);
		Player p = getPlayer(player);
		
		Boolean exists = false;
		
		for(Team alliance : alliances) {
			if(alliance.getTeamName().equals(allianceName)) {
				alliance.addMember(p);
				p.setTeam(alliance.getTeamId());
				exists = true;
			}
		}
		
		if(!exists) {
			Team alliance  = new Team(alliances.size());
			alliance.setTeamId(alliances.size());
			alliance.setTeamName(allianceName);
			alliance.addMember(p);
			p.setTeam(alliance.getTeamId());
			alliances.add(alliance);
		}
		
		super.welcomeMessageWithTeam(player, p.getTeam());
		super.addClientToBC(player);
	}
	
	/**
	 * Checks if the match has ended
	 */
	@Override
	public boolean checkEndMatch() {
		for(Team alliance : alliances) {
			if(alliance.getTeamKills() >= killLimit) {
				System.out.println("ALLIANCE " + alliance.getTeamName() + " WINS! " + alliance.getTeamKills());
				return true;
			}
		}
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
		super.registerKill(player, enemy);
			
		alliances.get(enemy.getTeam()).addTeamKill();
		
		if(checkEndMatch()) {
			super.endMatch();
		}
	}
	
	
	
	
}
