package websocketclient;

import java.net.URI;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class Main {
	
	static Client client;
	
	enum jsonType {
		LOGIN,
		SHIP_DATA,
		LOCATION,
		PROJECTILE,
		HIT,
		DEATH,
		RESPAWN,
		QUIT,
		DB_FRIEND,
		DB_STATS,
		DB_SHIP,
		MATCH_STATS,
		JOIN_MATCH,
		REGISTRATION
	};
	
	public static void main(String args[]) throws Exception {
		
//		ws://echo.websocket.org
//		ws://localhost:8080/name
//		Client client = new Client(new URI("ws://localhost:8080/name"));
		client = new Client(new URI("ws://localhost:8080/bfg"));
		
		System.out.println("Connecting to server!");
		client.connectBlocking();
		System.out.println("URI: " + client.getURI() + "  ||  " + "Is open: " + client.isOpen());
		
		JSONObject value = new JSONObject();
		value.put("jsonOrigin", 1); // From Client
		value.put("jsonType", "2");
		value.put("Client", "Connected!");
		client.send(value.toString()); 		// Initial send
		// Test prints
//		testPrints(value);
	
		Thread.sleep(200);
		
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
					login(scanner);
					break;
				case (2): // Add kills
					addKills();
					break;
				case (3): 
					System.out.println("Nothing. Implement to whatever you want");
					break;
				case (4): // Remove self from match
					removeFromMatch();
					break;
				case (5): // Add self to match
					joinMatch();
					break;
				case (6): // Send batch messages to server
					batchMessage(scanner);
					break;
				case (7): // Get match stats
					getMatchStats();
					break;
				case (8): // Get hit
					getHit();
					break;
				case (9):
					regUser(scanner);
					break;
				case (10): // Quit
					quit();
					break;
				
				default:
					System.err.println("ERROR! response init to 0");
					break;
			}
		}
		scanner.close();
	}
	
	public static void menu() {
		int i = 1;
		System.out.println("");
		System.out.println("Menu:");
		System.out.println(i++ + ": login");
		System.out.println(i++ + ": Add kills");
		System.out.println(i++ + ": IMPLEMENT ME");
		System.out.println(i++ + ": Remove self from match");
		System.out.println(i++ + ": Join match");
		System.out.println(i++ + ": Send batch messages to server");
		System.out.println(i++ + ": Get match stats");
		System.out.println(i++ + ": Get hit");
		System.out.println(i++ + ": Register");
		System.out.println(i++ + ": Quit");
	}
	
	
	/*
	 * Tests logging in
	 */
	public static void login(Scanner scanner) throws JSONException {
		String username = null, password = null;
		JSONObject value = new JSONObject();
		
		System.out.println("Username: ");
		if(scanner.hasNext()) {
			username = scanner.next();
		}
		System.out.println("Password: ");
		if(scanner.hasNext()) {
			password = scanner.next();
		}
		
		value.put("jsonOrigin", 1); // From Client
		value.put("jsonType", jsonType.LOGIN.ordinal());
		value.put("id", username);
		value.put("pass", password);
		client.send(value.toString());
	}
	
	/*
	 * Tests adding kills to a player
	 * Adds 1 kill and 1 death to the same player for now
	 */
	public static void addKills() throws JSONException {
//		System.out.println("Add Kill/Death (To same player)");
		
		JSONObject value = new JSONObject();
		value.put("jsonOrigin", 1); // From Client
		value.put("jsonType", jsonType.HIT.ordinal());
		value.put("sourceId", client.getMatchId());
		value.put("playerId", client.getMatchId());
		value.put("causedDeath", true);
		
		client.send(value.toString());
	}
	
	public static void getHit() throws JSONException {
		System.out.println("WE BE HIT CAP'N!");
		System.out.println("Enter damage: ");
		Scanner scanner = new Scanner(System.in);
		Integer dmg = 0;
		if(scanner.hasNextInt()) {
			dmg = scanner.nextInt();
		}
		
		JSONObject value = new JSONObject();
		value.put("jsonOrigin", 1); // From Client
		value.put("jsonType", jsonType.HIT.ordinal());
		value.put("sourceId", client.getMatchId());
		value.put("playerId", client.getMatchId());
		value.put("causedDeath", false);
		
		client.send(value.toString());
	}
	
	
	public static void joinMatch() throws JSONException {
		JSONObject value = new JSONObject();
		value.put("jsonOrigin", 1); // From Client
		value.put("jsonType", jsonType.JOIN_MATCH.ordinal());
		
		client.send(value.toString());
	}
	
	public static void removeFromMatch () throws JSONException {
		JSONObject value = new JSONObject();
		value.put("jsonOrigin", 1); // From Client
		value.put("jsonType", jsonType.QUIT.ordinal());
		
		client.send(value.toString());
	}
	
	/*
	 * Sends a batch of messages to the server to test the broadcasting
	 */
	public static void batchMessage(Scanner scanner) throws JSONException {
		System.out.println("Batch Message");
		System.out.println("Enter batch size");
	
		int batchsize = 0;
		if(scanner.hasNextInt()) {
			batchsize = scanner.nextInt();
		}
		
		int i;
		for(i = 0; i < batchsize; i++) {
			JSONObject value = new JSONObject();
			value.put("jsonOrigin", 1); // From Client
			value.put("jsonType", i + 100);
			value.put("batch", i);
			value.put("id", client.getMatchId());
			
			client.send(value.toString());
		}
		return;
	}
	
	/*
	 * Gets the match stats for a current match
	 */
	public static void getMatchStats() throws JSONException {
		JSONObject value = new JSONObject();
		value.put("jsonOrigin", 1); // From Client
		value.put("jsonType", jsonType.MATCH_STATS.ordinal());
		
		client.send(value.toString());
	}
	
	public static void quit() throws JSONException {
		JSONObject value = new JSONObject();
		value.put("jsonOrigin", 1); // From Client
		value.put("jsonType", jsonType.QUIT.ordinal()); 
		
		client.send(value.toString());
	}
	
	
	public static void testPrints(JSONObject value) throws JSONException {
		System.out.println("Sending message to server:");
		System.out.println(value.get("jsonType") + " " + value.get("id") + " " + value.get("pass"));
		System.out.println("");
	}
	
	public static void regUser(Scanner scanner) throws JSONException {
		String username = null, password = null;
		JSONObject value = new JSONObject();
		
		System.out.println("Username: ");
		if(scanner.hasNext()) {
			username = scanner.next();
		}
		System.out.println("Password: ");
		if(scanner.hasNext()) {
			password = scanner.next();
		}
		
		value.put("jsonOrigin", 1); // From Client
		value.put("jsonType", jsonType.REGISTRATION.ordinal());
		value.put("id", username);
		value.put("pass", password);
		client.send(value.toString());
	}
}