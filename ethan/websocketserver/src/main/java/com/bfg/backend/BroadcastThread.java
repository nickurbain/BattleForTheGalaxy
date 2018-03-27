package com.bfg.backend;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class BroadcastThread extends Thread {

	private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	
	private ConcurrentLinkedQueue<TextMessage> messages = new ConcurrentLinkedQueue<TextMessage>();
	
	private Thread t;
	
	private boolean end;
	
	private int id;
	
	public BroadcastThread(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		end = false;
		broadcast();
	}
	
	
	/*
	 * Sends out messages to all connected clients
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
	
	public void start() {
		System.out.println("^&^&^^&^&^&^&^&^&^&^&^&^&^Starting broadcasting thread " + id);
		if(t == null) {
			t = new Thread(this);
			t.start();
		}
	}
	
	public void addMessage(TextMessage message) {
		messages.add(message);
	}
	
	public void addClient(WebSocketSession session) {
		sessions.add(session);
	}
	
	public void removeClient(WebSocketSession session) {
		sessions.remove(session);
	}
	
	public void end() {
		end = true;
	}
}

