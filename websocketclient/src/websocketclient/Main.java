package websocketclient;

import java.net.URI;

public class Main {
	public static void main(String args[]) throws Exception {
		
//		ws://echo.websocket.org
//		ws://localhost:8080/name
//		Client client = new Client(new URI("ws://localhost:8080/name"));
		Client client = new Client(new URI("ws://localhost:8080/name"));
		
		System.out.println("Connecting to server!");
		client.connectBlocking();
		
		System.out.println("URI: " + client.getURI());
		System.out.println("Is open: " + client.isOpen());
		
		client.send("HELLO");
		
		
		
	}
}
