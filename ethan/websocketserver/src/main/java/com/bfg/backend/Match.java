package com.bfg.backend;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
	private ConcurrentHashMap<WebSocketSession, Boolean> ready = new ConcurrentHashMap<>();
	private int killLimit;
	private String matchName;
	private int id;

	
	public void initMatch() {
		// Set matchName
		
		// Set killLimit
		
		id = 0;
		b.start();
	}
	
	public void joinMatch(WebSocketSession player) {
		players.add(player);
		ready.put(player, false);
		
		// TODO Send player the Player Id;
		// send(id++);
		
		// TODO Broadcast <<playername>> has joined
	}
	
	public void readyUp(WebSocketSession player) {
		ready.putIfAbsent(player, true);
	}
	
	public boolean checkReady() {
		// TODO For each entry, check if it is false, if it is, return false -- ready
//			if(ready
		return true;
	}
	
	public void startMatch() {
		// If not ready
		if(!checkReady()) {
			return;
		}
		// TODO
	}
	
	public void endMatch() {
		b.end(); // Ends the thead
		
	}
}
