package data;

import battle.galaxy.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.JsonValue;

import battle.galaxy.BattleForTheGalaxy;

/*
 * DataController is the class that controls the input/outputof data
 * to/from the Server/Game. It contains a listener to listen for input
 * from the server and when it is received it will change state to be
 */

public class DataController {
	private BattleForTheGalaxy game;
	//Listener thread for receiving input from the server.
	private Listener listener;
	//Storage for parse data from the listener
	private EntityData receievedEntity;
	//State that is true when new data has been received from the listener, false otherwise
	private boolean state;
	
	/*
	 * Constructor which is passed the game, starts the listener, and sets state to false
	 */
	public DataController(BattleForTheGalaxy game) {
		this.game = game;
		listener = new Listener(game.client);
		listener.start();
		state = false;
	}
	/*
	 * Checks to see if the listener has received data, if it has
	 * sets state to true, parses data and stores it
	 */
	public void updateGameData() {
		if(listener.recieved()) {
			state = true;
			parse(listener.getInput());
		}else {
			return;
		}
	}
	/*
	 * Parses data from server
	 */
	private void parse(String data) {
		JsonValue base = game.jsonReader.parse(data);
		JsonValue component = base.child;
		if(component.name == "p") {
			if(component.asInt() != 2) {
				game.json.setIgnoreUnknownFields(true);
				this.receievedEntity = game.json.fromJson(PlayerData.class, data);
			}
		}
	}

	private void parsePlayerData(String data) {
		//TODO
	}
	
	/*
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
	
	public boolean getState() {
		return state;
	}
	
	public PlayerData getEntity() {
		return (PlayerData) receievedEntity;
	}
	

}
