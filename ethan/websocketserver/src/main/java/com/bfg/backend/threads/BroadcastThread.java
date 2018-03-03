package com.bfg.backend.threads;

import org.springframework.web.socket.WebSocketSession;

public class BroadcastThread extends Thread {

	private Thread t;
	
	BroadcastThread() {}

	@Override
	public void run() {
		System.out.println("New Thread Started!");
		
		for(int i = 0; i < 5; i++) {
			System.out.println("Broadcast Thread started and running! " + i);
		}
	}
	
	
	public void start() {
		System.out.println("Starting broadcasting thread");
		if(t == null) {
			t = new Thread(this);
			t.start();
		}
	}
	
}
