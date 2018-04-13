package com.bfg.backend;

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
	
	
}
