package websocketclient;

import java.net.URI;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class Main {
	
	static Client client;
	
	public static void main(String args[]) throws Exception {
		
//		ws://echo.websocket.org
//		ws://localhost:8080/name
//		Client client = new Client(new URI("ws://localhost:8080/name"));
		client = new Client(new URI("ws://localhost:8080/bfg"));
		
		System.out.println("Connecting to server!");
		client.connectBlocking();
		System.out.println("URI: " + client.getURI() + "  ||  " + "Is open: " + client.isOpen());
		
		JSONObject value = new JSONObject();
		value.put("jsonOrigin", "1");
		value.put("jsonType", "1");  // 0 means login
		value.put("id", "finn");
		value.put("pass", "bork");

		// Test prints
		testPrints(value);
	
		// Initial send
		client.send(value.toString());
		
		int response = 0;
		boolean quit = false;
		Scanner scanner = new Scanner(System.in);
		
		while(!quit) {
			menu();

			if(scanner.hasNextInt()) {
				response = scanner.nextInt();
			}
			
			switch (response) {
			
				case (1): // Login
					break;
				case (2): // Add kills
					addKills(scanner);
					break;
				case (3): // Add Deaths
					addDeaths();
					break;
				case (4): // Remove self from match
					removeFromMatch();
					break;
				case (5): // Add self to match
					addToMatch();
					break;
				case (6): // Send batch messages to server
					batchMessage();
					break;
				case (7): // Quit
					quit = true;
					break;
				default:
					System.err.println("ERROR! response init to 0");
					break;
			}
		}
		scanner.close();
	}
	
	public static void addKills(Scanner scanner) throws JSONException {
		System.out.println("Add Kills");
		System.out.println("How many kills do you want to add?");
		
		int numKills = 0;
		
		if(scanner.hasNextInt()) {
			numKills = scanner.nextInt();
		}
		
		JSONObject value = new JSONObject();
		value.put("jsonOrigin", "1"); // From Client
		value.put("jsonType", "1");   // 0 means login
		value.put("kills", numKills);
		
		
		client.send(value.toString());
		
	}
	
	public static void addDeaths() {
		
	}
	
	public static void addToMatch() {
		
	}
	
	public static void removeFromMatch () {
		
	}
	
	public static void batchMessage() {
		
	}
	
	public static void menu() {
		System.out.println("Menu:");
		System.out.println("1: login");
		System.out.println("2: Add kills");
		System.out.println("3: Add Deaths");
		System.out.println("4: Remove self from match");
		System.out.println("5: Add self to match again? -- Need to test this");
		System.out.println("6: Send batch messages to server");
		System.out.println("7: Quit");
	}
	
	public static void testPrints(JSONObject value) throws JSONException {
		System.out.println("Sending message to server:");
		System.out.println(value.get("jsonType") + " " + value.get("id") + " " + value.get("pass"));
		System.out.println("");
		System.out.println("Recieved from Server:");
		System.out.println(value.toString());
	}
}