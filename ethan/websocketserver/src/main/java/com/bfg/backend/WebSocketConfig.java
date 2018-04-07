package com.bfg.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

//import com.bfg.backend.repository.UserRepository;

/**
 * Configures the websocket to listen for things with '/bfg', and 
 * send incoming messages to the SocketHandler to be handled.
 * 
 * @author emball
 *
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	
	@Autowired
	private SocketHandler socketHandler;

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// Used to be /name here
		registry.addHandler(socketHandler, "/bfg");
	}
}