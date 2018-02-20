package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.JsonValue;

public class Listener extends Thread{
	
	private Socket client;
	private String input;
	private boolean receieved;

	public Listener(Socket client) {
		super();
		this.client = client;
		input = "";
		receieved = false;
	}
	
	@Override
	public void run() {
		String rx = "";
		BufferedReader in;
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		while(rx.isEmpty()) {
			try {
				rx = in.readLine();
				input = rx;
				receieved = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean recieved() {
		if(receieved) {
			receieved = false;
			return !receieved;
		}else {
			return receieved;
		}
	}
	
	public String getInput() {
		return input;
	}
	
}
