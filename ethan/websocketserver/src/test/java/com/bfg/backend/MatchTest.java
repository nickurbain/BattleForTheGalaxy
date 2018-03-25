package com.bfg.backend;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.repository.UserRepository;

public class MatchTest {
	@Autowired
	private UserRepository userRepository;
	
	private final HttpHeaders headers = new HttpHeaders();
	
	WebSocketSession session;
	
	@Test
	public void testAddPlayer() {
		
	}
	
//	Mockito.mock();
}
