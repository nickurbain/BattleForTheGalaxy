package controllers;

import java.net.UnknownHostException;

import battle.galaxy.LoginScreen;
import battle.galaxy.MainMenu;
import data.JsonHeader;
import data.UserQueryData;
import master.classes.MasterScreen;

public class UserQueryController extends MasterScreen {
	
	private static String player;
	/*public UserQueryController(BattleForTheGalaxy game) {
		super(game);
		// TODO Auto-generated constructor stub
	}*/

	/**
	 * Makes a call to the server to check if the user exists in the database
	 * 
	 * @param id
	 * @param pass
	 */
	public static void login(final String id, final String pass) {
		
		//System.out.println("In userquery ~ User Name: " + id + ", Password: " + pass);
		
		// Create Login object
		UserQueryData login = new UserQueryData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_LOGIN, id, pass);
		//System.out.println(dc.getJsonController().dataToJson(login));
		if (((String) game.getDataController().sendToServerWaitForResponse(login, true)).contains("Validated")) {
			setUser(id);
			try {
				game.setScreen(new MainMenu());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Invalid user, please try again or register as a new user");
		}	
	}

	public static void registration(String user, String pass) {
		
		UserQueryData register = new UserQueryData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_REGISTRATION, user, pass);
		//RegistrationData register = new RegistrationData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_REGISTRATION, user, pass);
		//System.out.println(dc.getJsonController().dataToJson(register));
		//System.out.println("DataController ~ Client is open?: " + client.isOpen());
		
		if (((String) game.getDataController().sendToServerWaitForResponse(register, true)).contains("User added successfully")) {
			try {
				game.setScreen(new LoginScreen());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("User name is taken, please try another");
		}
	}
	
	public static String getUser() {
		return player;
	}
	
	public static void setUser(String user) {
		player = user;
	}
}
