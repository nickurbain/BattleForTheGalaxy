package com.bfg.backend;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.enums.MatchType;
import com.bfg.backend.match.AbstractMatch;

public class TeamDeathmatchTest extends MatchTest {

	MatchType type = MatchType.TEAMDEATHMATCH;
	AbstractMatch m;
	
	@Override 
	public void init() {
		super.setType(type);
		super.init();
		m = super.getMatch();
	}
	
	
	@Test
	public void testAddKillOfSameTeam() {
		if(type != MatchType.TEAMDEATHMATCH) {
			return;
		}
		
		init();
		WebSocketSession player3 = Mockito.mock(WebSocketSession.class);
		WebSocketSession player4 = Mockito.mock(WebSocketSession.class);
		m.addPlayer(player1);
		m.addPlayer(player2);
		m.addPlayer(player3);
		m.addPlayer(player4);
		
		int p1Team = m.getPlayer(player1).getTeam();
		int p3Team = m.getPlayer(player3).getTeam();
		assertEquals(0, p1Team);
		assertEquals(0, p3Team);
		
		int p2Team = m.getPlayer(player2).getTeam();
		assertEquals(1, p2Team);
		
		super.addNKills(4, player1, player3);
		int kills = m.getPlayer(player3).getKills();
		int deaths = m.getPlayer(player1).getDeaths();
		
		assertEquals(0, deaths);
		assertEquals(0, kills);
		
	}
	
	@Test
	public void testAddMultiplePlayersToTeam() {
		init();
		AbstractMatch m = super.getMatch();
		
		m.addPlayer(player1);
		assertTrue(m.isPlayerInMatch(player1));
		int team = m.getPlayer(player1).getTeam();
		assertEquals(0, team);
		
		m.addPlayer(player2);
		assertTrue(m.isPlayerInMatch(player2));
		team = m.getPlayer(player2).getTeam();
		assertEquals(1, team);
		
		WebSocketSession player3 = Mockito.mock(WebSocketSession.class);
		
		m.addPlayer(player3);
		assertTrue(m.isPlayerInMatch(player3));
		team = m.getPlayer(player3).getTeam();
		assertEquals(0, team);
		
		WebSocketSession player4 = Mockito.mock(WebSocketSession.class);
		
		m.addPlayer(player4);
		assertTrue(m.isPlayerInMatch(player4));
		team = m.getPlayer(player4).getTeam();
		assertEquals(1, team);
		
		m.endMatch();
	}
	
}
