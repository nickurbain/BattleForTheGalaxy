package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;

import battle.galaxy.BattleForTheGalaxy;

public class DataController {
	
	BattleForTheGalaxy game;
	
	private String userId;
	private String pass;
	
	public DataController(BattleForTheGalaxy game) {
		this.game = game;
		
		 new Thread(new Runnable(){

	            @Override
	            public void run() {
	                ServerSocketHints serverSocketHint = new ServerSocketHints();
	                ServerSocket serverSocket = Gdx.net.newServerSocket(Protocol.TCP, 8081, serverSocketHint);
	                // Loop forever
	                while(true){
	                    // Create a socket
	                    Socket socket = serverSocket.accept(null);
	                    
	                    // Read data from the socket into a BufferedReader
	                    BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
	                }
	            }
	        }).start();
	}

	public void updateGameData(PlayerData playerData, ProjectileData projectileData) {
		String player = game.getJson().toJson(playerData);
		//TODO send to server
		String projectile = "";
		if(projectileData != null) {
			projectile = game.getJson().toJson(projectileData);
			//TODO send to server
		}
	}
	
	
	
	
	

}
