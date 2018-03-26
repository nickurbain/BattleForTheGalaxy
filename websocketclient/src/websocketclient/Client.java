package websocketclient;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.*;

public class Client extends WebSocketClient{
	
	private Integer matchId;
	
	public Client(URI serverUri) {
		super(serverUri);
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
		System.out.println("Closed");
	}

	@Override
	public void onError(Exception message) {
		System.out.println("Error:" + message);
	}

	@Override
	public void onMessage(String message) {
		System.out.println("RC: " + message);
		parseMessage(message);
//		dataController.newData(arg0);
	}

	@Override
	public void onOpen(ServerHandshake message) {
		System.out.println("Connected");
	}
	
	public void parseMessage(String message) {
		JsonObject jsonObj = new JsonParser().parse(message).getAsJsonObject();
		
		if(jsonObj.get("jsonType").getAsInt() == 6) {
			this.matchId = jsonObj.get("matchId").getAsInt();
			System.out.println("MatchId set! : " + matchId);
		}
		
		
	}

	
	public Integer getMatchId() {
		return matchId;
	}
} 
