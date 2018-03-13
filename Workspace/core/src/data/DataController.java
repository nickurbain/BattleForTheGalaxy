package data;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
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
	
	private BattleForTheGalaxy game;
	//Client endpoint for websocket
	private Client client;
	//ArrayList for storing raw data from server
	private List<String> rawData = new CopyOnWriteArrayList<String>();
	//Storage for parsed objects
	private ArrayList<Object> rxFromServer = new ArrayList<Object>();
	
	URI uri;
	
	
	/**
	 * Constructor which is passed the game, starts the listener, and sets state to false
	 */
	public DataController(BattleForTheGalaxy game) {
		this.game = game;
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
			if(jsonString.equals("DENIED SUCKA") || jsonString.equals("Validated") || jsonString.equals("User does not exist. Please register")) {
				System.out.println("First" + jsonString);
				rawData.remove(jsonString);
				return;
			}
			JsonValue base = game.jsonReader.parse((String)jsonString);
			JsonValue component = base.child;
			switch(component.asByte()) {
				case JsonHeader.ORIGIN_SERVER:
					//parseOriginServer();
					break;
				case JsonHeader.ORIGIN_CLIENT:
					parseOriginClient(component.next().asByte(), (String) jsonString);
					break;
				default:
					System.out.println(jsonString);
					break;
			}
		}
	}
	
	/**
	 * Parse data from a client
	 * @param jsonType
	 * @param jsonString
	 */
	private void parseOriginClient(byte jsonType, String jsonString) {
		switch(jsonType) {
			case JsonHeader.TYPE_LOGIN:
//				System.out.println(jsonString);
				break;
			case JsonHeader.TYPE_PLAYER:
				PlayerData playD = game.json.fromJson(PlayerData.class, jsonString);
				rawData.remove(jsonString);
				if(playD.getId() != 2) {
					rxFromServer.add(playD);
				}
				break;
			case JsonHeader.TYPE_PROJECTILE:
				ProjectileData projD = game.json.fromJson(ProjectileData.class, jsonString); 
				projD.adjustPositionForTest(); // for testing with the echo server (adds 150 to y)
				rawData.remove(jsonString);
				rxFromServer.add(projD);
				System.out.println(jsonString);
				break;
		}
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
		// {jsonOrigin:1,jsonType:2,id:0,position:{x:20480,y:12800},direction:{x:1499.3683,y:-43.52321},rotation:-91.6627,lifeTime:2,friendly:false}
	}
	
	/**
	 * Used for logging a user into the server, called from SplashScreen
	 */
	public boolean login(String user, String pass) {
		LoginData login = new LoginData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_LOGIN, user, pass);
		if(client.isOpen()) {
			client.send(game.json.toJson(login));
			return true;
		}
		return false;
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

}
