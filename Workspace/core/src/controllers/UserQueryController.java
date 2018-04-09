package controllers;

import java.net.UnknownHostException;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import battle.galaxy.BattleForTheGalaxy;
import battle.galaxy.LoginScreen;
import battle.galaxy.MainMenu;
import battle.galaxy.RegistrationScreen;
import data.DataController;
import data.JsonHeader;
import data.LoginData;
import data.RegistrationData;
import master.classes.MasterScreen;
import sun.util.logging.resources.logging;

public class UserQueryController extends MasterScreen {

	static DataController dc = new DataController(getGame());
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
		
		System.out.println("In userquery ~ User Name: " + id + ", Password: " + pass);
		
		// Create Login object
		LoginData login = new LoginData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_LOGIN, id, pass);
		System.out.println(login.getJsonType());
		System.out.println(dc.getJsonController().dataToJson(login));
		if (dc.sendToServerWaitForResponse(login).contains("Validated")) {
			try {
				getGame().setScreen(new MainMenu());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Invalid user, please try again or register as a new user");
		}	
	}
	
	public static void registration(String user, String pass) {
		
		LoginData register = new LoginData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_REGISTRATION, user, pass);
		//RegistrationData register = new RegistrationData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_REGISTRATION, user, pass);
		System.out.println(dc.getJsonController().dataToJson(register));
		//System.out.println("DataController ~ Client is open?: " + client.isOpen());
		
		if (dc.sendToServerWaitForResponse(register).contains("User added successfully")) {
			try {
				getGame().setScreen(new LoginScreen());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("User name is taken, please try another");
		}
	}
}