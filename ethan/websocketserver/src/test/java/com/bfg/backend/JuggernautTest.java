package com.bfg.backend;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.enums.MatchType;
import com.bfg.backend.match.AbstractMatch;

public class JuggernautTest extends MatchTest {

	MatchType type = MatchType.JUGGERNAUT;
	AbstractMatch m;
	
	@Override 
	public void init() {
		super.setType(type);
		super.init();
		m = super.getMatch();
	}
	
	
	
	@Test
	public void testAddJugg() {
		init();
		
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
		assertEquals(1, team);
		
		WebSocketSession player4 = Mockito.mock(WebSocketSession.class);
		
		m.addPlayer(player4);
		assertTrue(m.isPlayerInMatch(player4));
		team = m.getPlayer(player4).getTeam();
		assertEquals(1, team);
		
		m.endMatch();
	}
	
	
	@Override
	@Test
	public void testRespawn() {
		init();
		
	}
	
	@Override
	@Test
	public void test2Kills() {
		init();
		
		m.addPlayer(player1);
		m.addPlayer(player2);
		int team = m.getPlayer(player1).getTeam();
		assertEquals(0, team);
		
		team = m.getPlayer(player2).getTeam();
		assertEquals(1, team);
		
		addNKills(2, player2, player1);
		
		int deaths = m.getPlayer(player1).getDeaths();
		int kills = m.getPlayer(player1).getKills();
		assertEquals(deaths, 0);
		assertEquals(kills, 2);
		
		deaths = m.getPlayer(player2).getDeaths();
		kills = m.getPlayer(player2).getKills();
		
		assertEquals(2, deaths);
		assertEquals(0, kills);
		
		m.endMatch();
	}
	
	@Test
	public void testAssignNewJugg() {
		init();
		
		m.addPlayer(player1);
		m.addPlayer(player2);
		
		addNKills(1, player1, player2);
		
		int team = m.getPlayer(player1).getTeam();
		assertTrue(1 == team || 0 == team);
		
		if(team == 0) {
			int p2Team = m.getPlayer(player2).getTeam();
			assertEquals(1, p2Team);
		}
		else {
			int p2Team = m.getPlayer(player2).getTeam();
			assertEquals(0, p2Team);
		}
		
	}
	
	
	@Test
	public void testJuggWin() {
		init();
		
		m.addPlayer(player1);
		m.addPlayer(player2);
		
		addNKills(3, player2, player1);
		
		assertTrue(m.isMatchOver());
	}
	
	@Override
	@Test
	public void testKillHPDmgDealtDeaths() {

	}
	
	
}
