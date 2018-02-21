package websocketclient;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class Client extends WebSocketClient{
	
//	private DataController dataController;

//	public Client(URI serverUri, DataController dataController) {
	public Client(URI serverUri) {
		super(serverUri);
//		this.dataController = dataController;
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
		System.out.println(arg0);
//		dataController.newData(arg0);
	}

	@Override
	public void onOpen(ServerHandshake arg0) {
		System.out.println("Connected");
	}

}
