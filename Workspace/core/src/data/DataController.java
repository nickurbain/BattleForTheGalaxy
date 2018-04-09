package data;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.utils.JsonValue;

import battle.galaxy.BattleForTheGalaxy;
import controllers.JsonController;

/*
 * DataController is the class that controls the input/output of data
 * to/from the Server/Game. It contains a listener to listen for input
 * from the server and when it is received it will change state to be
 */

public class DataController {
	
	//Final Vars
	private String JAMES_URI = "ws://localhost:8080/bfg";
	private String TEST_URI = "ws://echo.websocket.org";
	private String BASE_URI = "ws://proj-309-vc-2.cs.iastate.edu:8080/bfg";
	
	private BattleForTheGalaxy game;
	private JsonController jsonController;
	//Client endpoint for websocket
	private Client client;
	//ArrayList for storing raw data from server
	private List<String> rawData = new CopyOnWriteArrayList<String>();
	//Storage for parsed objects
	private ArrayList<Object> rxFromServer = new ArrayList<Object>();
	
	private URI uri;
	
	private int matchId;
	
	
	/**
	 * Constructor which is passed the game, starts the listener, and sets state to false
	 */
	public DataController(BattleForTheGalaxy game) {
		this.game = game;
		jsonController = new JsonController();
		setupWebSocket();
	}
	
	/**
	 * Connect WebSocket to the server
	 */
	public void setupWebSocket() {
		try {
			//uri = new URI(JAMES_URI);
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
	 * Send some data object to the server
	 * @param data the data object to be sent to the server
	 */
	public void sendToServer(Object data) {
		if(data.getClass() == HitData.class) {
			client.send(jsonController.hitToJson(data, ((HitData) data).getCausedDeath()));
		}else if(data.getClass() == String.class){
			client.send((String) data);
		}else {
			client.send(jsonController.dataToJson(data));
		}
	}
	
	/**
	 * Send data to the server and wait for a response
	 * @param data the data to be sent to the server
	 * @return json the server's response
	 */
	public String sendToServerWaitForResponse(Object data) {
		client.send(jsonController.dataToJson(data));
		
		while(rawData.isEmpty()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//
		return rawData.get(0);
	}
	
	/**
	 * Parses raw data from server
	 */
	public void parseRawData() {
		for(String jsonString: rawData) {
			JsonValue base = jsonController.getJsonReader().parse((String)jsonString);
			JsonValue component = base.child;
			switch(component.asInt()) {
				case JsonHeader.ORIGIN_SERVER:
					parseOriginServer(component.next().asInt(), (String) jsonString);
					break;
				case JsonHeader.ORIGIN_CLIENT:
					parseOriginClient(component.next().asInt(), (String) jsonString);
					//System.out.println("DC.parseRawData RX: " + jsonString); // for debugging
					break;
				default:
					System.out.println("DataController.parseRawData - ERROR: incoming JSON Origin not Server or Client:\n\t" + jsonString);
					break;
			}
		}
	}
	
	private void parseOriginServer(int jsonType, String jsonString) {
		JsonValue base = jsonController.getJsonReader().parse((String)jsonString);
		JsonValue component = base.child;
		System.out.println("DataController: JSON type: " + jsonType);
		switch(jsonType) {
		case JsonHeader.TYPE_MATCH_NEW:
			matchId = base.getInt("matchId");
			rawData.remove(jsonString);
			break;
		case JsonHeader.TYPE_MATCH_END:
			rxFromServer.add(jsonString);
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
				PlayerData playD = jsonController.getJson().fromJson(PlayerData.class, jsonString);
				rawData.remove(jsonString);
				if(playD.getId() != matchId) {
					rxFromServer.add(playD);
				}
				break;
			case JsonHeader.TYPE_PROJECTILE:
				ProjectileData projD = jsonController.getJson().fromJson(ProjectileData.class, jsonString); 
				//projD.adjustPositionForTest(); // for testing with the echo server (adds 150 to y)
				rawData.remove(jsonString);				
				if(projD.getSource() != matchId) {
					rxFromServer.add(projD);
				}
				break;
			case JsonHeader.TYPE_HIT:
				HitData hitData = jsonController.getJson().fromJson(HitData.class, jsonString);
				rawData.remove(jsonString);
				rxFromServer.add(hitData);
				break;
			case JsonHeader.TYPE_JOINMATCH:
				System.out.println("DC.parseOriginClient: received a Client|JoinMatch Json");
				rawData.remove(jsonString);
				break;
			case JsonHeader.TYPE_REGISTRATION:
				System.out.println("Data Controller: " + jsonString);
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
			JsonValue base = jsonController.getJsonReader().parse((String)jsonString);
			JsonValue component = base.child;
			JsonValue componentNext = component.next();
			if(component.asInt() == JsonHeader.ORIGIN_SERVER && componentNext.asInt() == JsonHeader.TYPE_DB_SHIP) {
				ship = jsonController.getJson().fromJson(Ship.class, jsonString);
				rawData.remove(jsonString);
			}
		}
		
		return ship;
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

	/**
	 * Gets the match id
	 * @return matchId the id of the match
	 */
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
	 * Gets the locally saved ship data
	 * @return Ship ship with locally saved data
	 */
	public Ship getShipLocal() {
		Ship ship = new Ship();
		/*String content = "";
	    try{
	        content = new String (Files.readAllBytes(Paths.get("/BattleForTheGalaxy-core/assets/ship.txt")));
	    } catch (IOException e)
	    {
	        e.printStackTrace();
	    }*/
		String content = "{}";
	    ship = jsonController.getJson().fromJson(Ship.class, content);
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
			out.write(jsonController.dataToJson(ship));
			System.out.println("SAVED: " + jsonController.dataToJson(ship));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * Get the jsonController
	 * @return the jsonController
	 */
	public JsonController getJsonController() {
		return jsonController;
	}

}
