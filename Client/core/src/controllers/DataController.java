package controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import battle.galaxy.BattleForTheGalaxy;
import data.Client;
import data.CoreData;
import data.GenericData;
import data.HitData;
import data.JsonHeader;
import data.JuggernautData;
import data.NewMatchData;
import data.PlayerData;
import data.PlayerDisconnectData;
import data.ProjectileData;
import data.Ship;

/**
 * DataController is the class that controls the input/output of data
 * to/from the Server/Game. It contains a listener to listen for input
 * from the server and when it is received it will change state to be
 */
public class DataController {
	
	//Final Vars
	private String JAMES_URI = "ws://localhost:8080/bfg";
	//private String TEST_URI = "ws://echo.websocket.org";
	private String BASE_URI = "ws://proj-309-vc-2.cs.iastate.edu:8080/bfg";
	
	private static BattleForTheGalaxy game;
	private JsonController jsonController;
	//Client endpoint for websocket
	private Client client;
	//ArrayList for storing raw data from server
	private List<String> rawData = new CopyOnWriteArrayList<String>();
	//Storage for parsed objects
	private ArrayList<Object> rxFromServer = new ArrayList<Object>();
	//Storage for chat messages
	private ArrayList<String> chatDataFromServer = new ArrayList<String>();
	
	private URI uri;
	
	private int matchId;
	
	
	/**
	 * Constructor which is passed the game, starts the listener, and sets state to false
	 */
	public DataController(BattleForTheGalaxy new_game) {
		game = new_game;
		jsonController = new JsonController();
		setupWebSocket(false);
	}
	
	/**
	 * The game to be used between screens
	 * @return game
	 */
	public static BattleForTheGalaxy getGame() {
		return game;
	}
	
	/**
	 * Connect WebSocket to the server
	 */
	public void setupWebSocket(boolean james) {
		try {
			if(james) {
				uri = new URI(JAMES_URI);
			}else {
				uri = new URI(BASE_URI);
			}
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
		//System.out.println("Object in: " + data);
		//System.out.println("Data type: " + data.getClass());
		if(data.getClass() == String.class){
			client.send((String) data);
		} else {
			client.send(jsonController.dataToJson(data));
		}
	}
	
	/**
	 * Send data to the server and wait for a response
	 * @param data the data to be sent to the server
	 * @return json the server's response
	 */
	public Object sendToServerWaitForResponse(Object data, boolean convert) {
		if(convert) {
			client.send(jsonController.dataToJson(data));
		}else {
			client.send((String) data);
		}
		System.out.println("STS: " + (String) jsonController.dataToJson(data));
		while(rawData.isEmpty()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Raw Data: " + (String) rawData.get(0));
		String response = rawData.get(0);
		rawData.remove(0);
		//Check if the data is NewMatchData
		if(jsonController.convertFromJson(response, NewMatchData.class).getClass() != String.class) {
			return (NewMatchData)jsonController.convertFromJson(response, NewMatchData.class);
		}
		
		return response;
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
	
	/**
	 * Parse raw data that originated from the server
	 * @param jsonType the type of message
	 * @param jsonString the message
	 */
	private void parseOriginServer(int jsonType, String jsonString) {
		JsonValue base = jsonController.getJsonReader().parse((String)jsonString);
		//System.out.println("DataController: JSON type: " + jsonType);
		switch(jsonType) {
		case JsonHeader.TYPE_MATCH_NEW:
			matchId = base.getInt("matchId");
			break;
		case JsonHeader.TYPE_MATCH_END:
			GenericData match = jsonController.getJson().fromJson(GenericData.class, jsonString);
			rxFromServer.add(match);
			break;
		case JsonHeader.TYPE_MATCH_STATS:
			GenericData stats = jsonController.getJson().fromJson(GenericData.class, jsonString);
			rxFromServer.add(stats);
			break;
		case JsonHeader.PLAYER_DISCONNECT:
			rxFromServer.add(jsonController.convertFromJson(jsonString, PlayerDisconnectData.class));
			break;
		case JsonHeader.SELECT_JUGGERNAUT:
			JuggernautData jd = jsonController.getJson().fromJson(JuggernautData.class, jsonString);
			rxFromServer.add(jd);
			break;
		case JsonHeader.S_TYPE_REGISTRATION:
			System.out.println("DataController: parseOriginServer -> Registration: " + jsonString);
			break;
		default:
			System.out.println("Parse Origin Server default: " + jsonString);
			break;
		}
		rawData.remove(jsonString);
	}
	/**
	 * Parse data that originated from client
	 * @param jsonType
	 * @param jsonString
	 */
	private void parseOriginClient(int jsonType, String jsonString) {
		switch(jsonType) {
			case JsonHeader.TYPE_PLAYER:
				PlayerData playD = jsonController.getJson().fromJson(PlayerData.class, jsonString);
				rawData.remove(jsonString);
				if(playD.getId() != matchId) {
					rxFromServer.add(playD);
				}
				break;
			case JsonHeader.TYPE_PROJECTILE:
				ProjectileData projD = jsonController.getJson().fromJson(ProjectileData.class, jsonString); 
				rawData.remove(jsonString);				
				if(projD.getSource() != matchId) {
					rxFromServer.add(projD);
				}
				break;
			case JsonHeader.TYPE_HIT:
				HitData hitData = (HitData) jsonController.convertFromJson(jsonString, HitData.class);
				rawData.remove(jsonString);
				rxFromServer.add(hitData);
				break;
			case JsonHeader.TYPE_CORE_UPDATE:
				CoreData coreData = (CoreData) jsonController.convertFromJson(jsonString, CoreData.class);
				//System.out.println("DataController: Core Update: " + jsonString);
				rawData.remove(jsonString);
				rxFromServer.add(coreData);
				break;
			case JsonHeader.TYPE_REGISTRATION:
				System.out.println("Data Controller: " + jsonString);
				rawData.remove(jsonString);
				break;
			case JsonHeader.C_TYPE_MESSAGE:
				jsonController.getJson().setOutputType(OutputType.json);
				chatDataFromServer.add(jsonController.getJsonReader().parse(jsonString).getString("message"));
				jsonController.getJson().setOutputType(OutputType.minimal);
				//System.out.println("Received a message from the server: " + jsonString);
				rawData.remove(jsonString);
				break;
			default:
				System.out.println("Parse Origin Client unknown JsonType: " + jsonString);
				rawData.remove(jsonString);
		}
	}
	
	/**
	 * Reads data from the server. Called from Client.
	 * 
	 * @param data raw data from the server
	 */
	public void newRawData(String data) {
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
	 * TODO
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
	 * Get the jsonController
	 * @return the jsonController
	 */
	public JsonController getJsonController() {
		return jsonController;
	}
	
	/**
	 * Get the rawData
	 * @return the rawData
	 */
	public List<String> getRawData(){
		return rawData;
	}

	/**
	 * @return the chatDataFromServer
	 */
	public ArrayList<String> getChatDataFromServer() {
		return chatDataFromServer;
	}
	
}