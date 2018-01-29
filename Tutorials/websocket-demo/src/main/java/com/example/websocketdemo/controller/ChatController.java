package com.example.websocketdemo.controller;

import com.example.websocketdemo.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * All the messages sent from clients with a destination starting 
 * with /app will be routed to these message handling methods 
 * annotated with @MessageMapping.
 * 
 * @author James Lockard
 *
 */
@Controller
public class ChatController {

	/**
	 * Routes messages with the destination /app/chat.sendmessage
	 * to this method
	 * 
	 * @param chatMessage
	 * 			See ChatMessage.java for more information
	 * @return
	 * 			The message stored in the chatMessage variable 
	 * 			that the client sent to the server.
	 */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    /**
     * Routes messages with the destination /app/chat.addUser
     * to this method. Broadcasts the user join event to other
     * user. See WebSocketEventListener for more info.
     * 
     * @param chatMessage
     * 			See model.ChatMessage
     * @param headerAccessor
     * 			Accesses the sessions attributes and stores the username
     * 			in chatmessage.getsender(). See ChatMessage.java for more
     * 			information.
     * @return
     * 			The message the client sent to the server
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, 
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}