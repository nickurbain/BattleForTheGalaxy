package data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.utils.JsonValue;

import battle.galaxy.BattleForTheGalaxy;

/*
 * DataController is the class that controls the input/output of data
 * to/from the Server/Game. It contains a listener to listen for input
 * from the server and when it is received it will change state to be
 */

public class DataController {
	
	//Final Vars
	private String TEST_URI = "ws://echo.websocket.org";
	private String BASE_URI = "ws://proj-309-vc-2.cs.iastate.edu:8080/bfg";
	
	boolean authorized = false;
	
	private BattleForTheGalaxy game;
	//Client endpoint for websocket
	private Client client;
	//ArrayList for storing raw data from server
	private List<String> rawData = new CopyOnWriteArrayList<String>();
	//Storage for parsed objects
	private ArrayList<Object> rxFromServer = new ArrayList<Object>();
	
	private URI uri;
	
	private int matchId;
	private boolean isOver;
	private String matchStats;
	
	
	/**
	 * Constructor which is passed the game, starts the listener, and sets state to false
	 */
	public DataController(BattleForTheGalaxy game) {
		this.game = game;
		setOver(false);
		setupWebSocket();
	}
	
	/**
	 * Connect WebSocket to the server
	 */
	public void setupWebSocket() {
		try {
			//uri = new URI(TEST_URI);
			uri = new URI(BASE_URI);
			client = new Client(uri, this);
			client.connectBlocking();
		} catch (URISyntaxException | InterruptedException e) {
			e.printStackTrace();
			System.out.println("Client-Server connection could not be made.");
		}
	}
	/**
	 * Parses raw data from server
	 */
	public void parseRawData() {
		for(String jsonString: rawData) {
			JsonValue base = game.jsonReader.parse((String)jsonString);
			JsonValue component = base.child;
			switch(component.asInt()) {
				case JsonHeader.ORIGIN_SERVER:
					parseOriginServer(component.next().asInt(), (String) jsonString);
					break;
				case JsonHeader.ORIGIN_CLIENT:
					parseOriginClient(component.next().asInt(), (String) jsonString);
//					System.out.println("DC.parseRawData RX: " + jsonString); // for debugging
					break;
				default:
					System.out.println("DataController.parseRawData - ERROR: incoming JSON Origin not Server or Client:\n\t" + jsonString);
					break;
			}
		}
	}
	
	private void parseOriginServer(int jsonType, String jsonString) {
		JsonValue base = game.jsonReader.parse((String)jsonString);
		JsonValue component = base.child;
		switch(jsonType) {
		case JsonHeader.TYPE_AUTH:
			component = component.next();
			component = component.next();
			if(component.asString().equals("Validated")) {
				authorized = true;
			}else {
				authorized = false;
			}
			break;
		case JsonHeader.TYPE_MATCH_NEW:
			component = component.next();
			component = component.next();
			matchId = component.asInt();
			rawData.remove(jsonString);
			break;
		case JsonHeader.TYPE_MATCH_END:
			setOver(true);
			rawData.remove(jsonString);
			break;
		case JsonHeader.TYPE_MATCH_STATS:
			matchStats = jsonString;
			rawData.remove(jsonString);
			break;
		}
	}

	/**
	 * Parse data from a client
	 * @param jsonType
	 * @param jsonString
	 */
	private void parseOriginClient(int jsonType, String jsonString) {
		switch(jsonType) {
			case JsonHeader.TYPE_LOGIN:
//				System.out.println(jsonString);
				break;
			case JsonHeader.TYPE_PLAYER:
				PlayerData playD = game.json.fromJson(PlayerData.class, jsonString);
				rawData.remove(jsonString);
				if(playD.getId() != matchId) {
					rxFromServer.add(playD);
				}
				break;
			case JsonHeader.TYPE_PROJECTILE:
				ProjectileData projD = game.json.fromJson(ProjectileData.class, jsonString); 
				//projD.adjustPositionForTest(); // for testing with the echo server (adds 150 to y)
				rawData.remove(jsonString);				
				if(projD.getSource() != matchId) {
					rxFromServer.add(projD);
				}
				break;
			case JsonHeader.TYPE_HIT:
				HitData hitData = game.json.fromJson(HitData.class, jsonString);
				rawData.remove(jsonString);
				rxFromServer.add(hitData);
				break;
			case JsonHeader.TYPE_JOINMATCH:
				System.out.println("DC.parseOriginClient: received a Client|JoinMatch Json");
				rawData.remove(jsonString);
				break;
			case JsonHeader.TYPE_REGISTRATION:
				//TODO
				break;
		}
	}
	
	/**
	 * Parse ship data from the server database
	 * @return Ship the ship containing data from the database
	 */
	private Ship parseShip(){
		Ship ship = new Ship();
		for(String jsonString: rawData) {
			JsonValue base = game.jsonReader.parse((String)jsonString);
			JsonValue component = base.child;
			JsonValue componentNext = component.next();
			if(component.asInt() == JsonHeader.ORIGIN_SERVER && componentNext.asInt() == JsonHeader.TYPE_DB_SHIP) {
				ship = game.json.fromJson(Ship.class, jsonString);
				rawData.remove(jsonString);
			}
		}
		
		return ship;
	}
	
