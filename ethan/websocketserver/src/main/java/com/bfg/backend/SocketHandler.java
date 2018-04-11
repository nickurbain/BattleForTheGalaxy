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
import com.bfg.backend.enums.MatchType;
import com.bfg.backend.match.AbstractMatch;
import com.bfg.backend.match.AllOutDeathmatch;
import com.bfg.backend.match.MatchFactory;
import com.bfg.backend.match.Player;
import com.bfg.backend.model.User;
import com.bfg.backend.repository.BattleStatsRepository;
import com.bfg.backend.repository.UserRepository;
import com.bfg.backend.threads.LoginThread;

/**
 * Handles incoming messages from a client. This can be thought of as the 'main
 * controller' since all communication from the client passes through and is
 * handled here.
 * 
 * @author emball, jln
 *
 */
@Controller
public class SocketHandler extends TextWebSocketHandler {

	@Autowired
	private UserRepository userRepository;	// Autowired for dependency injection to the database with Spring
	
	private MatchFactory mf;				// The match factorty used to build matches
	private AbstractMatch match;			// The match currently being played
	private List<WebSocketSession> online;	// A list of online users to be used in a friends list
	private boolean initBuild;
	

	/**
	 * Sends the incoming message to the main controller for the server
	 * 
	 * @param session
	 *            The session sent
	 * @param message
	 *            The message sent
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		mainController(session, message);
	}

	/**
	 * The main message handling method. Basically the routing controller.
	 * 
	 * @param session
	 *            The session received
	 * @param message
	 *            The message recieved
	 * @throws IOException
	 */
	private void mainController(WebSocketSession session, TextMessage message) throws IOException {
		// Prints out what we received immediately
		System.out.println("rc: " + message.getPayload());
		
		JsonObject jsonObj = new JsonParser().parse(message.getPayload()).getAsJsonObject();
		
		// Immediately add the message to the queue if we can
		if(match != null && match.isPlayerInMatch(session)) {
			match.addMessageToBroadcast(message);
			handleInMatchMessage(session, jsonObj);
		}
		
		int type = jsonObj.get("jsonType").getAsInt();
		
		if (type == ClientJsonType.LOGIN.ordinal() || type == ClientJsonType.REGISTRATION.ordinal()) { // jsonType.LOGIN.ordinal()
			userQuery(session, jsonObj, type);
		}
		else {
			checkMatch(session, type);
		}
		
		
		/* Checking if jsonType exists... Do I need? TODO
		int type = 0;
		// Check jsonType for errors
		if(jsonObj.has("jsonType")) {
			type = jsonObj.get("jsonType").getAsInt();
		}
		else {
			System.err.println("Invalid jsonType! jsonType is null");
			return;
		}
		*/
	}
	
	// TODO Check which match we are joining
	public void checkMatch(WebSocketSession session, int matchType) {
		// If we haven't built a match yet, or if the match is over
		System.out.println(initBuild);
		if(!initBuild) {
			buildNewMatch(matchType);
		}
		
		if(match != null && match.isMatchOver()) {
			buildNewMatch(matchType);
		}
		
		if(match != null && !match.isPlayerInMatch(session)) {
			match.addPlayer(session);
		}
		else {
			System.out.println("Client not currently in a match -- No one to broadcast to!");	
		}
	}
	
	public void buildNewMatch(int matchType) {
		System.out.println("BUILD MATCH IN JOINMATCH IN SOCKERHANDLER!!");
		if(matchType == ClientJsonType.JOIN_MATCH.ordinal()) {
			match = mf.buildMatch(MatchType.ALLOUTDEATHMATCH);
		}
		else if(matchType == ClientJsonType.TEAMDEATHMATCH.ordinal()) {
			match = mf.buildMatch(MatchType.TEAMDEATHMATCH);
		}
		else {
			System.out.println("No joinmatch given");
		}
		
		if(match != null) {
			initBuild = true;
			System.out.println(match.getMatchType());
		}
	}
	 
