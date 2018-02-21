package com.bfg.backend;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.bfg.backend.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


@Controller
public class SocketHandler extends TextWebSocketHandler {
	List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {

		String response = "MILK: TEST CASE -- SHOULDN'T SEE THIS. INIT PART";
		
		// Make json object
		JsonObject jsonObj = new JsonParser().parse(message.getPayload()).getAsJsonObject();
		
		// Test
		testPrints(jsonObj);
	
		// Login
		if(jsonObj.get("jsonType").getAsInt() == 0) {
			User user = new User();
			user.setName(jsonObj.get("id").getAsString());
			user.setPass(jsonObj.get("pass").getAsString());
			response = login(user);
			session.sendMessage(new TextMessage(response));
		}
		// Broadcast locations
		else if(jsonObj.get("jsonType").getAsInt() == 1) {
			broadcast(session, message);
		}
		// Else, ERROR
		else {
			System.err.println("Error!");
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// the messages will be broadcasted to all users.
		sessions.add(session);
	}
	
	
	public String login(User user) {
		loginTests(user);
		
		Long id = userRepository.findByLogin(user.getName(), user.getPass());
		if(id != null) {
			if(userRepository.exists(id)) {
				return "Validated";
			}
			else {
				return "DENIED SUCKA";
			}
		}
		else {
			return "User does not exist. Please register";
		}
	}
	
	
	public void broadcast(WebSocketSession session, TextMessage message) throws IOException {
		for (WebSocketSession webSocketSession : sessions) {
			webSocketSession.sendMessage(message);
		}
	}
	
	private void loginTests(User user) {
		System.out.println("LOGIN TEST >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("Username sent from client: " + user.getName() + ", Password: " + user.getPass());
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}
	
	private void testPrints(JsonObject jsonObj) {
		System.out.println("TESTPRINTS ----------------------------------------------");
		System.out.println("jsonOrigin: " + jsonObj.get("jsonOrigin").getAsInt());
		System.out.println("jsonType: " + jsonObj.get("jsonType").getAsInt());
		System.out.println("id: " + jsonObj.get("id").getAsString());
		
		if(jsonObj.get("jsonType").getAsInt() == 0) {
			System.out.println("pass: " + jsonObj.get("pass").getAsString());
		}
		System.out.println("---------------------------------------------------------");
	}
}

















/*
System.out.println(message);



//JSONObject json = new JSONObject.put(message.getPayload());




//Map value = new Gson().fromJson(message.getPayload(), Map.class);
/*
for (WebSocketSession webSocketSession : sessions) {
	Map value = new Gson().fromJson(message.getPayload(), Map.class);
	webSocketSession.sendMessage(new TextMessage("Hello " + value.get("name") + " !"));
}

	
User n = new User();
n.setName(message.getPayload());
//System.out.println(n);
n.setPass(message.getPayload());

userRepository.save(n);

session.sendMessage(new TextMessage("Saved " + message.getPayload() + "!"));



		
//		Map value = new Gson().fromJson(message.getPayload(), Map.class);
		
//		System.out.println(value.get("type"));
		
//		session.sendMessage(new TextMessage("Saved " + value.get("Id") + "!"));
		
//		User n = new User();
//		n.setName(message.getPayload());
////		System.out.println(n);
//		n.setPass(message.getPayload());
//		
//		userRepository.save(n);
//		
//		session.sendMessage(new TextMessage("Saved " + message.getPayload() + "!"));


*/