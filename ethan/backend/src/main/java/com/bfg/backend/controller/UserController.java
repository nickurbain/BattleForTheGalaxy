package com.bfg.backend.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bfg.backend.model.User;
import com.bfg.backend.model.UserRepository;

@Controller // This means that this class is a Controller
//@RequestMapping(path = "/bfg") // This means URL's start with /demo (after Application path)
public class UserController {
	@Autowired // This means to get the bean called userRepository
				// Which is auto-generated by Spring, we will use it to handle the data
	private UserRepository userRepository;

	
	
	@MessageMapping("/bfg/ood")
	@SendTo("/topic/response")
	@GetMapping(path = "/ood") // Map ONLY GET Requests
	public User oodNewUser(User user) {
		
		System.out.println("username: " + user.getName() + ", user_pass: " + user.getPass());
		
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request
		User n = new User();
		n.setName(user.getName());
		n.setPass(user.getPass());
		userRepository.save(n);
		return n;
	}
	
	@MessageMapping("/add")
//	@GetMapping(path = "/add") // Map ONLY GET Requests
	public @ResponseBody String addNewUser(@RequestParam String user_name, @RequestParam String user_pass) {
		
		System.out.println("username: " + user_name + ", user_pass: " + user_pass);
		
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request
		User n = new User();
		n.setName(user_name);
		n.setPass(user_pass);
		userRepository.save(n);
		return "Saved";
	}

	@MessageMapping("/all")
//	@GetMapping(path = "/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		System.out.println("HERE IN GETALLLUSERS!!!");
		
//		System.out.println("LINKED!!");
		// This returns a JSON or XML with the users
		return userRepository.findAll();

	}

	@GetMapping(path = "/user")
	public @ResponseBody User getUserbyId(Long id) {
		// TODO
//		linkTo(methodOn(UserController.class).getAllUsers());
		return userRepository.findOne(id);
	}

	@GetMapping(path = "/login")
	@ResponseBody
	public String greetingJson(HttpEntity<String> httpEntity) {
		System.out.println("HERE IN LOGIN!!!");
		String json = httpEntity.getBody();
		System.out.println("Json from client: " + httpEntity.getBody());
		// json contains the plain json string
		return json;
	}
	
	@GetMapping("/welcome")
	public String welcome(User user) {
		return "Welcome";
	}


}

/*
 * 
 * /** OLD STUFF SimpMessagingTemplate message;
 * 
 * @Autowired UserRepository userRepo;
 * 
 * @MessageMapping("/login") public void login(String username, String password)
 * throws Exception { message.convertAndSendToUser(username, "/login", "OK"); //
 * String s = "OK"; // return s; }
 */
