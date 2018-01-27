package com.example.websocketdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * @author James Lockard
 * Tutorial is from:
 * https://www.callicoder.com/spring-boot-websocket-chat-example/
 * 
 * A simple WebSocket connection that overrides methods to modify
 * behavior of AbstractWebSocketMessageBrokerConfigurer
 */
@Configuration
// Used to enable a WebSocket server
@EnableWebSocketMessageBroker

public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	/**
	 * Registers a websocket endpoint that the client will use to connect to the
	 * websocket server.
	 * 
	 * withSockJS() allows browsers that do not support websocket a fall back 
	 * option.
	 * 
	 * STOMP (Simple Text Oriented Messaging Protocol) is apart of the Spring 
	 * framework that determines the way the data is exchanged. STOMP rules 
	 * and formatting can help deliver messages to particular users and user 
	 * channels
	 */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    /**
     * Routes messages from one user to another.
     * 
     * Line 1: Routes messages whose destination begins with "/app" to
     *	       message-handling methods.
     * Line 2: Routes messages to the message broker to broadcast to clients
     *         subscribed to a particular client.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
    	// Line 1
        registry.setApplicationDestinationPrefixes("/app");
        // Line 2
        registry.enableSimpleBroker("/topic");
    }
}