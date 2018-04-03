package com.bfg.backend;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.repository.UserRepository;

import junit.framework.TestCase;

public class MatchTest extends TestCase {
	@Autowired
	private UserRepository userRepository;
	
	private final HttpHeaders headers = new HttpHeaders();
	
	WebSocketSession session = Mockito.mock(WebSocketSession.class);
	
	@Test
	public void testAddPlayer() {
	
		
//		assertEquals();
	}
}
