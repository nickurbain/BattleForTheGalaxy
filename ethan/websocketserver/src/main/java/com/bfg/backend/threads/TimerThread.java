package com.bfg.backend.threads;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;


import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class TimerThread extends Thread {

	private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();							// List of clients to send messages to
	private Thread thread;		// The thread to run
	private boolean end;	// Ends the thread
	
	public TimerThread() {
	}

	@Override
	public void run() {
		end = false;
		countdown();
	}
	
	
	
	private void countdown() {
		while(!end) {
			
		}
		
		System.err.println("Ending timer thread");
	}
	
	/**
	 * Starts the broadcasting thread
	 */
	public void start() {
		System.out.println("Timer thread started!");
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	
	/**
	 * Adds a client to send messages to
	 * 
	 * @param session to add client
	 */
	public void addClient(WebSocketSession session) {
		sessions.add(session);
	}
	
	/**
	 * Removes a client from the list of clients
	 * 
	 * @param session to be removed
	 */
	public void removeClient(WebSocketSession session) {
		sessions.remove(session);
	}
	
	/**
	 * Sets the end bool to true, this will end the thread.
	 */
	public void end() {
		end = true;
	}
	
}
