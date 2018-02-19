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

public class DataController {
	
	BattleForTheGalaxy game;
	private Listener listener;
	private String listenerData;
	
	private GameData gameData;
	
	public DataController(BattleForTheGalaxy game) {
		this.game = game;
		listener = new Listener(game.client);
		listener.start();
		listenerData = "";
	}
	
	public void updateGameData() {
		if(checkForUpdate()) {
			listenerData = listener.getInput();
			parse(listenerData);
		}else {
			return;
		}
	}
	
	private void parse(String data) {
		EntityData entity;
		JsonValue base = game.jsonReader.parse(data);
		JsonValue component = base.child;
		if(component.name == "p") {
			if(component.asInt() != 2) {
				data = data.substring(3, data.length()-1);
				entity = parsePlayerData(data);
			}
		}
	}

	private EntityData parsePlayerData(String data) {
		return null;
	}

	private boolean checkForUpdate() {
		if(listener.recieved()) {
			return true;
		}
		return false;
	}

	public void updateServerData(PlayerData playerData, ProjectileData projectileData) {
		String player = game.getJson().toJson(playerData);
		//TODO send to server
		String projectile = "";
		if(projectileData != null) {
			projectile = game.getJson().toJson(projectileData);
			//TODO send to server
		}
	}
	
	public GameData getGameData() {
		return gameData;
	}
	
	
	
	
	

}
