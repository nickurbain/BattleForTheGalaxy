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
	
}
