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
import master.classes.MasterScreen;

public class UserQueryController extends MasterScreen {

	/*public UserQueryController(BattleForTheGalaxy game) {
		super(game);
		// TODO Auto-generated constructor stub
	}*/

	public TextButton login(TextButton button, final Skin skin, final TextField user_name, final TextField password) {
		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				//super.clicked(event, x, y);
				//if (name.equals("LOGIN")) {
					//System.out.println("User Name: " + user.getText() + ", Password: " + pass.getText());
					String id = user_name.getText();
					String pass = password.getText();

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
}
