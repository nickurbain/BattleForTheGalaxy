package data;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/*
 * Client WebSocket class used to talk with the server
 */
public class Client extends WebSocketClient{
	
	private DataController dataController;

	/**
	 * Constructor 
	 * 
	 * @param serverUri the address of the server
	 * @param dataController the Game's DataController
	 */
	public Client(URI serverUri, DataController dataController) {
		super(serverUri);
		this.dataController = dataController;
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
		System.out.println("Closed");
	}

	@Override
	public void onError(Exception arg0) {
		System.out.println("Error:" + arg0);
	}

	@Override
	public void onMessage(String arg0) {
//		System.out.println(arg0);  		// Uncommenting this prints every received socket message
		//Send new data to the DataController for processing
		dataController.newData(arg0);
	}

	@Override
	public void onOpen(ServerHandshake arg0) {
		System.out.println("Connected to: " + getURI());
	}

}
