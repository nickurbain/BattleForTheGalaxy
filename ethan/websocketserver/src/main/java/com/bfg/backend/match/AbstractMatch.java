package com.bfg.backend.match;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.JsonContainer;
import com.bfg.backend.enums.ClientJsonType;
import com.bfg.backend.enums.MatchType;
import com.bfg.backend.enums.ServerJsonType;
import com.bfg.backend.threads.BroadcastThread;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Abstract match implementation.
 * All the basic match functionality implemented here.
 * 
 * Tracks connected players
 * 
 * @author emball
 *
 */
public abstract class AbstractMatch {
	private List<WebSocketSession> playerList; // List to track players
	private ConcurrentHashMap<WebSocketSession, Player> players; // Maps players to their websocketsession

	private BroadcastThread bc; // Broadcasting thread for sending messages to clients
	private BroadcastThread playerData;
	
	// private Integer killLimit; // Tracks the kill limit for a match. Defaults to
	// 10
	private Integer idIncrementer; // Increments an id for users
	private boolean isOver; // Tracks if the match is over or not
//	private String matchType;
	private MatchType matchType;
	private Integer vamsiTime;
	
	private Integer matchCap = 4;	// How many people allowed in a match at a given time 

	/**
	 * Constructor, initializes everything
	 */
	public AbstractMatch() {
		playerList = new CopyOnWriteArrayList<>();
		bc = new BroadcastThread(1);
		playerData = new BroadcastThread(2);
		idIncrementer = 0;
		isOver = false;
		if (!bc.isAlive()) {
			bc.start();
		}
		
		if(!playerData.isAlive()) {
			playerData.start();
		}
		
		players = new ConcurrentHashMap<>();
	}
	
