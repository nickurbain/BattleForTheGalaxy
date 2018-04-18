package com.bfg.backend;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.model.User;

public class OnlineUsers {
	
	private static List<WebSocketSession> sessions;
	private static ConcurrentHashMap<WebSocketSession, User> onlineUsers;
	
	private static OnlineUsers ou;
	
	private OnlineUsers() {
		sessions = new CopyOnWriteArrayList<>();
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
	
	public static void addSession(WebSocketSession session) {
		sessions.add(session);
	}
	
	public static List<WebSocketSession> getSessions() {
		return sessions;
	}
	
	public static void addUser(WebSocketSession session, User user) {
		onlineUsers.put(session, user);
	}
	
	public static User getUser(WebSocketSession session) {
		return onlineUsers.get(session);
	}
	
	public static boolean userOnline(WebSocketSession session) {
//		return onlineUsers.get(session);
		return onlineUsers.containsKey(session);
	}
	
	public static boolean isEmpty() {
		return onlineUsers.isEmpty();
	}
}
