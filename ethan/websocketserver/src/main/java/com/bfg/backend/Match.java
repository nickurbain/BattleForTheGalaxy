package com.bfg.backend;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.JsonObject;

/*
 * Needs it's own broadcasting thread for all of the match specific messages
 * 
 * clients are added to the players list
 * 
 */
public class Match {
	private List<WebSocketSession> players;				 	// List to track players
	private HashMap<WebSocketSession, Integer> playerIds; 	// Maps the match ID of a player to the player
	private HashMap<Integer, Integer> kills;			 	// Maps IDs to kills per match
	private HashMap<Integer, Integer> deaths;			  	// Maps IDs to deaths per match
	
	private BroadcastThread bc; // Broadcasting thread for sending messages to clients
	
	private Integer killLimit;	// Tracks the kill limit for a match. Defaults to 10
	private Integer id;			// Increments an id for users

	/* 
	 * Constructor, initializes values
	 */
	public Match() {
		players = new CopyOnWriteArrayList<>();
		bc = new BroadcastThread(1);
		playerIds = new HashMap<>();
		players = new CopyOnWriteArrayList<>();
		kills = new HashMap<>();
		deaths = new HashMap<>();
		
		// Set matchName
		killLimit = 10;
		id = 0;
		bc.start();
	}

	/*
	 * Adds a player to the match. Initializes their kills and deaths to 0
	 * and adds them to the broadcasting thread
	 */
	public void addPlayer(WebSocketSession player) {
		id++;
		players.add(player);
		playerIds.put(player, id);
		kills.put(id, 0);
		deaths.put(id, 0);
		
		bc.addClient(player);
		
		String welcomeMessage = "Welcome player " + playerIds.get(player);
		System.out.println("Player " + playerIds.get(player) + " joined match!");
		
		try {
			player.sendMessage(new TextMessage(id.toString()));
			player.sendMessage(new TextMessage(welcomeMessage)); // TODO: Testing statement
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error on sending id to client");
		}
		
		// TODO Broadcast <<playername>> has joined
	}
	
	/*
	 * Removes all traces of a player from the match
	 */
	public void removePlayer(WebSocketSession player) {
		kills.remove(playerIds.get(player));
		deaths.remove(playerIds.get(player));
		playerIds.remove(player);
		players.remove(player);
		bc.removeClient(player);
	}

	public void startMatch() {
		
	}

	/*
	 * Ends the match
	 */
	public void endMatch() {
		// Show match stats
		System.out.println("END MATCH HIT!!!!! Showing stats");
		System.out.println(getStats().toString());
		
		bc.end(); // Ends the thread
	}
	
	/*
	 * TODO: Make one large json object with all of the match stats in it. Then send that out to the clients.
	 */
	public JsonObject getStats() {
		JsonObject stats = new JsonObject();
		
		System.out.println("");
		System.out.println("getStats Method");
		
		int i;
		for(i = 0; i < players.size(); i++) {
			Integer id = playerIds.get(players.get(i));
			stats.addProperty("id", id);
			stats.addProperty("kills", kills.get(id));
			stats.addProperty("deaths", deaths.get(id));
			
			System.out.println("	Stats for player " + id);
			System.out.println("	Player " + id + " kills from getStats: " + kills.get(id));
			System.out.println("	Player " + id + " deaths from getStats: " + deaths.get(id));
			System.out.println("");
		}
		
		System.out.println("");
		
		return stats;
	}
	
	/*
	 * Checks if the match has ended
	 *  TODO: Need to test
	 */
	public boolean isEndMatch() {
		System.out.println("isEndMatch Method");
		// if a persons kills are equal to the kill limit, then the game ends
		for (Integer id: kills.keySet()) {
			
			System.out.println("	kills for player " + id + ": " + kills.get(id)); // TODO testing
			
			if(kills.get(id) >= killLimit) {
				System.err.println("KILL LIMIT REACHED! ENDING GAME. WINNER: " + id);
				return true;
			}
		}
		System.out.println("Match not over yet!!");
		System.out.println("");
		return false;
	}
	
	/*
	 * Registers the kills for a player
	 */
	public void registerKill(int player, int enemy) {
		System.out.println("registerKill Method");
		
		// Add the kills to the enemy
		kills.replace(enemy, kills.get(enemy), kills.get(enemy) + 1);
		
		// Add the death to the player
		deaths.replace(player, deaths.get(player), deaths.get(player) + 1);
		
		System.out.println("	Player deaths: " + deaths.get(player) + " | Enemy total kills: " + kills.get(player)); // TODO Testing statement
		System.out.println("");
		
		// Check if the match is over
		if(isEndMatch()) {
			endMatch();
		}
	}
	
	/*
	 * Checks if a client is in a match
	 */
	public boolean isClientInMatch(WebSocketSession client) {
		return players.contains(client);	
	}
	
	/*
	 * Adds a message to the broadcasting queue
	 */
	public void addMessageToBroadcast(TextMessage message) throws IOException {
		bc.addMessage(message);
	}
	
	public Integer getPlayerId(WebSocketSession player) {
		return playerIds.get(player);
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
