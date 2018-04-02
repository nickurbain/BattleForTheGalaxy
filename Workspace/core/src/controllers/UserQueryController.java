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

	/*public UserQueryController(BattleForTheGalaxy game) {
		super(game);
		// TODO Auto-generated constructor stub
	}*/

	/**
	 * 
	 * @param button
	 * @param skin
	 * @param user_name
	 * @param password
	 * @return
	 */
	public TextButton login(TextButton button, final Skin skin, final TextField user_name, final TextField password) {
		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				//super.clicked(event, x, y);
				//if (name.equals("LOGIN")) {
					//System.out.println("User Name: " + user.getText() + ", Password: " + pass.getText());
					String id = user_name.getText();
					String pass = password.getText();

					LoginData login = new LoginData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_LOGIN, id, pass);
					// Create Login object
					
					// String response = sendToServerWait(Login Obj)
					
					// If Response == validated
					
					
					
					// Try to make client-server connection when Login button is clicked
					if (login(id, pass)) {
						try {
							getGame().setScreen(new MainMenu());
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("SplashScreen - ERROR: Connection Failed");
						Dialog dialog = new Dialog("Connection Failed", skin) {
							public void result(Object obj) {
								remove();
							}
						};
						dialog.text("Server couldn't be reached");
						dialog.button("OK", false);
						dialog.key(Keys.ENTER, false);
						dialog.show(stage);
					}

				} else if (name.equals("REGISTER")) {
					try {
						getGame().setScreen(new RegistrationScreen());
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					System.out.println("Register button pushed");
				}
			}
		});
		return null;
		
	}
	
	public TextButton setPassword(TextButton button) {
		
		return null;
		
	}
	
	/**
	 * Used for logging a user into the server, called from SplashScreen
	 */
	public boolean login(String user, String pass) {
		
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
	}
	
	public boolean registration(String user, String pass) {
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
	}
}
