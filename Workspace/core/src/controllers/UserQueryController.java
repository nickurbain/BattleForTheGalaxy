package controllers;

import java.net.UnknownHostException;

import battle.galaxy.LoginScreen;
import battle.galaxy.MainMenu;
import data.JsonHeader;
import data.UserQueryData;
import master.classes.MasterScreen;

public class UserQueryController extends MasterScreen {

	private static String player;

	/**
	 * Makes a call to the server to check if the user exists in the database
	 * 
	 * @param id
	 * @param pass
	 * @throws UnknownHostException
	 */
	public static void login(final String id, final String pass) {

		// Create Login object
		UserQueryData login = new UserQueryData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_LOGIN, id, pass);
		if (((String) game.getDataController().sendToServerWaitForResponse(login, true)).contains("Validated")) {
			setUser(id);
			try {
				game.setScreen(new MainMenu());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Invalid user, please try again or register as a new user");
			try {
				game.setScreen(new LoginScreen());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

	public static void registration(String user, String pass) {

		UserQueryData register = new UserQueryData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_REGISTRATION, user, pass);

		if (((String) game.getDataController().sendToServerWaitForResponse(register, true))
				.contains("User added successfully")) {
				try {
					game.setScreen(new LoginScreen());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			} 
		}
	

	public static String getUser() {
		return player;
	}

	public static void setUser(String user) {
		player = user;
	}
}
