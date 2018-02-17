package com.bfg.backend;

import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
	
	private static final String VIEWS_USER_CREATE_OR_UPDATE_FROM = "/user/createOrUpdateUserFrom";
	private final UserRepository users;
	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	public UserController(UserRepository userService) {
		this.users = userService;
	}
	
	public void setAllowedFields(WebDataBinder dataBinder) {}

	@GetMapping("/users/new")
	public String initCreationFrom(Map<String, Object> model) {
		logger.info("Entered into Controller Layer!");
		User user = new User();
		model.put("user", user);
		return VIEWS_USER_CREATE_OR_UPDATE_FROM;
	}
	
	@PostMapping("/users/new")
	public String processCreationForm(@Valid User user, BindingResult result) {
		if(result.hasErrors()) {
			return VIEWS_USER_CREATE_OR_UPDATE_FROM;
		}
		else {
			this.users.save(user);
			return "redirect:/user/" + user.getId();
		}
	}
	
}










/** OLD STUFF
SimpMessagingTemplate message;
@Autowired
UserRepository userRepo;
@MessageMapping("/login")
public void login(String username, String password) throws Exception {
	message.convertAndSendToUser(username, "/login", "OK");	
//	String s = "OK";
//	return s;
}
*/


  /**
   * GET /create  --> Create a new user and save it in the database.
   */
//  @RequestMapping("/create")
//  @ResponseBody
//  public String create(String email, String name) {
//    String userId = "";
//    try {
//      User user = new User(email, name);
//      userDao.save(user);
//      userId = String.valueOf(user.getId());
//    }
//    catch (Exception ex) {
//      return "Error creating the user: " + ex.toString();
//    }
//    return "User succesfully created with id = " + userId;
//  }
//  
//  /**
//   * GET /delete  --> Delete the user having the passed id.
//   */
//  @RequestMapping("/delete")
//  @ResponseBody
//  public String delete(long id) {
//    try {
//      User user = new User();
//      userDao.delete(user);
//    }
//    catch (Exception ex) {
//      return "Error deleting the user:" + ex.toString();
//    }
//    return "User succesfully deleted!";
//  }
//  
//  /**
//   * GET /get-by-email  --> Return the id for the user having the passed
//   * email.
//   */
//  @RequestMapping("/get-by-id")
//  @ResponseBody
//  public String getById(Integer id) {
//    String userId = "";
//    try {
//      User user = userDao.findById(id);
//      userId = String.valueOf(user.getId());
//    }
//    catch (Exception ex) {
//      return "User not found";
//    }
//    return "The user id is: " + userId;
//  }
//  
//  /**
//   * GET /update  --> Update the email and the name for the user in the 
//   * database having the passed id.
//   */
//  @RequestMapping("/update")
//  @ResponseBody
//  public String updateUser(long id, String password, String name) {
//    try {
//      User user = userDao.findOne(id);
//      user.setPass(password);
//      user.setName(name);
//      userDao.save(user);
//    }
//    catch (Exception ex) {
//      return "Error updating the user: " + ex.toString();
//    }
//    return "User succesfully updated!";
//  }
//
//  // Private fields
//
//  @Autowired
//  private UserDao userDao;






/*
 *  TODO:
 *  	Get client IP or individual info.
 *  
 * 
 * 
 * 
 * 
 */
