package com.bfg.backend.threads;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Broadcasting Thread for sending messages to clients in list.
 * Adds messages to a queue to be sent out.
 * 
 * @author emball, jln
 *
 */
public class BroadcastThread extends Thread {

	private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();							// List of clients to send messages to
	private ConcurrentLinkedQueue<TextMessage> messages = new ConcurrentLinkedQueue<TextMessage>();	// Message queue to send to clients
	private Thread t;		// The thread to run
	private boolean end;	// Ends the thread
	private int id;			// To track how many threads we have going. Only really used when thread is spawned and prints out message to console.
	
	public BroadcastThread(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		end = false;
		broadcast();
	}
	
	
	/**
	 * Picks top message off the queue and sends it to all connected clients
	 */
	private void broadcast() {
		while(!end) {
			if(!messages.isEmpty() && messages.peek() != null) {
				TextMessage message = messages.poll();
				System.out.println("BC: " + message.getPayload());
				for (WebSocketSession webSocketSession : sessions) {
					try {
						webSocketSession.sendMessage(message);
					} catch (IOException e) {
						e.printStackTrace();
						System.err.println("ERROR WITH RUN -- BROADCAST ID: " + id);
					}
				}
			}
		}
		System.err.println("Ending broadcasting thread");
	}
	
	/**
	 * Starts the broadcasting thread
	 */
	public void start() {
		System.out.println("^&^&^^&^&^&^&^&^&^&^&^&^&^Starting broadcasting thread " + id);
		if(t == null) {
			t = new Thread(this);
			t.start();
		}
	}
	
	/**
	 * Adds a message to the queue to be broadcast
	 * 
	 * @param message to be broadcast
	 */
	public void addMessage(TextMessage message) {
		messages.add(message);
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

