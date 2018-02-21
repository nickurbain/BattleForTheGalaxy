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
	 * Parse data 
	 * @param jsonType
	 * @param jsonString
	 */
	private void parseOriginClient(byte jsonType, String jsonString) {
		switch(jsonType) {
			case JsonHeader.TYPE_LOGIN:
				System.out.println(jsonString);
				break;
			case JsonHeader.TYPE_PLAYER:
				PlayerData pd = game.json.fromJson(PlayerData.class, jsonString);
				rawData.remove(jsonString);
				if(pd.getId() != 1) {
					rxFromServer.add(pd);
				}
				break;
		}
	}
	
	/**
	 * Sends data from the game to the server
	 */
	public void updateServerData(PlayerData playerData, ProjectileData projectileData) {
		String player = game.getJson().toJson(playerData);
		client.send(player);
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
