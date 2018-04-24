package com.bfg.backend.threads;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.OnlineUsers;
import com.bfg.backend.enums.ClientJsonType;
import com.bfg.backend.enums.ServerJsonType;
import com.bfg.backend.model.User;
import com.bfg.backend.repository.UserRepository;
import com.google.gson.JsonObject;

/**
 * LoginThread for querying the database for login and register requests
 * 
 * @author emball, jln
 *
 */
public class LoginThread extends Thread {
	UserRepository userRepository;
	
	private WebSocketSession client;
	private Thread t;
	private User user;
	private int type;
	private boolean logged_in;
	
	/**
	 * Constructor for initializing a LoginThread
	 * 
	 * @param userRepository: the repository to query
	 * @param user: the user to enact querys with
	 * @param client: a websocketsession for sending a response to
	 * @param type: login or register
	 */
	public LoginThread(UserRepository userRepository, User user, WebSocketSession client, int type, boolean logged_in) {
		this.userRepository = userRepository;
		this.user = user;
		this.client = client;
		this.type = type;
		this.logged_in = logged_in;
	}
	
	@Override
	public void run() {
		if(logged_in) {
			String errorResponse = "User already exists & is logged in";
			sendMessage(errorResponse);
		}
		else if (type == ClientJsonType.REGISTRATION.ordinal()) {
			registration();
		} else if (type == ClientJsonType.LOGIN.ordinal()){
			login();
		} else {
			addAlliance();
		}
	}

	/**
	 * Starts the login thread
	 */
	public void start() {
		System.out.println("Starting Login thread");
		if(t == null) {
			t = new Thread(this);
			t.start();
		}
	}
	
	/**
	 * Checks if it is a valid user in the database
	 */
	public void login() {
		Long id = userRepository.findByLogin(user.getName(), user.getPass());
		String alliance = userRepository.findAllianceById(id);
		
		String response = "init";
	
		System.out.println("Server ~ User name: " + user.getName());
		System.out.println("Password: " + user.getPass());
		user.setId(id);
		user.setAllianceName(alliance);
		
		if (id != null) {
			if (userRepository.exists(id)) {
				response =  "Validated";
				
				OnlineUsers.addUser(client, user); // TODO -- TEST
			} else {
				response = "DENIED SUCKA";
			}
		} else {
			response = "User does not exist. Please register";
		}
		sendMessage(response);
	}
	
	/**
	 * Checks if it is a valid user in the database, if not, add user to the database
	 */
	public void registration() {
		
		String response = "User added successfully";
		System.out.println("User name: " + user.getName());
		System.out.println("Password: " + user.getPass());
		
		if (userRepository.findByUsername(user.getName()) != null) {
			response = "Thats a great name, but someone already has it";
		} else {
			userRepository.createUser(user.getName(), user.getPass());
		}
		sendMessage(response);
	}
	
	private void addAlliance() {
		// TODO Auto-generated method stub
		userRepository.addAlliance(user.getAllianceName(), user.getName());
	}
	
	/**
	 * Sends the given message to the associated session
	 * 
	 * @param message
	 */
	public void sendMessage(String message) {
		JsonObject res = new JsonObject();
		res.addProperty("jsonOrigin", 0);

		if (type == ClientJsonType.REGISTRATION.ordinal()) {
			res.addProperty("jsonType", ServerJsonType.REGISTRATION.ordinal());
			res.addProperty("regResponse", message);
		} else {
			res.addProperty("jsonType", ServerJsonType.LOGIN.ordinal());
			res.addProperty("loginResponse", message);
			res.addProperty("alliance", user.getAllianceName());
		}
		try {
			System.out.println("Login Data: " + res.toString());
			client.sendMessage(new TextMessage(res.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		System.out.println("Login message sent, ending login thread");
	}
}
