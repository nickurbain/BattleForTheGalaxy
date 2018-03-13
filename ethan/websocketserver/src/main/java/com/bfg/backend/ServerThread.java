package com.bfg.backend;

import org.springframework.web.socket.WebSocketSession;

public class ServerThread extends Thread {
	
	private Thread t;
	private WebSocketSession session;
	
	public ServerThread(WebSocketSession session) {
		this.session = session;
	}

	
	public WebSocketSession getThreadSession() {
		return session;
	}

	@Override
	public void run() {
		System.out.println("New Thread Started!");
		
		for(int i = 0; i < 5; i++) {
			System.out.println("$$$$$$$$ Thread " + session.getId() + " Loop " + i);
		}
	}
	
	
	public void start() {
		System.out.println("Starting Thread: " + session.getId());
		if(t == null) {
			t = new Thread(this, session.getId());
			t.start();
		}
	}
	
}
