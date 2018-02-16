package com.bfg.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.bfg.backend.model.User;
import com.bfg.backend.repository.UserRepository;

@Controller
public class UserController {
	
	@Autowired
	UserRepository userRepo;
	
	@MessageMapping("/login")
	@SendTo("/login/response")
	public String send() throws Exception {
		String s = "OK";
		return s;
	}
}



/*
 *  TODO:
 *  	Get client IP or individual info.
 *  
 * 
 * 
 * 
 * 
 */
