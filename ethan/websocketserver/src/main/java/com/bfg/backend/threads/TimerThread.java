package com.bfg.backend.threads;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.enums.ServerJsonType;
import com.google.gson.JsonObject;

public class TimerThread extends Thread {

	private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>(); // List of clients to send messages to
	private Thread thread; // The thread to run
	private boolean end; // Ends the thread

	public TimerThread() {
	}

	@Override
	public void run() {
		end = false;
		countdown();
	}

	private void countdown() {
		while (!end) {
			final Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				int i = 180; // In seconds

				public void run() {
					sendTime(i);
					if (i < 0) {
						timer.cancel();
						end = true;
					}
				}
			}, 0, 1000);
		}

		System.err.println("Ending timer thread");
	}
	
	public void sendTime(int seconds) {
		JsonObject json = new JsonObject();
		json.addProperty("jsonOrigin", 0);
		json.addProperty("JsonType", ServerJsonType.TIME.ordinal());
		json.addProperty("time", seconds);
		
		
		for (WebSocketSession webSocketSession : sessions) {
			try {
				webSocketSession.sendMessage(new TextMessage(json.toString()));
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("ERROR WITH TIMER THREAD");
			}
		}
	}

	/**
	 * Starts the broadcasting thread
	 */
	public void start() {
		System.out.println("Timer thread started!");
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * Adds a client to send messages to
	 * 
	 * @param session
	 *            to add client
	 */
	public void addClient(WebSocketSession session) {
		sessions.add(session);
	}

	/**
	 * Removes a client from the list of clients
	 * 
	 * @param session
	 *            to be removed
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
