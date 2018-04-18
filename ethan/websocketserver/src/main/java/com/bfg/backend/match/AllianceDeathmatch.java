package com.bfg.backend.match;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.WebSocketSession;

public class AllianceDeathmatch extends AbstractMatch {
	
	private List<Team> alliances;
	private Integer killLimit;
	
	AllianceDeathmatch() {
		setMatchType("AllianceDeathmatch");
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
		
		super.welcomeMessageWithTeam(player, p.getTeam());
		super.addClientToBC(player);
	}
	
	public void addPlayerToAlliances(Player p) {
		if(alliances.isEmpty()) {
			alliances.add(new Team(p.getTeam()));
		}
	}
	
}