package controllers;

import java.net.UnknownHostException;

import battle.galaxy.LoginScreen;
import battle.galaxy.MainMenu;
import data.JsonHeader;
import data.UserQueryData;
import master.classes.MasterScreen;

public class UserQueryController extends MasterScreen {

	static DataController dc = new DataController(game);
	private static String player;

	/**
	 * Makes a call to the server to check if the user exists in the database
	 * 
	 * @param id
	 * @param pass
	 * @throws UnknownHostException
	 */
	public static void login(final String id, final String pass) throws UnknownHostException {

		if (!id.equals(null) && !pass.equals(null)) {
			// Create Login object
			UserQueryData login = new UserQueryData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_LOGIN, id, pass);
			// System.out.println(dc.getJsonController().dataToJson(login));
			if (((String) dc.sendToServerWaitForResponse(login, true)).contains("Validated")) {
				setUser(id);
				game.setScreen(new MainMenu());
			} else {
				System.out.println("Invalid user, please try again or register as a new user");
				game.setScreen(new LoginScreen());
			}
		}
	}

	public static void registration(String user, String pass) {

		UserQueryData register = new UserQueryData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_REGISTRATION, user, pass);
		// RegistrationData register = new RegistrationData(JsonHeader.ORIGIN_CLIENT,
		// JsonHeader.TYPE_REGISTRATION, user, pass);
		// System.out.println(dc.getJsonController().dataToJson(register));
		// System.out.println("DataController ~ Client is open?: " + client.isOpen());
		if (!user.equals(null) && !pass.equals(null)) {

			if (((String) dc.sendToServerWaitForResponse(register, true)).contains("User added successfully")) {
				try {
					game.setScreen(new LoginScreen());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("User name is taken, please try another");
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
