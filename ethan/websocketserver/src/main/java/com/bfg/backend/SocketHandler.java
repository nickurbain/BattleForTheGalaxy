package com.bfg.backend;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.bfg.backend.enums.ClientJsonType;
import com.bfg.backend.repository.BattleStatsRepository;
import com.bfg.backend.repository.UserRepository;


@Controller
public class SocketHandler extends TextWebSocketHandler {

	@Autowired
	private UserRepository userRepository;
	
	private Match match;
	private List<WebSocketSession> online;
	

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
		mainController(session, message);
	}

	
	/*
	 * The main message handling method. Basically the routing controller.
	 */
	private void mainController(WebSocketSession session, TextMessage message) throws IOException {
		
		JsonObject jsonObj = new JsonParser().parse(message.getPayload()).getAsJsonObject();
		
		// Prints out what we received immediately
		System.out.println("rc: " + message.getPayload());
		
		
		if(match.isPlayerInMatch(session)) {
			match.addMessageToBroadcast(message);
			handleInMatchMessage(session, jsonObj);
		}
		else if (jsonObj.get("jsonType").getAsInt() == ClientJsonType.LOGIN.ordinal()) {  // jsonType.LOGIN.ordinal()
			login(session, jsonObj);
		}
		else if(jsonObj.get("jsonType").getAsInt() == ClientJsonType.JOIN_MATCH.ordinal()) { // jsonType.JOIN_MATCH.ordinal()
			if(match.isMatchOver()) {
				match = new Match();
			}
			if(!match.isPlayerInMatch(session)) {
				match.addPlayer(session);
			}
		}
		else {
			System.out.println("Client not currently in a match -- No one to broadcast to!");
		}
	}
	
	/*
	 * Handles messages for players in a match
	 */
	public void handleInMatchMessage(WebSocketSession session, JsonObject jsonObj) throws IOException {
		// Match stats
		if(jsonObj.get("jsonType").getAsInt() == ClientJsonType.MATCH_STATS.ordinal()) {
//			JsonObject stats = match.getStats();
			String stats = match.getStats();
			System.out.println("Match stat sent to client (not on BC thread): " + stats);
			session.sendMessage(new TextMessage(stats));
		}
		
		if(jsonObj.get("jsonType").getAsInt() == ClientJsonType.QUIT.ordinal()) { //jsonType.QUIT.ordinal()
			match.removePlayer(session);
		}
		
		if(jsonObj.get("jsonType").getAsInt() == ClientJsonType.HIT.ordinal()) {
			// If we want to add in other damage amounts
			// Integer dmg = jsonObj.get("dmg").getAsInt();
		
			match.registerHit(jsonObj.get("playerId").getAsInt(), jsonObj.get("sourceId").getAsInt(), jsonObj.get("causedDeath").getAsBoolean(), 30);
		}
		
		if(jsonObj.get("jsonType").getAsInt() == ClientJsonType.RESPAWN.ordinal()) {
			Player p = match.getPlayer(session);
			match.respawn(p.getId());
		}
		
//		if(jsonObj.get("jsonType").getAsInt() == ClientJsonType.DEATH.ordinal()) {
////	match.registerKill(match.getPlayerMatchId(session), match.getPlayerMatchId(session));
//match.registerKill(match.getPlayerById(jsonObj.get("playerId").getAsInt()), match.getPlayerById(jsonObj.get("sourceId").getAsInt()));
//}
	}
	

	/*
	 * Initialized the broadcasting thread bc The PostConstruct annotation is used
	 * to run this method only once when the server starts
	 */
	@PostConstruct
	public void init() {
		online = new CopyOnWriteArrayList<>();
		match = new Match();
	}
	

	/*
	 * Checks if it is a valid user in the database
	 */
	public void login(WebSocketSession session, JsonObject jsonObj) {
		if(jsonObj.has("id") && jsonObj.has("pass")) {
			User user = new User();
			user.setName(jsonObj.get("id").getAsString());
				user.setPass(jsonObj.get("pass").getAsString());
			
			LoginThread l = new LoginThread(userRepository, user, session);
			l.start();	
		}
		else {
			System.out.println("Invalid JSON format for LOGIN: " + jsonObj.toString());
		}
	}
	

	/*
	 * Handles new websocket connections Makes a thread for each new session
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.socket.handler.AbstractWebSocketHandler#
	 * afterConnectionEstablished(org.springframework.web.socket.WebSocketSession)
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("********Websocket Connection OPENED!********");
		System.out.println("WS session ID: " + session.getId());
		System.out.println("********************************************");
		online.add(session);
	}
	

	/*
	 * Handles websocket session disconnect Prints to the console for debugging and
	 * tracking purposes
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.socket.handler.AbstractWebSocketHandler#
	 * afterConnectionClosed(org.springframework.web.socket.WebSocketSession,
	 * org.springframework.web.socket.CloseStatus)
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("********Websocket Connection CLOSED!********");
		System.out.println("WS session ID: " + session.getId());
		System.out.println("********************************************");
		
		if(match.isPlayerInMatch(session)) {
			match.removePlayer(session);
		}
		
		online.remove(session);
		super.afterConnectionClosed(session, status);
	}
	
	
	
	
	
/****** Testing methods *******/

	/*
	 * Tests login
	 */
	@SuppressWarnings("unused")
	private void loginTests(User user, Long id) {
		System.out.println("LOGIN TEST >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("Username sent from client: " + user.getName());
		System.out.println("Password: " + user.getPass());
		System.out.println("Id FROM DATABASE: " + id);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	/*
	 * Tests the standard json given
	 */
	@SuppressWarnings("unused")
	private void testPrints(JsonObject jsonObj) {
		System.out.println("TESTPRINTS ----------------------------------------------");
		System.out.println("jsonOrigin (From client = 1, From server = 0): " + jsonObj.get("jsonOrigin").getAsInt());
		System.out.println("jsonType (0 = login, 1 = broadcast):  " + jsonObj.get("jsonType").getAsInt());
		System.out.println("Sent id: " + jsonObj.get("id").getAsString());

		if (jsonObj.get("jsonType").getAsInt() == 0) {
			System.out.println("Sent pass: " + jsonObj.get("pass").getAsString());
		}
		System.out.println("---------------------------------------------------------");
	}

	/*
	 * Tests with shorter printing to the console for easier reading of multiple
	 * threads.
	 */
	@SuppressWarnings("unused")
	private void shortTest(JsonObject jsonObj, WebSocketSession session) {
		System.out.println("Session ID: " + session.getId() + " | Sent ID: " + jsonObj.get("id").getAsString());
	}
}