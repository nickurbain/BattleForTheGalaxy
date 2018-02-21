package data;

import battle.galaxy.*;

import javax.websocket.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.JsonValue;

import battle.galaxy.BattleForTheGalaxy;

/*
 * DataController is the class that controls the input/output of data
 * to/from the Server/Game. It contains a listener to listen for input
 * from the server and when it is received it will change state to be
 */

public class DataController {
	
	//Final Vars
	private String BASE_URI = "ws://proj-309-vc-2.cs.iastate.edu:8080";
	
	private BattleForTheGalaxy game;
	//Client endpoint for websocket
	private Client client;
	//ArrayList for storing raw data from server
	private List<String> rawData = new CopyOnWriteArrayList<String>();
	//Storage for parse data from the listener
	private EntityData receievedEntity;
	//State that is true when new data has been received from the listener, false otherwise
	private boolean state;
	
	URI uri;
	
	
	/**
	 * Constructor which is passed the game, starts the listener, and sets state to false
	 */
	public DataController(BattleForTheGalaxy game) {
		this.game = game;
		setupWebSocket();
		state = false;
	}
	
	/**
	 * Connect WebSocket to the server
	 */
	private void setupWebSocket() {
		try {
			client = new Client(new URI("ws://echo.websocket.org"), this);
			client.connectBlocking();
		} catch (URISyntaxException | InterruptedException e) {
			e.printStackTrace();
			System.out.println("Client-Server connection could not be made.");
		}
	}
	/**
	 * Parses raw data from server
	 */
	private void parseRawData() {
		for(Iterator<String> iter = rawData.iterator(); iter.hasNext();) {
			String data = iter.next();
			JsonValue base = game.jsonReader.parse(data);
			JsonValue component = base.child;
			if(component.name == "p") {
				if(component.asInt() != 2) {
					game.json.setIgnoreUnknownFields(true);
					this.receievedEntity = game.json.fromJson(PlayerData.class, data);
				}
			}
		}
	}

	private void parsePlayerData(String data) {
		//TODO
	}
	
	/**
	 * Sends data from the game to the server
	 */
	public void updateServerData(PlayerData playerData, ProjectileData projectileData) {
		String player = game.getJson().toJson(playerData);
		//TODO send to server
		String projectile = "";
		if(projectileData != null) {
			projectile = game.getJson().toJson(projectileData);
			//TODO send to server
		}
	}
	
	/**
	 * Used for logging a user into the server, called from SplashScreen
	 */
	public boolean login(String user, String pass) {
		LoginData login = new LoginData(user, pass);
		client.send(game.json.toJson(login));
		return false;
	}
	
	public boolean getState() {
		return state;
	}
	
	public PlayerData getEntity() {
		return (PlayerData) receievedEntity;
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
	 * Closes the WebSocket when we are done
	 */
	public void close() {
		client.close();
	}

}
