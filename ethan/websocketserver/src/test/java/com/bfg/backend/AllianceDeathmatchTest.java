package com.bfg.backend;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.enums.MatchType;
import com.bfg.backend.match.AbstractMatch;

public class AllianceDeathmatchTest extends MatchTest {

	MatchType type = MatchType.ALLIANCEDEATHMATCH;
	AbstractMatch m;
	
	@Override 
	public void init() {
		super.setType(type);
		super.init();
		m = super.getMatch();
	}
	
	@Override
	@Test
	public void testRespawn() {
		init();
		
		m.addPlayerAlliance(player1, "Vamsi's_Crew");
		m.addPlayerAlliance(player2, "309_Punks");
		
		m.registerHit(m.getPlayer(player1).getId(), m.getPlayer(player2).getId(), true, 30);
		int hp = m.getPlayer(player1).getHP();
		assertEquals(70, hp);
		
		m.respawn(m.getPlayer(player1).getId());
		hp = m.getPlayer(player1).getHP();
		assertEquals(100, hp);
		
		m.endMatch();
	}
	
	@Override
	@Test
	public void test2Kills() {
		init();
		
		m.addPlayerAlliance(player1, "Vamsi's_Crew");
		m.addPlayerAlliance(player2, "309_Punks");
		
		addNKills(2, player1, player2);
		int deaths = m.getPlayer(player1).getDeaths();
		assertEquals(deaths, 2);
		int kills = m.getPlayer(player2).getKills();
		assertEquals(2, kills);
		
		m.endMatch();
	}
	
	
	@Test
	public void testWin() {
		init();
		
		m.addPlayerAlliance(player1, "Vamsi's_Crew");
		m.addPlayerAlliance(player2, "309_Punks");
		
		addNKills(10, player2, player1);
//		int deaths = m.getPlayer(player1).getDeaths();
//		assertEquals(deaths, 10);
//		int kills = m.getPlayer(player2).getKills();
//		assertEquals(10, kills);
		
		assertTrue(m.isMatchOver());
		
		m.endMatch();
	}
	
	@Override
	@Test
	public void testKillHPDmgDealtDeaths() {
		init();
		
		m.addPlayerAlliance(player1, "Vamsi's_Crew");
		m.addPlayerAlliance(player2, "309_Punks");
		
		m.registerHit(m.getPlayer(player1).getId(), m.getPlayer(player2).getId(), false, 30);
		m.registerHit(m.getPlayer(player1).getId(), m.getPlayer(player2).getId(), false, 30);
		m.registerHit(m.getPlayer(player1).getId(), m.getPlayer(player2).getId(), false, 30);
		int hp = m.getPlayer(player1).getHP();
		assertEquals(10, hp);		// Check hp of player
		
		m.registerHit(m.getPlayer(player1).getId(), m.getPlayer(player2).getId(), true, 30);
		int deaths = m.getPlayer(player1).getDeaths();
		hp = m.getPlayer(player1).getHP();
		int dmgDealt = m.getPlayer(player2).getDamageDealt();
		assertEquals(1, deaths);	// Check deaths
		assertEquals(0, hp);		// Check hp of player
		assertEquals(120, dmgDealt);
		
		m.endMatch();
	}
	

	@Test
	public void testAddMultipleAlliances() {
		init();
		
		WebSocketSession player3 = Mockito.mock(WebSocketSession.class);
		WebSocketSession player4 = Mockito.mock(WebSocketSession.class);
		
		m.addPlayerAlliance(player1, "Vamsi's_Crew");
		m.addPlayerAlliance(player2, "309_Punks");
		m.addPlayerAlliance(player3, "309_Punks");
		m.addPlayerAlliance(player4, "The_Other_Guys");
		
		
		
		
		addNKills(2, player1, player2);
		int deaths = m.getPlayer(player1).getDeaths();
		assertEquals(deaths, 2);
		int kills = m.getPlayer(player2).getKills();
		assertEquals(2, kills);
		
		
		
		m.endMatch();
	}
	
	
}
