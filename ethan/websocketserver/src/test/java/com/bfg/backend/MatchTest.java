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
public class MatchTest extends TestCase {
	@Autowired
	private UserRepository userRepository;
	
//	private final HttpHeaders headers = new HttpHeaders();
	
	private WebSocketSession player1 = Mockito.mock(WebSocketSession.class);
	private WebSocketSession player2 = Mockito.mock(WebSocketSession.class);
	
	private AbstractMatch m;
	private MatchFactory mf;
	private SocketHandler handler;
	
	/* Just change this to test a different match type */
	private MatchType type = MatchType.TEAMDEATHMATCH;
	
	/* Didn't want to work
	@Before
	public void initialize() {
		mf = new MatchFactory();
		m = mf.buildMatch(MatchType.ALLOUTDEATHMATCH);
		player1 = Mockito.mock(WebSocketSession.class);
		player2 = Mockito.mock(WebSocketSession.class);
	}
	*/
	
	public void init() {
		mf = new MatchFactory();
		m = mf.buildMatch(type);
		handler = new SocketHandler();
		handler.init();
	}
	
	
	@Test
	public void testMatchType() {
		init();
		assertEquals(m.getMatchType(), type.toString());
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
	public void testAddMultiplePlayersToTeam() {
		if(type != MatchType.TEAMDEATHMATCH) {
			return;
		}
		
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
		assertEquals(0, team);
		
		WebSocketSession player4 = Mockito.mock(WebSocketSession.class);
		
		m.addPlayer(player4);
		assertTrue(m.isPlayerInMatch(player4));
		team = m.getPlayer(player4).getTeam();
		assertEquals(1, team);
		
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
		
		addNKills(4, player1, player3);
		int kills = m.getPlayer(player3).getKills();
		int deaths = m.getPlayer(player1).getDeaths();
		
		assertEquals(0, deaths);
		assertEquals(0, kills);
		
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
	
	
	private void addNKills(int n, WebSocketSession playerA, WebSocketSession playerB) {
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
