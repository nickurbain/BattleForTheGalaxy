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
	private List<WebSocketSession> players = new CopyOnWriteArrayList<>();
	private BroadcastThread b = new BroadcastThread();
	
//	private ConcurrentHashMap<WebSocketSession, Boolean> ready = new ConcurrentHashMap<>();
	
	private HashMap<Integer, Integer> kills = new HashMap<>();
	private HashMap<Integer, Integer> deaths = new HashMap<>();
	
	private Integer killLimit;
	private Integer id;	// Increments an id for users
	
//	private String matchName;

	public void initMatch() {
		// Set matchName
		killLimit = 10;
		id = 0;
		b.start();
	}

	public void joinMatch(WebSocketSession player) {
		players.add(player);
//		ready.put(player, false);
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
		/*
		// If not ready
		if (!checkReady()) {
			return;
		}
		*/
		
		// TODO
		// Send out start signal
		initMatch();
	}

	public void endMatch() {
		b.end(); // Ends the thread
	}
	
	public boolean isEndMatch() {
		
		return true;
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


/*
public void readyUp(WebSocketSession player) {
	ready.putIfAbsent(player, true);
}

public boolean checkReady() {
	// TODO For each entry, check if it is false, if it is, return false
	for (WebSocketSession key : ready.keySet()) {
		
		// TODO: testing
		System.out.println("key id : " + ready.get(key.getId()));
		
		// If the player readiness is FALSE
		if(!ready.get(key)) {
			return false;
		}
	}
	return true;
}
*/
