package com.bfg.backend.model;

public class Message {
	User user;

	String message;
	
	public Message() {}

	public Message(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
