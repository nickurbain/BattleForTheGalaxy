package com.bfg.backend;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/*
 * Needs it's own broadcasting thread for all of the match specific messages
 * 
 * clients are added to the players list
 * 
 */
public class Match {
	private List<WebSocketSession> players;
	private HashMap<WebSocketSession, Integer> playerIds;
	private HashMap<Integer, Integer> kills;
	private HashMap<Integer, Integer> deaths;
	
	private BroadcastThread bc;
	
	private Integer killLimit;
	private Integer id;	// Increments an id for users

	public Match() {
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

	public void joinMatch(WebSocketSession player) {
		id++;
		players.add(player);
		playerIds.put(player, id);
		kills.put(id, 0);
		deaths.put(id, 0);
		
		bc.addClient(player);
		
		try {
			player.sendMessage(new TextMessage(id.toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error on sending id to client");
		}
		
		// TODO Broadcast <<playername>> has joined
	}

	public void startMatch() {
		
	}

	public void endMatch() {
		// Show match stats
		bc.end(); // Ends the thread
	}
	
	public boolean isEndMatch() {
		// if a persons kills are equal to the kill limit, then the game ends
		for (Integer id: kills.keySet()) {
			System.out.println("key id : " + kills.get(id)); // TODO testing
			if(kills.get(id) >= killLimit) {
				System.err.println("KILL LIMIT REACHED! ENDING GAME. WINNER: " + id);
				return true;
			}
		}
		
		return false;
	}
	
	public void registerKill(int player, int enemy) {
		// Add the kills to the enemy
		kills.replace(enemy, kills.get(enemy), kills.get(enemy) + 1);
		
		// Add the death to the player
		deaths.replace(player, deaths.get(player), deaths.get(player) + 1);
		
		// Check if the match is over
		if(isEndMatch()) {
			endMatch();
		}
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
*/



/*
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
