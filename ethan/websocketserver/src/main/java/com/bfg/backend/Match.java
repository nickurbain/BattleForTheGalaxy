package com.bfg.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.enums.ServerJsonType;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/*
 * Needs it's own broadcasting thread for all of the match specific messages
 * 
 * clients are added to the players list
 * 
 */
public class Match {
	private List<WebSocketSession> playerList;				 			// List to track players
	private ConcurrentHashMap<WebSocketSession, Player> players;
	
	private BroadcastThread bc; // Broadcasting thread for sending messages to clients
	
	private Integer killLimit;	// Tracks the kill limit for a match. Defaults to 10
	private Integer idIncrementer;			// Increments an id for users
	private boolean isOver;		// Tracks if the match is over or not

	/* 
	 * Constructor, initializes everything
	 */
	public Match() {
		playerList = new CopyOnWriteArrayList<>();
		bc = new BroadcastThread(1);
		killLimit = 10;
		idIncrementer = 0;
		isOver = false;
		bc.start();
		players = new ConcurrentHashMap<>();
	}

	/*
	 * Adds a player to the match. Initializes their kills and deaths to 0
	 * and adds them to the broadcasting thread
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
		System.out.println("	json sent to player: " + welcomeMessage);
		System.out.println("");
		
		try {
			player.sendMessage(new TextMessage(welcomeMessage));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error on sending id to client");
		}
	}
	
	/*
	 * Removes all traces of a player from the match
	 */
	public void removePlayer(WebSocketSession player) {		
		System.out.println("Player " + getPlayer(player).getId() + " left match!");
		
		players.remove(player);
		playerList.remove(player);
		bc.removeClient(player);
	}
	
	/*
	 * Returns a boolean representing if the match is over
	 */
	public boolean isMatchOver() {
		return isOver;
	}

	/*
	 * Ends the match
	 */
	public void endMatch() {
		// Show match stats
		System.out.println("END MATCH HIT!!!!! Showing stats");
		System.out.println(getStats().toString());
		
		JsonObject over = new JsonObject();
		over.addProperty("jsonOrigin", 0);
		over.addProperty("jsonType", ServerJsonType.GAME_OVER.ordinal());
		TextMessage send = new TextMessage(over.toString());
		bc.addMessage(send);
		
		isOver = true;
		bc.end(); // Ends the thread
	}
	
	
//	JsonObject header = new JsonObject();
//	header.addProperty("jsonOrigin", 0);
//	header.addProperty("jsonType", ServerJsonType.MATCH_STATS.ordinal()); // Match Stats

	/*
	 * Make one large json object with all of the match stats in it. Then send that out to the clients.
	 */
	public String getStats() {
		System.out.println("getStats Method");
		
		JsonArray arr = new JsonArray();
		Gson gson = new Gson();
	
		int i;
		for(i = 0; i < playerList.size(); i++) {
		
			JsonObject stats = new JsonObject();
			Player p = players.get(playerList.get(i));
			
			stats.addProperty("playerId", p.getId());
			stats.addProperty("kills", p.getKills());
			stats.addProperty("deaths", p.getDeaths());
			stats.addProperty("damageDealt", p.getDamageDealt());
//			stats.addProperty("hitPoints", p.getHP());
			
			
			arr.add(gson.toJsonTree(p));
 
			
			System.out.println("  Player " + p.getId() + " stats");
			System.out.println("	Player " + p.getId() + " kills : " + p.getKills());
			System.out.println("	Player " + p.getId() + " deaths: " + p.getDeaths());
			System.out.println("	Player " + p.getId() + " hitPoints: " + p.getHP());
			System.out.println("	Player " + p.getId() + " damageDealt: " + p.getDamageDealt());
			System.out.println("");
		}
		System.out.println("");
		
		JsonContainer json = new JsonContainer();
		json.setJsonType(ServerJsonType.NEW_MATCH.ordinal());
		json.setMatchStats(arr.toString());
		
		String str = gson.toJson(json);
		
		System.out.println("test 1: " + gson.toJson(json));
		
		return str;
	}
	
	/*
	 * Checks if the match has ended
	 */
	public boolean checkEndMatch() {
		// if a persons kills are equal to the kill limit, then the game ends
		for(Player player: players.values()) {
			
			System.out.println("	kills for player " + player.getId() + ": " + player.getKills()); // TODO testing
			
			if(player.getKills() >= killLimit) {
				System.err.println("	KILL LIMIT REACHED! ENDING GAME. WINNER: " + player.getId());
				endMatch();
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Checks if a client is in a match
	 */
	public boolean isPlayerInMatch(WebSocketSession player) {
		return playerList.contains(player);	
	}
	
	/*
	 * Adds a message to the broadcasting queue
	 */
	public void addMessageToBroadcast(TextMessage message) throws IOException {
		bc.addMessage(message);
	}
	
	/*
	 * Returns the player's match id
	 */
	public Integer getPlayerMatchId(WebSocketSession player) {
		players.get(player).getId();
		System.out.println("GetPlayerMatchId: " + players.get(player).getId());
		return players.get(player).getId();
	}
	
	/*
	 * Returns a given player associated with the session
	 */
	public Player getPlayer(WebSocketSession player) {
		return players.get(player);
	}
	
	/*
	 * Finds a player by their Id and returns it
	 */
	public Player getPlayerById(Integer playerId) {
		for(Player player: players.values()) {
			if(player.getId() == playerId) {
				return player;
			}
		}
		return null;	
	}
	
	/*
	 * Registers a hit on a player. Takes a player's ID and damage as args 
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
	
	/*
	 * Registers the kills for a player
	 */
	public void registerKill(Player player, Player enemy) {	
		enemy.addKill();
		player.addDeath();
		
		System.out.println("registerKill Method");
		System.out.println("	Player deaths: " + player.getDeaths() + " | Enemy total kills: " + enemy.getKills()); // Testing statement
		System.out.println("");
		
		// Check if the match is over
		if(checkEndMatch()) {
			endMatch();
		}
	}

	public void respawn(Integer playerId) {
		Player p = getPlayerById(playerId);
		p.respawn();
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
