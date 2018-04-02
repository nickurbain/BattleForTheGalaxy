package com.bfg.backend;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.enums.ClientJsonType;
import com.bfg.backend.enums.ServerJsonType;
import com.bfg.backend.model.User;
import com.bfg.backend.repository.UserRepository;
import com.google.gson.JsonObject;

public class LoginThread extends Thread {
	UserRepository userRepository;
	
	private WebSocketSession client;
	private Thread t;
	private User user;
	private int type;
	
	public LoginThread(UserRepository userRepository, User user, WebSocketSession client, int type) {
		this.userRepository = userRepository;
		this.user = user;
		this.client = client;
		this.type = type;
	}
	
	@Override
	public void run() {
		if (type == ClientJsonType.REGISTRATION.ordinal()) {
			registration();
		} else {
			login();
		}
	}
	
	public void start() {
		System.out.println("Starting Login thread");
		if(t == null) {
			t = new Thread(this);
			t.start();
		}
	}
	
	/*
	 * Checks if it is a valid user in the database
	 */
	public void login() {
		Long id = userRepository.findByLogin(user.getName(), user.getPass());
		String response = "init";
	
		if (id != null) {
			if (userRepository.exists(id)) {
				response =  "Validated";
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
	
	public void sendMessage(String message) {
		JsonObject res = new JsonObject();
		res.addProperty("jsonOrigin", 0);

		if (type == ClientJsonType.REGISTRATION.ordinal()) {
			res.addProperty("jsonType", ServerJsonType.REGISTRATION.ordinal());
			res.addProperty("regResponse", message);
		} else {
			res.addProperty("jsonType", ServerJsonType.LOGIN.ordinal());
			res.addProperty("loginResponse", message);
		}
		try {
			System.out.println("Login Data: " + res.toString());
			client.sendMessage(new TextMessage(res.toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		System.out.println("Login message sent, ending login thread");
	}
}