	public void startTimer(Integer matchTime) {
		System.out.println("Starting timer!");
		vamsiTime = matchTime;
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int i = matchTime; // In seconds 

			public void run() {
				i--;
				vamsiTime = i;
				
				if(isOver) {
					timer.cancel();
				}
				else if (i <= 0) {
					System.out.println("TIME LIMIT REACHED! ENDING GAME : " + matchType);
					endMatch();
					timer.cancel();
				}
			}
		}, 0, 1000);
	}
	
	
	/**
	 * Adds a player to the match. Initializes their kills and deaths to 0 and adds
	 * them to the broadcasting thread
	 * 
	 * @param WebSocketSession
	 *            The web socket session used to add the player
	 */
	public void addPlayer(WebSocketSession player) {
		idIncrementer++;
		playerList.add(player);

		Player p = new Player(idIncrementer);
		players.put(player, p);

		JsonContainer json = new JsonContainer();
		json.setMatchId(idIncrementer);
		json.setJsonType(ServerJsonType.NEW_MATCH.ordinal());
		json.setTime(vamsiTime);
		

		Gson gson = new Gson();
		String welcomeMessage = gson.toJson(json);

		System.out.println("Player " + p.getId() + " joined match!");
		System.out.println("	welcomeMessage sent to player: " + welcomeMessage);
		System.out.println("");

		try {
			player.sendMessage(new TextMessage(welcomeMessage));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error on sending id to client");
		}

		bc.addClient(player);
		playerData.addClient(player);
	}

	/**
	 * Adds a player to the match. Initializes their kills and deaths to 0 and adds
	 * them to the broadcasting thread
	 * 
	 * @param WebSocketSession
	 *            The web-socket session used
	 */
	public void addPlayerStd(WebSocketSession player) {
		idIncrementer++;
		playerList.add(player);

		Player p = new Player(idIncrementer);
		players.put(player, p);
	}

	/**
	 * Broadcasts a message when a player joins a match
	 * 
	 * @param player
	 *            The player joining the match.
	 */
	public void welcomeMessage(WebSocketSession player) {
		Player p = getPlayer(player);

		JsonContainer json = new JsonContainer();
		json.setMatchId(idIncrementer);
		json.setJsonType(ServerJsonType.NEW_MATCH.ordinal());
		json.setTime(vamsiTime);

		Gson gson = new Gson();
		String welcomeMessage = gson.toJson(json);

		System.out.println("Player " + p.getId() + " joined match!");
		System.out.println("	welcomeMessage sent to player: " + welcomeMessage);
		System.out.println("");

		try {
			player.sendMessage(new TextMessage(welcomeMessage));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error on sending id to client");
		}
	}
	
	/**
	 * Broadcasts a message when a player joins a match
	 * 
	 * @param player
	 *            The player joining the match.
	 */
	public void welcomeMessageWithTeam(WebSocketSession player, int teamNum) {
		Player p = getPlayer(player);

		JsonContainer json = new JsonContainer();
		json.setMatchId(idIncrementer);
		json.setJsonType(ServerJsonType.NEW_MATCH.ordinal());
		json.setTeamNum(teamNum);
		json.setTime(vamsiTime);

		Gson gson = new Gson();
		String welcomeMessage = gson.toJson(json);

		System.out.println("Player " + p.getId() + " joined match!");
		System.out.println("	welcomeMessage sent to player: " + welcomeMessage);
		System.out.println("");

		try {
			player.sendMessage(new TextMessage(welcomeMessage));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error on sending id to client");
		}
	}

	public void addClientToBC(WebSocketSession player) {
		bc.addClient(player);
		playerData.addClient(player);
	}

	/**
	 * Removes all traces of a player from the match
	 * 
	 * @param WebSocketSession
	 *            The web-socket session used
	 */
	public void removePlayer(WebSocketSession player) {
		System.out.println("Player " + getPlayer(player).getId() + " left match!");

		bc.removeClient(player);
		playerData.removeClient(player);
		
		JsonObject message = new JsonObject();
		message.addProperty("jsonOrigin", 0);
		message.addProperty("jsonType", ServerJsonType.QUIT.ordinal());
		message.addProperty("matchId", getPlayer(player).getId());		// TODO TEST-- sends leave match and player id
		bc.addMessage(new TextMessage(message.toString()));	
		
		players.remove(player);
		playerList.remove(player);
	}

	/**
	 * Returns a boolean representing if the match is over
	 * 
	 * @return isOver true is match has ended and false if not
	 */
	public boolean isMatchOver() {
		return isOver;
	}

	/**
	 * Ends the match, gets match stats and adds it to the broadcasting queue to send to clients
	 */
	public void endMatch() {
		System.out.println("Printing stats: & sending end message");

		JsonObject over = new JsonObject();
		over.addProperty("jsonOrigin", 0);
		over.addProperty("jsonType", ServerJsonType.GAME_OVER.ordinal());
		TextMessage send = new TextMessage(over.toString());
		bc.addMessage(send);

		send = new TextMessage(getStats());
		bc.addMessage(send);
		isOver = true;

		// TODO
		// Maybe wait for a little while to make sure we broadcast stats to everyone
		for (WebSocketSession player : playerList) {
			removePlayer(player);
		}

		bc.end(); // Ends the thread
		playerData.end();
	}

	/**
	 * Makes one large json object with all of the match stats in it
	 * 
	 * @return String of stats
	 */
	public String getStats() {
		System.out.println("getStats Method");

		JsonArray jr = new JsonArray();
		Gson gson = new Gson();

		int i;
		for (i = 0; i < playerList.size(); i++) {
			Player p = players.get(playerList.get(i));
			jr.add(gson.toJsonTree(p));
			getStatsPrints(p);
		}
		System.out.println("");
		
		JsonContainer json = new JsonContainer();
		json.setJsonType(ServerJsonType.MATCH_STATS.ordinal());
		json.setMatchStats(jr);

		String str = gson.toJson(json);

//		System.out.println("test 1: " + gson.toJson(json));

		return str;
	}

	/**
	 * Debugging statement that prints out player stats to the console
	 * 
	 * @param Player
	 */
	public void getStatsPrints(Player p) {
		System.out.println("  Player " + p.getId() + " stats");
		System.out.println("	Player " + p.getId() + " kills : " + p.getKills());
		System.out.println("	Player " + p.getId() + " deaths: " + p.getDeaths());
		System.out.println("	Player " + p.getId() + " hitPoints: " + p.getHP());
		System.out.println("	Player " + p.getId() + " damageDealt: " + p.getDamageDealt());
		System.out.println("");
	}

	/**
	 * Checks if the match has ended. This method to be overridden by subclasses.
	 * 
	 * @return boolean if the match is over
	 */
	public boolean checkEndMatch() {
		return isOver;
	}

	/**
	 * Returns true if a client is in a match, false otherwise.
	 * 
	 * @param WebSocketSession
	 *            The web-socket session used
	 * @return boolean true if player is in match, false otherwise
	 */
	public boolean isPlayerInMatch(WebSocketSession player) {
		return playerList.contains(player);
	}

	/**
	 * Adds a message to the broadcasting thread's queue to be sent to all connected
	 * players.
	 * 
	 * @param TextMessage
	 *            Message to broadcast
	 * @throws IOException
	 */
	public void addMessageToBroadcast(TextMessage message) throws IOException {
		bc.addMessage(message);
	}

	/**
	 * Returns the player's match id associated with the session.
	 * 
	 * @param WebSocketSession
	 *            The web-socket session used
	 * @return
	 */
	public Integer getPlayerMatchId(WebSocketSession player) {
		return players.get(player).getId();
	}

	/**
	 * Returns a given player associated with the session.
	 * 
	 * @param WebSocketSession
	 *            The web-socket session used
	 * @return Player in the session
	 */
	public Player getPlayer(WebSocketSession player) {
		return players.get(player);
	}

	/**
	 * Finds a player by their Id and returns it.
	 * 
	 * @param playerId
	 *            The player id used to find the player
	 * @return Player the player associated with the id
	 */
	public Player getPlayerById(Integer playerId) {
		for (Player player : players.values()) {
			if (player.getId() == playerId) {
				return player;
			}
		}
		return null;
	}

	/**
	 * Returns a list of players.
	 * 
	 * @return List<Player> the list of players
	 */
	public List<Player> getPlayers() {
		List<Player> playersList = new CopyOnWriteArrayList<>();
		for (Player player : players.values()) {
			playersList.add(player);
		}
		return playersList;
	}

	/**
	 * Registers a hit on a player. Player is damaged, enemy gets damage delt.
	 * 
	 * @param playerId
	 *            The player id associated with the hit
	 * @param sourceId
	 *            The enemy player id that hit the player
	 * @param causedDeath
	 *            True or False depending on if player was killed by enemy
	 * @param dmg
	 *            The amount of damage dealt to the player from the enemy
	 */
	public void registerHit(Integer playerId, Integer sourceId, boolean causedDeath, Integer dmg) {
		Player player = getPlayerById(playerId);
		Player enemy = getPlayerById(sourceId);
		player.takeDmg(dmg);
		enemy.addDamageDealt(dmg);
		if (causedDeath) {
			registerKill(player, enemy);
		}
	}

	/**
	 * Registers the death of a player, and adds the kill to the enemy who did the
	 * destroying. Player is the one who dies, enemy get the kill point.
	 * 
	 * @param player
	 *            Increments the death count for this player
	 * @param enemy
	 *            Increments the kill count for the enemy
	 */
	public void registerKill(Player player, Player enemy) {
		enemy.addKill();
		player.addDeath();
	}

	/**
	 * Finds the player by id and respawns the player in the match
	 * 
	 * @param playerId
	 *            The id of the player that needs to be respawned
	 */
	public void respawn(Integer playerId) {
		Player p = getPlayerById(playerId);
		p.respawn();
	}

	/**
	 * Retrieves the match type of this match
	 * 
	 * @return the match type for this match
	 */
	public MatchType getMatchType() {
		return matchType;
	}

	/**
	 * Sets the match type for this match
	 * 
	 * @param matchName The name of the match needing to be set.
	 */
	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}

	public void registerScore(JsonObject jsonObj) {
		// Only implemented by CaptureTheCore
		System.out.println("CAPTURE THE CORE IMPLEMENT ME");
	}

	public Integer getTime() {
		return vamsiTime;
	}

	public void setTime(Integer time) {
		this.vamsiTime = time;
	}
	
	public List<WebSocketSession> getPlayerList() {
		return playerList;
	}
	
	public Integer getPlayerListSize() {
		return playerList.size();
	}

	public void addPlayerAlliance(WebSocketSession session, String allanceName) {
		
	}

	public void addMessageToLocationBC(TextMessage message) {
		playerData.addMessage(message);
	}
	
	
	public Boolean isMatchFull() {
		if(playerList.size() == matchCap) {
			return true;
		}
		return false;
	}
	

}

	/* Lobby logic */

	// private ConcurrentHashMap<WebSocketSession, Boolean> ready = new
	// ConcurrentHashMap<>();
	// private String matchName;

	/* Join Match */
	// ready.put(player, false);

	/* StartMatch
	// If not ready
	if(!checkReady()){return;}

public void readyUp(WebSocketSession player) {
	ready.putIfAbsent(player, true);
}

public boolean checkReady() {
	// For each entry, check if it is false, if it is, return false
	for (WebSocketSession key : ready.keySet()) {
		
		System.out.println("key id : " + ready.get(key.getId()));
		
		// If the player readiness is FALSE
		if(!ready.get(key)) {
			return false;
		}
	}
	return true;
}*/
