package com.bfg.websocket.config;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bfg.chat.model.Message;

@Controller
public class WebSocketController {

	@MessageMapping("/chat/{topic}")
	@SendTo("/topic/messages")
	public Message send(@DestinationVariable("topic") String topic, Message message) throws Exception {
		return message;
	}
	
//	public String Greeting() {
//		return "HELLO";
//	}
	
	
}
