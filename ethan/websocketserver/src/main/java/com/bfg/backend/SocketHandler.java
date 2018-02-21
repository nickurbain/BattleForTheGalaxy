package com.bfg.backend;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.bfg.backend.repository.UserRepository;


@Controller
public class SocketHandler extends TextWebSocketHandler {
	List sessions = new CopyOnWriteArrayList<>();
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {

		System.out.println(message);
		
		//Map value = new Gson().fromJson(message.getPayload(), Map.class);
		/*
		for (WebSocketSession webSocketSession : sessions) {
			Map value = new Gson().fromJson(message.getPayload(), Map.class);
			webSocketSession.sendMessage(new TextMessage("Hello " + value.get("name") + " !"));
		}
		*/
			
		User n = new User();
		n.setName(message.getPayload());
//		System.out.println(n);
		n.setPass(message.getPayload());
		
		userRepository.save(n);
		
		session.sendMessage(new TextMessage("Saved " + message.getPayload() + "!"));
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// the messages will be broadcasted to all users.
		sessions.add(session);
	}
}