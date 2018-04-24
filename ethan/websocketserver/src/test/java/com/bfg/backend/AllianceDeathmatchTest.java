package com.bfg.backend;

import org.junit.Test;

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
	public void test2Kills() {
		
	}
	
	@Override
	@Test
	public void testKillHPDmgDealtDeaths() {
//		init();
//		
//		m.addPlayer(player1);
//		m.addPlayer(player2);
//		
//		m.registerHit(m.getPlayer(player1).getId(), m.getPlayer(player2).getId(), false, 30);
//		m.registerHit(m.getPlayer(player1).getId(), m.getPlayer(player2).getId(), false, 30);
//		m.registerHit(m.getPlayer(player1).getId(), m.getPlayer(player2).getId(), false, 30);
//		int hp = m.getPlayer(player1).getHP();
//		assertEquals(10, hp);		// Check hp of player
//		
//		m.registerHit(m.getPlayer(player1).getId(), m.getPlayer(player2).getId(), true, 30);
//		int deaths = m.getPlayer(player1).getDeaths();
//		hp = m.getPlayer(player1).getHP();
//		int dmgDealt = m.getPlayer(player2).getDamageDealt();
//		assertEquals(1, deaths);	// Check deaths
//		assertEquals(0, hp);		// Check hp of player
//		assertEquals(120, dmgDealt);
//		
//		m.endMatch();
	}
	
	@Override
	@Test
	public void testRespawn() {
//		init();
//		
//		m.addPlayer(player1);
//		m.addPlayer(player2);
//		
//		m.registerHit(m.getPlayer(player1).getId(), m.getPlayer(player2).getId(), true, 30);
//		int hp = m.getPlayer(player1).getHP();
//		assertEquals(70, hp);
//		
//		m.respawn(m.getPlayer(player1).getId());
//		hp = m.getPlayer(player1).getHP();
//		assertEquals(100, hp);
//		
//		m.endMatch();
	}
}
