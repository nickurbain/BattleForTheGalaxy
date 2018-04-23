package com.bfg.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.enums.ClientJsonType;
import com.bfg.backend.enums.MatchType;
import com.bfg.backend.match.AbstractMatch;
import com.bfg.backend.match.MatchFactory;
import com.google.gson.JsonObject;

public class SocketHandlerTest {
	
	private WebSocketSession player1 = Mockito.mock(WebSocketSession.class);
	private WebSocketSession player2 = Mockito.mock(WebSocketSession.class);
	
	private SocketHandler handler;
	
	/* Just change this to test a different match type */
	private MatchType matchType = MatchType.ALLOUTDEATHMATCH;
	
	public void init() {
		handler = new SocketHandler();
		handler.init();
	}
	
	
	
	@Test
	public void testNewMatchOfSameType() throws Exception {
		init();
		JsonObject json = new JsonObject();
		json.addProperty("jsonOrigin", 1);
		json.addProperty("jsonType", ClientJsonType.JOIN_MATCH.ordinal());
		
		
		assertFalse(handler.matchExists(matchType.ordinal()));
		json.addProperty("matchType", matchType.ordinal());
		handler.handleMessage(player1, new TextMessage(json.toString()));
		assertTrue(handler.matchExists(matchType.ordinal()));
		
		
		int time = handler.getMatchByType(matchType.ordinal()).getTime();
		assertTrue(30 == time || 29 == time);
		handler.getMatchByType(matchType.ordinal()).setTime(50);
		time = handler.getMatchByType(matchType.ordinal()).getTime();
		assertTrue(50 == time || 49 == time);
		
		
		
		handler.getMatchByType(matchType.ordinal()).endMatch();
		assertTrue(handler.getMatchByType(matchType.ordinal()).isMatchOver());
		
		
		handler.handleMessage(player1, new TextMessage(json.toString()));
		assertFalse(handler.getMatchByType(matchType.ordinal()).isMatchOver());
		time = handler.getMatchByType(matchType.ordinal()).getTime();
		assertTrue(30 == time || 29 == time);
		
		
	}
	
	
	@Test
	public void testSocketHandler() throws Exception {
		init();
		System.out.println("Test sockethandler!!\n\n");
		JsonObject json = new JsonObject();
		json.addProperty("jsonOrigin", 1);
		
		json.addProperty("jsonType", matchType.ordinal());
		handler.handleMessage(player1, new TextMessage(json.toString()));
		handler.handleMessage(player1, new TextMessage(json.toString()));
		
		System.out.println("Add player 2:");
		handler.handleMessage(player2, new TextMessage(json.toString()));
		
		System.out.println("Ending SocketHandler tests!\n\n");
	}
	
	
	@Test
	public void testAddPlayersToMatches() throws Exception {
		init();
		
		JsonObject json = new JsonObject();
		json.addProperty("jsonOrigin", 1);
		json.addProperty("jsonType", matchType.ordinal());
		
	}
	
	
	@Test
	public void testMultipleLoginWithSameName() throws Exception {
		System.out.println("****testMultipleLoginWithSameName");
		init();
		JsonObject json = new JsonObject();
		json.addProperty("jsonOrigin", 1);
		json.addProperty("jsonType", ClientJsonType.LOGIN.ordinal());
		
		json.addProperty("id", "finn");
		json.addProperty("pass", "bork");

		handler.handleMessage(player1, new TextMessage(json.toString()));
		handler.handleMessage(player1, new TextMessage(json.toString()));
		
//		assertTrue(OnlineUsers.userOnline(player1));
		
		System.out.println("Add player 2:");
		handler.handleMessage(player2, new TextMessage(json.toString()));
		
	}
	
	@Test
	public void testCreateMatches() throws Exception {
		init();
		JsonObject json = new JsonObject();
		json.addProperty("jsonOrigin", 1);
		json.addProperty("jsonType", ClientJsonType.JOIN_MATCH.ordinal());
		
		
		assertFalse(handler.matchExists(MatchType.TEAMDEATHMATCH.ordinal()));
		json.addProperty("matchType", MatchType.TEAMDEATHMATCH.ordinal());
		handler.handleMessage(player1, new TextMessage(json.toString()));
		assertTrue(handler.matchExists(MatchType.TEAMDEATHMATCH.ordinal()));
		
			json.addProperty("jsonType", ClientJsonType.QUIT.ordinal());
			handler.handleMessage(player1, new TextMessage(json.toString()));
			json.addProperty("jsonType", ClientJsonType.JOIN_MATCH.ordinal());
		
		assertFalse(handler.matchExists(MatchType.ALLOUTDEATHMATCH.ordinal()));
		json.addProperty("matchType", MatchType.ALLOUTDEATHMATCH.ordinal());
		handler.handleMessage(player1, new TextMessage(json.toString()));
		assertTrue(handler.matchExists(MatchType.ALLOUTDEATHMATCH.ordinal()));
		
		assertFalse(handler.matchExists(MatchType.ALLIANCEDEATHMATCH.ordinal()));
		json.addProperty("matchType", MatchType.ALLIANCEDEATHMATCH.ordinal());
		System.out.println(json.toString());
		handler.handleMessage(player2, new TextMessage(json.toString()));
		assertTrue(handler.matchExists(MatchType.ALLIANCEDEATHMATCH.ordinal()));
			
			json.addProperty("jsonType", ClientJsonType.QUIT.ordinal());
			handler.handleMessage(player1, new TextMessage(json.toString()));
			json.addProperty("jsonType", ClientJsonType.JOIN_MATCH.ordinal());
		
		assertFalse(handler.matchExists(MatchType.CAPTURETHECORE.ordinal()));
		json.addProperty("matchType", MatchType.CAPTURETHECORE.ordinal());
		handler.handleMessage(player1, new TextMessage(json.toString()));
		assertTrue(handler.matchExists(MatchType.CAPTURETHECORE.ordinal()));
		
		
			json.addProperty("jsonType", ClientJsonType.QUIT.ordinal());
			handler.handleMessage(player1, new TextMessage(json.toString()));
			json.addProperty("jsonType", ClientJsonType.JOIN_MATCH.ordinal());
		
		assertFalse(handler.matchExists(MatchType.JUGGERNAUT.ordinal()));
		json.addProperty("matchType", MatchType.JUGGERNAUT.ordinal());
		handler.handleMessage(player1, new TextMessage(json.toString()));
		assertTrue(handler.matchExists(MatchType.JUGGERNAUT.ordinal()));
		
	}
	
	
}
