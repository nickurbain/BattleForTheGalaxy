package com.bfg.backend;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
//import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.enums.ClientJsonType;
import com.bfg.backend.enums.MatchType;
import com.bfg.backend.match.AbstractMatch;
import com.bfg.backend.match.MatchFactory;
import com.bfg.backend.repository.UserRepository;
import com.google.gson.JsonObject;

import junit.framework.TestCase;

/**
 * 
 * @author emball
 *
 */
public abstract class MatchTest extends TestCase {
	@Autowired
	private UserRepository userRepository;
	
//	private final HttpHeaders headers = new HttpHeaders();
	
	protected WebSocketSession player1 = Mockito.mock(WebSocketSession.class);
	protected WebSocketSession player2 = Mockito.mock(WebSocketSession.class);
	
	private AbstractMatch m;
	private MatchFactory mf;
	private SocketHandler handler;
	
	/* Just change this to test a different match type */
	private MatchType type;
	
	public void setType(MatchType type) {
		this.type = type;
	}
	
	public AbstractMatch getMatch() {
		return m;
	}
	
	public SocketHandler getSocketHandler() {
		return handler;
	}
	
	
	public void init() {
		mf = new MatchFactory();
		m = mf.buildMatch(type);
		handler = new SocketHandler();
		handler.init();
	}
	
	
	@Test
	public void testMatchType() {
		init();
		assertEquals(m.getMatchType(), type);
		m.endMatch();
	}
	
	@Test
	public void testAddPlayer() {
		init();
		
		m.addPlayer(player1);
		assertTrue(m.isPlayerInMatch(player1));
		m.removePlayer(player1);
		
		m.endMatch();
	}
	
	@Test
	public void testKillHPDmgDealtDeaths() {
		init();
		
		m.addPlayer(player1);
		m.addPlayer(player2);
		
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
	public void test9Kills() {
		init();
		
		m.addPlayer(player1);
		m.addPlayer(player2);
		addNKills(9, player1, player2);
		int deaths = m.getPlayer(player1).getDeaths();
		assertEquals(deaths, 9);
		int kills = m.getPlayer(player2).getKills();
		assertEquals(9, kills);
		
		m.endMatch();
	}
	
	@Test
	public void testEndMatch() {
		init();
		
		m.addPlayer(player1);
		m.addPlayer(player2);
		addNKills(10, player1, player2);
		assertTrue(m.isMatchOver());
		
		m.endMatch();
	}
	
	@Test
	public void testRespawn() {
		init();
		
		m.addPlayer(player1);
		m.addPlayer(player2);
		
		m.registerHit(m.getPlayer(player1).getId(), m.getPlayer(player2).getId(), true, 30);
		int hp = m.getPlayer(player1).getHP();
		assertEquals(70, hp);
		
		m.respawn(m.getPlayer(player1).getId());
		hp = m.getPlayer(player1).getHP();
		assertEquals(100, hp);
		
		m.endMatch();
	}
	
	@Test
	public void testWhoWinsFirst() {
		init();
		WebSocketSession player3 = Mockito.mock(WebSocketSession.class);
		WebSocketSession player4 = Mockito.mock(WebSocketSession.class);
		m.addPlayer(player1);
		m.addPlayer(player2);
		m.addPlayer(player3);
		m.addPlayer(player4);
		
		addNKills(4, player2, player3);
		addNKills(2, player4, player1);
		
		assertTrue(!m.isMatchOver());
		
		addNKills(1, player1, player2);
		addNKills(1, player3, player4);
		
		// TODO
	}
	
	
	protected void addNKills(int n, WebSocketSession playerA, WebSocketSession playerB) {
		int i;
		for(i = 0; i < n; i++) {
			m.registerHit(m.getPlayer(playerA).getId(), m.getPlayer(playerB).getId(), true, 30);
		}
	}
	
	
	/* 
	@After
	public void end() {
		m.endMatch();
	}
	*/
}
