package controllers;

import java.net.UnknownHostException;

import battle.galaxy.LoginScreen;
import battle.galaxy.MainMenu;
import data.JsonHeader;
import data.UserQueryData;
import master.classes.MasterScreen;

public class UserQueryController extends MasterScreen {

	private static String player, user_alliance;

	/**
	 * Makes a call to the server to check if the user exists in the database
	 * 
	 * @param name
	 * @param pass
	 * @throws UnknownHostException
	 */
	public static void login(final String name, final String pass) {

		// Create Login object
		UserQueryData login = new UserQueryData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_LOGIN, name, pass);
		String response = (String) game.getDataController().sendToServerWaitForResponse(login, true);
		if (response.contains("Validated")) {
			String allianceName = game.getDataController().getJsonController().getJsonReader().parse(response).getString("alliance");
			setUser(name);
			setAlliance(allianceName);
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
	
	public static String getAlliance() {
		return user_alliance;
	}
	
	public static void setAlliance(String alliance) {
		user_alliance = alliance;
	}
}