	/**
	 * Handles messages for players in a match
	 * 
	 * @param session
	 *            The session used to handle the match message
	 * @param jsonObj
	 *            The Json object containing the message
	 * @throws IOException
	 */
	public void handleInMatchMessage(WebSocketSession session, JsonObject jsonObj) throws IOException {

		if (jsonObj.get("jsonType").getAsInt() == ClientJsonType.MATCH_STATS.ordinal()) {
			String stats = match.getStats();
			session.sendMessage(new TextMessage(stats));
			System.out.println("Match stat sent to single client (not on BC thread): ");
			System.out.println(stats);
		}

		if (jsonObj.get("jsonType").getAsInt() == ClientJsonType.QUIT.ordinal()) {
			match.removePlayer(session);
		}

		if (jsonObj.get("jsonType").getAsInt() == ClientJsonType.HIT.ordinal()) {
			// If we want to add in other damage amounts later
			// Integer dmg = jsonObj.get("dmg").getAsInt();

			match.registerHit(jsonObj.get("playerId").getAsInt(), jsonObj.get("sourceId").getAsInt(),
					jsonObj.get("causedDeath").getAsBoolean(), 30);
		}

		if (jsonObj.get("jsonType").getAsInt() == ClientJsonType.RESPAWN.ordinal()) {
			Player p = match.getPlayer(session);
			match.respawn(p.getId());
		}
	}

	/**
	 * Initialized the broadcasting thread bc The PostConstruct annotation is used
	 * to run this method only once when the server starts
	 */
	@PostConstruct
	public void init() {
		online = new CopyOnWriteArrayList<>();
		mf = new MatchFactory();
		initBuild = false;
//		match = mf.buildMatch(MatchType.ALLOUTDEATHMATCH);	// TODO
//		System.out.println("MATCH CREATED!");	// TODO
	}

	/**
	 * Checks if it is a valid user in the database. Spins up a loginThread to
	 * handle database queries.
	 * 
	 * @param session,
	 *            passed to the loginThread so it knows who to send responses to
	 * @param jsonObj,
	 *            the info to be used in the query
	 * @param type,
	 *            if it is a login or register query
	 */
	public void userQuery(WebSocketSession session, JsonObject jsonObj, int type) {
		if (jsonObj.has("id") && jsonObj.has("pass")) {
			User user = new User();
			user.setName(jsonObj.get("id").getAsString());
			user.setPass(jsonObj.get("pass").getAsString());

			System.out.println("SocketHandler: (Name: " + user.getName() + ", Pass: " + user.getPass() + ")");
			LoginThread l = new LoginThread(userRepository, user, session, type);
			l.start();
		} else {
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

		if (match.isPlayerInMatch(session)) {
			match.removePlayer(session);
		}

		online.remove(session);
		super.afterConnectionClosed(session, status);
	}

	/****** Testing methods *******/

	/**
	 * Printouts for login. Prints to the terminal values for a user for debugging
	 * purposes.
	 * 
	 * @param user
	 *            The user trying to login
	 * @param id
	 *            The id associated with the user
	 */
	@SuppressWarnings("unused")
	private void loginTests(User user, Long id) {
		System.out.println("LOGIN TEST >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("Username sent from client: " + user.getName());
		System.out.println("Password: " + user.getPass());
		System.out.println("Id FROM DATABASE: " + id);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	/**
	 * Prints the standard json given for debugging
	 * 
	 * @param jsonObj
	 *            The Json object to test
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

	/**
	 * Tests with shorter printing to the console for easier reading of multiple
	 * threads.
	 * 
	 * @param jsonObj
	 *            The Json object to test
	 * @param session
	 *            The session used for the test
	 */
	@SuppressWarnings("unused")
	private void shortTest(JsonObject jsonObj, WebSocketSession session) {
		System.out.println("Session ID: " + session.getId() + " | Sent ID: " + jsonObj.get("id").getAsString());
	}
}