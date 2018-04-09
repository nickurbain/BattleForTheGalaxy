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
import battle.galaxy.MainMenu;
import battle.galaxy.RegistrationScreen;
import data.DataController;
import data.JsonHeader;
import data.LoginData;
import data.RegistrationData;
import master.classes.MasterScreen;

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
	
	/*public TextButton setPassword(TextButton button) {
		
		return null;
	}*/
	
	/**
	 * Used for logging a user into the server, called from SplashScreen
	 */
	/*public boolean login(String user, String pass) {
		
		if(client.isOpen()) {
			
			
			// HARD CODED TO JOIN A MATCH WHEN LOGIN IS CALLED
			//client.send("{jsonOrigin:1,jsonType:12}");
			System.out.println("DC.login TX: sent a Client|JoinMatch Json");
			
			
			// LOGIN IS SUPPOSED TO BE CALLED AT THE SPLASHSCREEN BUT THIS IS FOR DEBUGGING THE SERVER MATCHES
			client.send(game.json.toJson(login));

			try {
				Thread.sleep(2000);
				parseRawData();
				if(authorized) {
					return true;
				}else {
					return false;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}*/
	
	/*public boolean registration(String user, String pass) {
		RegistrationData register = new RegistrationData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_REGISTRATION, user, pass);
		
		System.out.println("DataController ~ Client is open?: " + client.isOpen());
		
		if(client.isOpen()) {	
			
			System.out.println("DataController ~ JSON: " + register.toString());
			client.send(game.json.toJson(register));
			
			try {
				Thread.sleep(2000);
				parseRawData();
				if(authorized) {
					return true;
				}else {
					System.out.println("DataController: Not authorized");
					return false;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}*/
}
