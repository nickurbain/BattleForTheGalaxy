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
import com.bfg.backend.enums.ServerJsonType;
import com.bfg.backend.match.AbstractMatch;
import com.bfg.backend.match.MatchFactory;
import com.bfg.backend.match.Player;
import com.bfg.backend.model.Alliance;
import com.bfg.backend.model.User;
import com.bfg.backend.repository.AllianceRepository;
import com.bfg.backend.repository.BattleStatsRepository;
import com.bfg.backend.repository.ItemRepository;
import com.bfg.backend.repository.UserRepository;
import com.bfg.backend.threads.AllianceThread;
import com.bfg.backend.threads.BroadcastThread;
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
	
	@Autowired
	private AllianceRepository allyRepo;	// Autowired for dependency injection to the database with Spring
	
	@Autowired
	private ItemRepository itemRepo;		//Autowired for dependency injection to the database with Spring
	
	private MatchFactory mf;				// The match factory used to build matches
	private BroadcastThread chat;
	private List<AbstractMatch> matches;

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
		
		JsonObject jsonObj = new JsonParser().parse(message.getPayload()).getAsJsonObject();
		int type = jsonObj.get("jsonType").getAsInt();
		
		AbstractMatch matchy = isPlayerInAMatch(session);
		// We do this check twice, but the other needs the json things and that needs parsing. We want to optimize for broadcasting speed, so we will take the hit on everything else
		if(matchy != null) {
			// Player data
			if(type == 2) {
				matchy.addMessageToLocationBC(message);
			}
			else {
				matchy.addMessageToBroadcast(message);
			}
		}
		
		
		// Prints out what we received immediately
//		System.out.println("rc: " + message.getPayload());
		
