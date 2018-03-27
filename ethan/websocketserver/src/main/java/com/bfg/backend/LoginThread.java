package com.bfg.backend;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.repository.UserRepository;
import com.google.gson.JsonObject;

public class LoginThread extends Thread {
	UserRepository userRepository;
	
	private WebSocketSession client;
	private Thread t;
	private User user;
	
	public LoginThread(UserRepository userRepository, User user, WebSocketSession client) {
		this.userRepository = userRepository;
		this.user = user;
		this.client = client;
	}
	
	@Override
	public void run() {
		login();
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
	
	public void sendMessage(String message) {
		JsonObject res = new JsonObject();
		res.addProperty("jsonOrigin", 0);
		res.addProperty("jsonType", 0);
		res.addProperty("loginResponse", message);
		
		try {
			client.sendMessage(new TextMessage(res.toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		System.out.println("Login message sent, ending login thread");
	}
}
