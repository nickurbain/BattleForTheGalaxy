package websocketclient;

import java.net.URI;

import org.json.JSONObject;

public class Main {
	public static void main(String args[]) throws Exception {
		
//		ws://echo.websocket.org
//		ws://localhost:8080/name
//		Client client = new Client(new URI("ws://localhost:8080/name"));
		Client client = new Client(new URI("ws://localhost:8080/bfg"));
		
		System.out.println("Connecting to server!");
		client.connectBlocking();
		
		System.out.println("URI: " + client.getURI());
		System.out.println("Is open: " + client.isOpen());
		
		JSONObject value = new JSONObject();
		value.put("jsonOrigin", "1");
		value.put("jsonType", "1");  // 0 means login
		value.put("id", "finn");
		value.put("pass", "bork");
		

		// Test prints
		System.out.println(value.get("jsonType") + " " + value.get("id") + " " + value.get("pass"));
		System.out.println(value.toString());
		
		
		client.send(value.toString());
		
		Thread.sleep(1000);
		
		int i = 0;
		while(i < 1000) {
			value.put("id", "finn " + i);
			client.send(value.toString());
			i++;
			Thread.sleep(100);
		}

		
		
		
		/*
		 * 		value.put("type", "login");
		*	value.put("Id", "finn");
		*	value.put("pass", "WINS");
		 */
		
		
	}
}



/*
//ws://echo.websocket.org
//ws://localhost:8080/name
//Client client = new Client(new URI("ws://localhost:8080/name"));
Client client = new Client(new URI("ws://localhost:8080/bfg"));

System.out.println("Connecting to server!");
client.connectBlocking();

System.out.println("URI: " + client.getURI());
System.out.println("Is open: " + client.isOpen());


JSONObject value = new JSONObject();
value.put("type", "login");
value.put("Id", "testUser");
value.put("pass", "testPass");

System.out.println(value.get("type") + " " + value.get("Id") + " " + value.get("pass"));
System.out.println(value.toString());


client.send("HEllO!");

//client.send(value.toString());
 * */