//		System.out.println("Json Type: " + type);
		
		cleanMatches(); 	// Clean the matches
		
		// Immediately add the message to the queue if we can
		if(matchy != null) {
			handleInMatchMessage(session, jsonObj, matchy);
		}
		else if(type == ClientJsonType.LOGIN.ordinal() || type == ClientJsonType.REGISTRATION.ordinal()) {
			userQuery(session, jsonObj, type);
		} else if (type == ClientJsonType.ALLIANCE_CREATE.ordinal() || type == ClientJsonType.ALLIANCE_JOIN.ordinal()) {
			allianceQuery(session, jsonObj, type);
			//System.out.println("Excuting second query");
			//userQuery(session, jsonObj, type);
		} else if (type == ClientJsonType.ALLIANCE_RETRIEVE.ordinal()) {
			allianceRetrieve(session, type);
		}
		else if(type == ClientJsonType.JOIN_MATCH.ordinal()) {
			if(!jsonObj.has("matchType")) {
				System.err.println("No matchtype given on JOIN_MATCH message!");
				return;
			}
			checkMatch(session, jsonObj.get("matchType").getAsInt(), jsonObj);
		}
		else if(type == ClientJsonType.CHAT.ordinal()) {
			chat(session, jsonObj, message);
		}
		else if(type == ClientJsonType.MINING_DOUBLOONS.ordinal()) {
			System.out.println("Recieved " + jsonObj.get("amount").getAsInt() + " for " + OnlineUsers.getUser(session).getName());
			Integer currDoubloons = userRepository.getDoubloonsByUsername(OnlineUsers.getUser(session).getName());
			currDoubloons += jsonObj.get("amount").getAsInt();
			userRepository.addDoubloons(currDoubloons, OnlineUsers.getUser(session).getName());
		}
		else if(type == ClientJsonType.GET_DOUBLOONS.ordinal()) {
			getDoubloons(session);
		}
		else {
			System.out.println("Invalid message!");
		}
	}
	
	public void chat(WebSocketSession session, JsonObject jsonObj, TextMessage message) throws IOException {
		System.out.println("I recieved a chat message to: " + jsonObj.get("to"));
		System.out.println(message.getPayload());
		if(jsonObj.get("to").getAsString().equals("all")) {
			// Broadcast to everyone
			System.out.println("Broadcast to everyone");
			chat.addMessage(message);
		}
		else {
			// Check which player we want to send to.
			String player = jsonObj.get("to").getAsString();
			long player_id = getUserId(player);
			if(OnlineUsers.userOnline(player_id)) {
				WebSocketSession sendTo = OnlineUsers.getUserSessionById(player_id);
				sendTo.sendMessage(message);	
			}
		}
	}
	
	
	public void getDoubloons(WebSocketSession session) throws IOException {
		User user = OnlineUsers.getUser(session);
		int doubloons = userRepository.getDoubloonsByUsername(user.getName());
		user.setDoubloons(doubloons);
		JsonObject json = new JsonObject();
		json.addProperty("jsonOrigin", 0);
		json.addProperty("jsonType", ServerJsonType.GET_DOUBLOONS.ordinal());
		json.addProperty("doubloons", doubloons);
		session.sendMessage(new TextMessage(json.toString()));
	}
	

	/**
	 * Checks which matchtype we want to create/join
	 * 
	 * @param session
	 * @param matchType
	 */
	public void checkMatch(WebSocketSession session, int matchType, JsonObject jsonObj) {
		if(matches.isEmpty() || !matchExists(matchType)) {
			buildNewMatch(matchType);
		}
		
		if(matchExists(matchType) && matchIsFull(matchType)) {
			buildNewMatch(matchType);
		}
		
		if(isPlayerInAMatch(session) == null) {
			
			addPlayerToOpenMatch(session, matchType, jsonObj);
			
//			if(matchType == 2) {
//				getMatchByType(matchType).addPlayerAlliance(session, jsonObj.get("alliance").getAsString());
//			}
//			else {
//				getMatchByType(matchType).addPlayer(session);
//			}
		}
	}
	
	
	public Boolean matchIsFull(int matchType) {
		for(AbstractMatch am : matches) {
			if(am.isMatchFull()) {
				return true;
			}
		}
		return false;
	}
	
	
	public void addPlayerToOpenMatch(WebSocketSession session, int matchType, JsonObject jsonObj) {
		System.out.println("Add player to Open match!");
		AbstractMatch am = getNextOpenMatchType(matchType);
		if(matchType == 2) {
			am.addPlayerAlliance(session, jsonObj.get("alliance").getAsString());
		}
		else {
			am.addPlayer(session);
		}
	}
	
	
	private AbstractMatch getNextOpenMatchType(int matchType) {
		for(AbstractMatch am : matches) {
			if((am.getMatchType().ordinal() == matchType) && !am.isMatchFull()) {
				return am;
			}
		}
		return null;
	}

	/**
	 * Checks if a given player is in a match and returns the match type if true.
	 * Return null if false.
	 * 
	 * @param 
	 * 		session
	 * @return
	 * 		AbstractMatch of type player is in if true
	 * 		Null if false
	 */
	public AbstractMatch isPlayerInAMatch(WebSocketSession session) {
		for(AbstractMatch am : matches) {
			if(am.isPlayerInMatch(session)) {
				return am;
			}
		}
		return null;
	}
	
	/**
	 * Returns an AbstractMatch of the matchType correlating to the int given.
	 * Null if it doesn't exist.
	 * 
	 * @param 
	 * 		matchType to get
	 * @return
	 * 		AbstractMatch if true
	 * 		Null if false
	 */
	public AbstractMatch getMatchByType(int matchType) {
		for(AbstractMatch am : matches) {
			if(am.getMatchType().ordinal() == matchType) {
				return am;
			}
		}
		return null;
	}
	
	
	/**
	 * Checks if a match currently exists
	 * 
	 * @param 
	 * 		matchType
	 * @return
	 * 		True if the match exists
	 * 		False otherwise
	 */
	public boolean matchExists(int matchType) {
		for(AbstractMatch am : matches) {
			if(am.getMatchType().ordinal() == matchType) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Builds a new match based upon which matchtype a user tries to join,
	 * and if the user is the first one to join.
	 * 
	 * @param matchType
	 */
	public void buildNewMatch(int matchType) {	
		matches.add(mf.buildMatch(matchType));
//		AbstractMatch am = getMatchByType(matchType);	// TODO
		System.out.println("New Match built! " + getMatchByType(matchType).getMatchType());
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
	public void handleInMatchMessage(WebSocketSession session, JsonObject jsonObj, AbstractMatch am) throws IOException {
		if (jsonObj.get("jsonType").getAsInt() == ClientJsonType.HIT.ordinal()) {
			Integer dmg = jsonObj.get("damage").getAsInt();
			
			am.registerHit(jsonObj.get("playerId").getAsInt(), jsonObj.get("sourceId").getAsInt(),
					jsonObj.get("causedDeath").getAsBoolean(), dmg);
		}

		if (jsonObj.get("jsonType").getAsInt() == ClientJsonType.RESPAWN.ordinal()) {
			Player p = am.getPlayer(session);
			am.respawn(p.getId());
		}
		
		if (jsonObj.get("jsonType").getAsInt() == ClientJsonType.MATCH_STATS.ordinal()) {
			String stats = am.getStats();
			session.sendMessage(new TextMessage(stats));
			System.out.println("Match stat sent to single client (not on BC thread): ");
			System.out.println(stats);
		}

		if (jsonObj.get("jsonType").getAsInt() == ClientJsonType.QUIT.ordinal()) {
			am.removePlayer(session);
			cleanMatches();
		}

	}

	/**
	 * Initialized the broadcasting thread bc The PostConstruct annotation is used
	 * to run this method only once when the server starts
	 */
	@PostConstruct
	public void init() {
		mf = new MatchFactory();
		matches = new CopyOnWriteArrayList<>();
		chat = new BroadcastThread(1);
		chat.start();
		OnlineUsers.setInstance();
	}
	
	
	public void cleanMatches() {
		for(AbstractMatch am : matches) {
			if(am.getPlayerListSize() == 0) {
				am.endMatch();
				matches.remove(am);
			}
		}
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
		boolean logged_in = false;
		if (jsonObj.has("id") && jsonObj.has("pass")) {
			User user = new User();
			user.setName(jsonObj.get("id").getAsString());
			user.setPass(jsonObj.get("pass").getAsString());
			
			if(OnlineUsers.userOnline(session)) {
				System.out.println("USER " + user.getName() + " ALREADY LOGGED IN!");
			}

			System.out.println("SocketHandler: (Name: " + user.getName() + ", Pass: " + user.getPass() + ")");
			
			LoginThread l = new LoginThread(userRepository, user, session, type, logged_in);
			l.start();
		} else if (jsonObj.has("admiral")) {
			User user = new User();
			user.setName(jsonObj.get("admiral").getAsString());
			LoginThread l = new LoginThread(userRepository, user, session, type, logged_in);
			l.start();
		} else {
			System.out.println("Invalid JSON format for LOGIN: " + jsonObj.toString());
		}
	}
	
	public long getUserId(String name) {
		long id = userRepository.findIdByUsername(name);
		return id;
	}

	public void allianceQuery(WebSocketSession session, JsonObject jsonObj, int type) {
		// TODO
		Alliance alliance = new Alliance();
		alliance.setAlliance_name(jsonObj.get("alliance").getAsString());
		String user = jsonObj.get("member").getAsString();
		
		System.out.println("SocketHandler ~ Create an alliance");
		System.out.println("SH ~ (User: " + user + ", Guild: " + alliance.getAlliance_name() + ")");
		AllianceThread a = new AllianceThread(allyRepo, userRepository, alliance, user, session, type);
		a.start();
	}
	
	public void allianceRetrieve(WebSocketSession session, int type) {
		AllianceThread l = new AllianceThread(allyRepo, session, type);
		l.start();
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
		chat.addClient(session);
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

		AbstractMatch am = isPlayerInAMatch(session);
		if(am != null) {
			am.removePlayer(session);
		}
		
		chat.removeClient(session);
		
		cleanMatches();

		OnlineUsers.removeUser(session);

		super.afterConnectionClosed(session, status);
	}
}