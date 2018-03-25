package com.bfg.backend;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.bfg.backend.repository.UserRepository;
import com.bfg.backend.BroadcastThread;
import com.bfg.backend.LoginThread;

@Controller
public class SocketHandler extends TextWebSocketHandler {

	@Autowired
	private UserRepository userRepository;

//	private BroadcastThread bc;
	
	private Match match;

	enum jsonType {
		LOGIN,			// 0
		SHIP_DATA,		// 1
		LOCATION,		// 2
		PROJECTILE,		// 3
		HIT,			// 4
		DEATH,			// 5
		RESPAWN,		// 6
		QUIT,			// 7
		DB_FRIEND,		// 8
		DB_STATS,		// 9
		DB_SHIP,		// 10
		MATCH_STATS,	// 11
		JOIN_MATCH		// 12
	};
	

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {

		JsonObject jsonObj = new JsonParser().parse(message.getPayload()).getAsJsonObject();
		
		mainController(session, message, jsonObj);
		
		/* Test prints */
//		testPrints(jsonObj);
//		shortTest(jsonObj, session);
	}

	/*
	 * The main message handling method. Basically the routing controller.
	 */
	private void mainController(WebSocketSession session, TextMessage message, JsonObject jsonObj) throws IOException {
		
		// Prints out what we received immediately
//		System.out.println("rc: " + message.getPayload());
		
		// Login
		if (jsonObj.get("jsonType").getAsInt() == jsonType.LOGIN.ordinal()) {
			login(session, jsonObj);
		}
		// Quit
		else if(jsonObj.get("jsonType").getAsInt() == jsonType.QUIT.ordinal()) {
			match.removePlayer(session);
		}
		// Join match
		else if(jsonObj.get("jsonType").getAsInt() == jsonType.JOIN_MATCH.ordinal()) {
			if(match.isMatchOver()) {
				match = new Match();
			}
			if(!match.isClientInMatch(session)) {
				match.addPlayer(session);
			}
		}
		// Otherwise, in a match so broadcast
		else {
			// Check if in match
			if(match.isClientInMatch(session)) {
				// Match stats
				if(jsonObj.get("jsonType").getAsInt() == jsonType.MATCH_STATS.ordinal()) {
					JsonObject stats = match.getStats();
					System.out.println("Match stat sent to client (not on BC thread): " + stats.toString());
					session.sendMessage(new TextMessage(stats.toString()));
				}
				
				// kills/deaths
				if(jsonObj.get("jsonType").getAsInt() == jsonType.DEATH.ordinal()) {
					match.registerKill(match.getPlayerMatchId(session), match.getPlayerMatchId(session));
				}
				
				match.addMessageToBroadcast(message);	
			}
			else {
				System.out.println("Client not currently in a match -- No one to broadcast to!");
			}
		}
	}
	

	/*
	 * Initialized the broadcasting thread bc The PostConstruct annotation is used
	 * to run this method only once when the server starts
	 */
	@PostConstruct
	public void init() {
//		bc = new BroadcastThread(0);
//		bc.start();
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
//		bc.addClient(session);
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
//		bc.removeClient(session);
		
		if(match.isClientInMatch(session)) {
			match.removePlayer(session);
		}
		
		super.afterConnectionClosed(session, status);
	}
	
	
	
	/****** Testing methods *******/

	/*
	 * Tests login
	 */
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
	private void shortTest(JsonObject jsonObj, WebSocketSession session) {
		System.out.println("Session ID: " + session.getId() + " | Sent ID: " + jsonObj.get("id").getAsString());
	}
	
	/*
	 * Adds a message to the message queue in the broadcasting thread
	 */
//	public void addMessageToBroadcast(TextMessage message) throws IOException {
//		bc.addMessage(message);
//	}
	
	
//	System.out.println(
//			"######################################################################################################################");
//	System.out.println(
//			"######################################################################################################################");
//	System.out.println(
//			"######################################################################################################################");
//	System.out.println(
//			"######################################################################################################################");


}