package com.bfg.backend;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;
import com.bfg.backend.model.User;

public class OnlineUsers {
	
	private static ConcurrentHashMap<WebSocketSession, User> onlineUsers;
	
	private static OnlineUsers ou;
	
	private OnlineUsers() {
		onlineUsers = new ConcurrentHashMap<>();
	}
	
	
	public static OnlineUsers getInstance() {
		return ou;
	}
	
	public static void setInstance() {
		if(null == ou) {
			ou = new OnlineUsers();
		}
	}
	
	public static synchronized void addUser(WebSocketSession session, User user) {
		onlineUsers.put(session, user);
	}
	
	public static synchronized User getUser(WebSocketSession session) {
		return onlineUsers.get(session);
	}
	
	public static synchronized boolean userOnline(WebSocketSession session) {
		return onlineUsers.containsKey(session);
	}
	
	public static synchronized boolean userOnline(int userId) {
		for(User user : onlineUsers.values()) {
			if(user.getId() == userId) {
				return true;
			}
		}
		return false;
	}
	
	public static synchronized WebSocketSession getUserSessionById(int userId) {
		for(Entry<WebSocketSession, User> entry : onlineUsers.entrySet()) {
			if(entry.getValue().getId() == userId) {
				return entry.getKey();
			}
		}
		return null;	
	}
	
	public static synchronized boolean isEmpty() {
		return onlineUsers.isEmpty();
	}
	
	public static synchronized void removeUser(WebSocketSession session) {
		onlineUsers.remove(session);
	}
}
