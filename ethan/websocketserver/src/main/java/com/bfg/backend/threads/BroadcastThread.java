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

/**
 * Broadcasting Thread for sending messages to clients in list. Adds messages to
 * a queue to be sent out.
 * 
 * @author emball, jln
 *
 */
public class BroadcastThread extends Thread {

	private List<WebSocketSession> sessions; // List of clients to send messages to
	private ConcurrentLinkedQueue<TextMessage> messages; // Message queue to send to clients
	private Thread broadcast; // The thread to run
	private boolean end; // Ends the thread
	private int id; // To track how many threads we have going. Only really used when thread is
					// spawned and prints out message to console.
	private Thread timer;
	private boolean time;

	public BroadcastThread(int id) {
		this.id = id;
		time = false;
		sessions = new CopyOnWriteArrayList<>();
		messages = new ConcurrentLinkedQueue<TextMessage>();
	}

	/**
	 * Starts the broadcasting thread
	 */
	public void start() {
		System.out.println("^&^&^^&^&^&^&^&^&^&^&^&^&^Starting broadcasting & timer threads " + id);
		if (broadcast == null) {
			broadcast = new Thread();
			broadcast.start();
		}

		if (timer == null) {
			timer = new Thread();
			timer.start();
		}
	}

	@Override
	public void run() {
		end = false;
		if (time == false) {
			broadcast();
			time = true;
		} else {
			countdown();
		}
	}

	/**
	 * Picks top message off the queue and sends it to all connected clients
	 */
	private void broadcast() {
		while (!end) {
			if (!messages.isEmpty() && messages.peek() != null) {
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

	private void countdown() {
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int i = 180; // In seconds

			public void run() {
				i--;
				System.out.println("Time " + i);
				sendTime(i);
				if (i < 0 || end) {
					timer.cancel();
					end = true;
				}
			}
		}, 0, 1000);

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
	 * Adds a message to the queue to be broadcast
	 * 
	 * @param message
	 *            to be broadcast
	 */
	public void addMessage(TextMessage message) {
		messages.add(message);
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
