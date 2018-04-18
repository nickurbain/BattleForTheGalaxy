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
	private ClientJsonType matchType = ClientJsonType.TEAMDEATHMATCH;
	
	public void init() {
		handler = new SocketHandler();
		handler.init();
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
		
		assertFalse(handler.matchExists(MatchType.ALLOUTDEATHMATCH.ordinal()));
		json.addProperty("matchType", MatchType.ALLOUTDEATHMATCH.ordinal());
		handler.handleMessage(player1, new TextMessage(json.toString()));
		assertTrue(handler.matchExists(MatchType.ALLOUTDEATHMATCH.ordinal()));
		
		assertFalse(handler.matchExists(MatchType.ALLIANCEDEATHMATCH.ordinal()));
		json.addProperty("matchType", MatchType.ALLIANCEDEATHMATCH.ordinal());
		handler.handleMessage(player1, new TextMessage(json.toString()));
		assertTrue(handler.matchExists(MatchType.ALLIANCEDEATHMATCH.ordinal()));
		
		assertFalse(handler.matchExists(MatchType.CAPTURETHEFLAG.ordinal()));
		json.addProperty("matchType", MatchType.CAPTURETHEFLAG.ordinal());
		handler.handleMessage(player1, new TextMessage(json.toString()));
		assertTrue(handler.matchExists(MatchType.CAPTURETHEFLAG.ordinal()));
		
		assertFalse(handler.matchExists(MatchType.JUGGERNAUT.ordinal()));
		json.addProperty("matchType", MatchType.JUGGERNAUT.ordinal());
		handler.handleMessage(player1, new TextMessage(json.toString()));
		assertTrue(handler.matchExists(MatchType.JUGGERNAUT.ordinal()));
		
	}
	
	
}
