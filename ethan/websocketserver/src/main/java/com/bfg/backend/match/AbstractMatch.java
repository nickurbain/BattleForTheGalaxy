package com.bfg.backend.match;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.JsonContainer;
import com.bfg.backend.enums.ServerJsonType;
import com.bfg.backend.threads.BroadcastThread;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Abstract match implementation
 * 
 * All the basic match functionality
 * 
 * Tracks connected players
 * 
 * @author emball
 *
 */
public abstract class AbstractMatch {
	private List<WebSocketSession> playerList;						// List to track players
	private ConcurrentHashMap<WebSocketSession, Player> players;	// Maps players to their websocketsession
	
	private BroadcastThread bc; 		// Broadcasting thread for sending messages to clients
	
//	private Integer killLimit;			// Tracks the kill limit for a match. Defaults to 10
	private Integer idIncrementer;		// Increments an id for users
	private boolean isOver;				// Tracks if the match is over or not
	private String matchType;

	/**
	 * Constructor, initializes everything
	 */
	public AbstractMatch() {
		playerList = new CopyOnWriteArrayList<>();
		bc = new BroadcastThread(1);
//		killLimit = 10;
		idIncrementer = 0;
		isOver = false;
		if(!bc.isAlive()) {
			bc.start();
		}
		players = new ConcurrentHashMap<>();
	}

	/**
	 * Adds a player to the match. Initializes their kills and deaths to 0
	 * and adds them to the broadcasting thread
	 * 
	 * @param WebSocketSession
	 */
	public void addPlayer(WebSocketSession player) {
		idIncrementer++;
		playerList.add(player);

		Player p = new Player(idIncrementer);
		players.put(player, p);
		
		bc.addClient(player);
		
		JsonContainer json = new JsonContainer();
		json.setMatchId(idIncrementer);
		json.setJsonType(ServerJsonType.NEW_MATCH.ordinal());
		
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
	 * Removes all traces of a player from the match
	 * 
	 * @param WebSocketSession
	 */
	public void removePlayer(WebSocketSession player) {		
		System.out.println("Player " + getPlayer(player).getId() + " left match!");
		
		players.remove(player);
		playerList.remove(player);
		bc.removeClient(player);
	}
	
	/**
	 * Returns a boolean representing if the match is over
	 * 
	 * @return isOver
	 */
	public boolean isMatchOver() {
		return isOver;
	}

	/**
	 * Ends the match 
	 */
	public void endMatch() {
		System.out.println("Printing stats: & sending end message");
		System.out.println(getStats().toString());
		
		JsonObject over = new JsonObject();
		over.addProperty("jsonOrigin", 0);
		over.addProperty("jsonType", ServerJsonType.GAME_OVER.ordinal());
		TextMessage send = new TextMessage(over.toString());
		bc.addMessage(send);
		
		// New
		send = new TextMessage(getStats());
		bc.addMessage(send);
		isOver = true;
		
		
		// TODO
		// Maybe wait for a little while to make sure we broadcast stats to everyone
		for(WebSocketSession player: playerList) {
			removePlayer(player);
		}
		
		bc.end(); // Ends the thread
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
		for(i = 0; i < playerList.size(); i++) {
			Player p = players.get(playerList.get(i));
			jr.add(gson.toJsonTree(p));
			getStatsPrints(p);
		}
		System.out.println("");
		
		JsonContainer json = new JsonContainer();
		json.setJsonType(ServerJsonType.MATCH_STATS.ordinal());
		json.setMatchStats(jr);
		
		String str = gson.toJson(json);
		
		System.out.println("test 1: " + gson.toJson(json));
		
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
	 * Checks if the match has ended.
	 * This method to be overridden by subclasses.
	 * 
	 * @return boolean if the match is over
	 */
	public boolean checkEndMatch() {
//		// if a persons kills are equal to the kill limit, then the game ends
//		for(Player player: players.values()) {			
//			if(player.getKills() >= killLimit) {
//				System.err.println("	KILL LIMIT REACHED! ENDING GAME. WINNER: " + player.getId());
//				endMatch();
//				return true;
//			}
//		}
//		return false;
		return isOver;
	}
	
	/**
	 * Returns true if a client is in a match, false otherwise.
	 * 
	 * @param WebSocketSession
	 * @return boolean
	 */
	public boolean isPlayerInMatch(WebSocketSession player) {
		return playerList.contains(player);	
	}
	
	/**
	 * Adds a message to the broadcasting thread's queue to be sent to all connected players.
	 * 
	 * @param TextMessage
	 * @throws IOException
	 */
	public void addMessageToBroadcast(TextMessage message) throws IOException {
		bc.addMessage(message);
	}
	
	/**
	 * Returns the player's match id associated with the session.
	 * 
	 * @param WebSocketSession
	 * @return
	 */
	public Integer getPlayerMatchId(WebSocketSession player) {
		return players.get(player).getId();
	}
	
	/**
	 * Returns a given player associated with the session.
	 * 
	 * @param WebSocketSession
	 * @return Player
	 */
	public Player getPlayer(WebSocketSession player) {
		return players.get(player);
	}
	
	/**
	 * Finds a player by their Id and returns it.
	 * 
	 * @param playerId
	 * @return Player
	 */
	public Player getPlayerById(Integer playerId) {
		for(Player player: players.values()) {
			if(player.getId() == playerId) {
				return player;
			}
		}
		return null;	
	}
	
	
	/**
	 * Returns a list of players.
	 * 
	 * @return List<Player>
	 */
	public List<Player> getPlayers() {
		List<Player> playersList = new CopyOnWriteArrayList<>();
		for(Player player: players.values()) {			
			playersList.add(player);
		}
		return playersList;
	}
	
	/**
	 * Registers a hit on a player. Player is damaged, enemy gets damage delt.
	 * 
	 * @param playerId
	 * @param sourceId
	 * @param causedDeath
	 * @param dmg
	 */
	public void registerHit(Integer playerId, Integer sourceId, boolean causedDeath, Integer dmg) {
		Player player = getPlayerById(playerId);
		Player enemy = getPlayerById(sourceId);
		player.takeDmg(dmg);
		enemy.addDamageDealt(dmg);
		if(causedDeath) {
			registerKill(player, enemy);
		}
	}
	
	/**
	 * Registers the death of a player, and adds the kill to the enemy who did the destroying.
	 * Player is the one who dies, enemy get the kill point.
	 * 
	 * @param player
	 * @param enemy
	 */
	public void registerKill(Player player, Player enemy) {	
		enemy.addKill();
		player.addDeath();
	}

	/**
	 * 
	 * @param playerId
	 */
	public void respawn(Integer playerId) {
		Player p = getPlayerById(playerId);
		p.respawn();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMatchType() {
		return matchType;
	}

	/**
	 * 
	 * @param matchName
	 */
	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}
}




/* Lobby logic */

// private ConcurrentHashMap<WebSocketSession, Boolean> ready = new ConcurrentHashMap<>();
// private String matchName;


/* Join Match */
// ready.put(player, false);

/* StartMatch
// If not ready
if (!checkReady()) {
	return;
}


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
}
*/