	/**
	 * Sends Player data from the game to the server
	 */
	public void updateServerPlayerData(PlayerData playerData) {
		String player = game.getJson().toJson(playerData);
		client.send(player);
	}
	
	/**
	 * Sends new Projectile data from the game to the server
	 */
	public void updateServerProjectileData(ProjectileData projectileData) {
		String projectile = game.getJson().toJson(projectileData);
		client.send(projectile);
		// New projectile JSON example below:
		// {jsonOrigin:1,jsonType:3,id:93348661,position:{x:20380.682,y:12755.344},direction:{x:-1029.6886,y:1090.7526},rotation:43.350464,lifeTime:2,source:1517219043,damage:30}
	}
	
	/**
	 * Sends Hit data from the game to the server
	 **/
	public void updateServerHit(int sourceId, int playerId, int damage, boolean causedDeath) {
		HitData hitData = new HitData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_HIT, sourceId, playerId, damage, causedDeath);
		String hit = game.getJson().toJson(hitData);
		hit = hit.substring(0, hit.length() - 1);
		hit += ",causedDeath:" + causedDeath + "}";
		System.out.println(hit);
		client.send(hit);
	}
	
	/**
	 * Used for logging a user into the server, called from SplashScreen
	 */
	public boolean login(String user, String pass) {
		LoginData login = new LoginData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_LOGIN, user, pass);
		if(client.isOpen()) {
			
			
			// HARD CODED TO JOIN A MATCH WHEN LOGIN IS CALLED
			//client.send("{jsonOrigin:1,jsonType:12}");
			//System.out.println("DC.login TX: sent a Client|JoinMatch Json");
			
			
			// LOGIN IS SUPPOSED TO BE CALLED AT THE SPLASHSCREEN BUT THIS IS FOR DEBUGGING THE SERVER MATCHES
			client.send(game.json.toJson(login));

			try {
				Thread.sleep(2000);
				parseRawData();
				if(authorized) {
					return true;
				}else {
					return false;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	public boolean registration(String user, String pass) {
		RegistrationData register = new RegistrationData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_REGISTRATION, user, pass);
		if(client.isOpen()) {	
			
			client.send(game.json.toJson(register));
			
			try {
				Thread.sleep(2000);
				parseRawData();
				if(authorized) {
					return true;
				}else {
					return false;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Send request to server to join a match. Server should respond with matchId
	 */
	public void joinMatch() {
		client.send("{jsonOrigin:1,jsonType:12}");
		try {
			Thread.sleep(1000);
			parseRawData();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads data from the server. Called from Client.
	 * 
	 * @param data raw data from the server
	 */
	public void newData(String data) {
		rawData.add(data);
	}
	
	/**
	 * Gets the parsed objects ArrayList
	 * @return
	 */
	public ArrayList<Object> getRxFromServer(){
		return rxFromServer;
	}
	
	/**
	 * Closes the WebSocket when we are done
	 */
	public void close() {
		client.close();
	}

	public int getMatchId() {
		return matchId;
	}

	/**
	 * Gets the ship data stored on the database for use in customization or in game.
	 * @param id of the client making the request
	 * @return Ship object containing client ship data
	 */
	public Ship getShipFromDB(int id) {
		String shipReq = "{jsonOrigin:1,jsonType:2,id:" + id + "}";
		client.send(shipReq);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return parseShip();
	}
	
	/**
	 * Send the ship data for storage in the database.
	 * @param id the id of the client
	 */
	public void sendShipToDB(int id, Ship ship) {
		String json = game.json.toJson(ship);
		client.send(json);
	}
	
	/**
	 * Gets the locally saved ship data
	 * @return Ship ship with locally saved data
	 */
	public Ship getShipLocal() {
		Ship ship = new Ship();
		String content = "";
	    try{
	        content = new String (Files.readAllBytes(Paths.get("core/assets/ship.txt")));
	    } catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	    ship = game.json.fromJson(Ship.class, content);
		return ship;
	}
	
	/**
	 * Saves ship data locally
	 * @param ship
	 */
	public void saveShipLocal(Ship ship) {
		PrintWriter out = null;
		try {
			out = new PrintWriter("core/assets/ship.txt");
			out.write(game.json.toJson(ship));
			System.out.println("SAVED: " + game.json.toJson(ship));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * 
	 * @return matchStats the string containing match stats json
	 */
	public String getMatchStatsFromServer() {
		try {
			Thread.sleep(2000);
			parseRawData();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return matchStats;
		
	}
	/**
	 * @return the isOver
	 */
	public boolean isOver() {
		return isOver;
	}

	/**
	 * @param isOver the isOver to set
	 */
	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}
	
	/**
	 * Send a generic string to the server in the form of a json
	 * @param jsonString
	 */
	public void sendGeneric(String jsonString) {
		client.send(jsonString);
	}

}
