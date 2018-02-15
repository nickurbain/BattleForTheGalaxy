package com.bfg.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
	
	/*
	 * Register a websocket endpoint for clients to connect to.
	 * 
	 * STOMP = simple text oriented messaging protocol
	*/
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
//		registry.addEndpoint("/ws");
		// SockJS allows for fall-back options for browsers that don't support websocket
		registry.addEndpoint("/chat").withSockJS(); // TODO: Testing
	}
	
	
	/*
	 * Message broker used to route messages
	*/
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// Routes messages with the "/app" destination to the message-handling methods in ChatController
		registry.setApplicationDestinationPrefixes("/app");	
		
		// Routes messages with "/topic" should be routed to the message broker
		// Message broker broadcasts messages to all connected clients who are subscribed to a topic
		registry.enableSimpleBroker("/topic");
	}
}
