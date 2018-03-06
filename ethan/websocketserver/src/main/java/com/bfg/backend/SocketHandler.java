package com.bfg.backend;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.bfg.backend.repository.UserRepository;
import com.bfg.backend.BroadcastThread;
import com.bfg.backend.ServerThread;

@Controller
public class SocketHandler extends TextWebSocketHandler{

	@Autowired
	private UserRepository userRepository;
	
	private BroadcastThread bc;

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {

		// Make json object
		JsonObject jsonObj = new JsonParser().parse(message.getPayload()).getAsJsonObject();

		// Test
//		testPrints(jsonObj);
		shortTest(jsonObj, session);

		mainController(session, message, jsonObj);

	}

	/*
	 * The main message handling method.
	 * Basically the routing controller.
	 */
	private void mainController(WebSocketSession session, TextMessage message, JsonObject jsonObj) throws IOException {
		String response = "INIT CASE -- SHOULD NOT SEE THIS";

		// Login
		if (jsonObj.get("jsonType").getAsInt() == 0) {
			User user = new User();
			user.setName(jsonObj.get("id").getAsString());
			user.setPass(jsonObj.get("pass").getAsString());
			response = login(user);
			session.sendMessage(new TextMessage(response));
		}
		// Broadcast locations
		else if (jsonObj.get("jsonType").getAsInt() == 1) {
			addMessageToBroadcast(message);
		}
		// Else, ERROR
		else {
			System.err.println("Error!");
		}
	}
	
	/*
	 * Initialized the broadcasting thread bc
	 * The PostConstruct annotation is used to run this method only once when the server starts
	 */
	@PostConstruct
	public void init() {
		System.out.println("######################################################################################################################");
		System.out.println("######################################################################################################################");
		System.out.println("######################################################################################################################");
		System.out.println("######################################################################################################################");
	
		bc = new BroadcastThread();
		bc.start();
	}
	
	/*
	 * Adds a message to the message queue in the broadcasting thread
	 */
	public void addMessageToBroadcast(TextMessage message) throws IOException {
		bc.addMessage(message);
	}

	/*
	 * Checks if it is a valid user in the database
	 */
	public String login(User user) {
		Long id = userRepository.findByLogin(user.getName(), user.getPass());
		loginTests(user, id);

		if (id != null) {
			if (userRepository.exists(id)) {
				return "Validated";
			} else {
				return "DENIED SUCKA";
			}
		} else {
			return "User does not exist. Please register";
		}	
	}
	
	
	/*
	 * Handles new websocket connections
	 * Makes a thread for each new session
	 * 
	 * (non-Javadoc)
	 * @see org.springframework.web.socket.handler.AbstractWebSocketHandler#afterConnectionEstablished(org.springframework.web.socket.WebSocketSession)
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("********Websocket Connection OPENED!********");
		System.out.println("WS session ID: " + session.getId());
		System.out.println("********************************************");
		bc.addSession(session);
	}

	/*
	 * Handles websocket session disconnect
	 * Prints to the console for debugging and tracking purposes
	 * 
	 * (non-Javadoc)
	 * @see org.springframework.web.socket.handler.AbstractWebSocketHandler#afterConnectionClosed(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.CloseStatus)
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("********Websocket Connection CLOSED!********");
		System.out.println("WS session ID: " + session.getId());
		System.out.println("********************************************");
		bc.removeSession(session);
		super.afterConnectionClosed(session, status);
		
	}
	
	
	
	
	/****** Testing methods *******/

	/*
	 * Tests login
	 */
	private void loginTests(User user, Long id) {
		System.out.println("LOGIN TEST >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("Username sent from client: " + user.getName());
		System.out.println("Password: " + user.getPass());
		System.out.println("Id FROM DATABASE: " + id);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	/*
	 * Tests the standard json given
	 */
	private void testPrints(JsonObject jsonObj) {
		System.out.println("TESTPRINTS ----------------------------------------------");
		System.out.println("jsonOrigin (From client = 1, From server = 0): " + jsonObj.get("jsonOrigin").getAsInt());
		System.out.println("jsonType (0 = login, 1 = broadcast):  " + jsonObj.get("jsonType").getAsInt());
		System.out.println("Sent id: " + jsonObj.get("id").getAsString());

		if (jsonObj.get("jsonType").getAsInt() == 0) {
			System.out.println("Sent pass: " + jsonObj.get("pass").getAsString());
		}
		System.out.println("---------------------------------------------------------");
	}
	
	/*
	 * Tests with shorter printing to the console for easier reading of multiple threads.
	 */
	private void shortTest(JsonObject jsonObj, WebSocketSession session) {
		System.out.println("Session ID: " + session.getId() + " | Sent ID: " + jsonObj.get("id").getAsString());
	}


}


/** TODO **
* When a connection is received:
* 	Create a new thread to handle the connection
* 		Handle login
*		Handle message
*			Add message to queue
*		
*
* Server will continually broadcast messages off the queue to all connected clients
* 	Should there be a thread that just does this?
*
*/