package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import controllers.UserQueryController;
import master.classes.MasterScreen;

public class RegistrationScreen extends MasterScreen {

	private Label title;
	private TextField userName, password, confirm_password;
	private Table RegistrationMenu, buttons;

	public RegistrationScreen() throws UnknownHostException {

		super("Login.jpg", "clean-crispy-ui.json");

		RegistrationMenu = new Table();
		RegistrationMenu.setWidth(stage.getWidth());
		RegistrationMenu.align(Align.top);
		RegistrationMenu.setPosition(0, stage.getHeight());

		buttons = new Table();
		buttons.add(Button(skin, "REGISTER USER")).fill();
		buttons.add(Button(skin, "RETURN TO LOGIN")).fill();

		title = new Label("Battle for the Galaxy\nRegistration", skin);
		title.setFontScale(4f);

		userName = TextBox(skin, "userName", "User Name");
		password = TextBox(skin, "password", "Password");
		confirm_password = TextBox(skin, "confirm_password", "Confirm Password");

		RegistrationMenu.add(title).padTop((stage.getHeight() / 2) - 150);
		RegistrationMenu.row();
		RegistrationMenu.add(userName).padTop(20);
		RegistrationMenu.row();
		RegistrationMenu.add(password).padTop(20);
		RegistrationMenu.row();
		RegistrationMenu.add(confirm_password).padTop(20);
		RegistrationMenu.row();
		RegistrationMenu.add(buttons).padTop(10);

		stage.addActor(RegistrationMenu);
		Gdx.input.setInputProcessor(stage);
	}

	public void render(float delta) {
		super.render(delta);

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			try {
				game.setScreen(new LoginScreen());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

	public TextButton Button(Skin skin, final String name) {

		TextButton button = new TextButton(name, skin);
		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				String pass = password.getText();
				String c_pass = confirm_password.getText();

				if (pass.equals(c_pass) && name.equals("REGISTER USER")) {				
					System.out.println("User Name: " + userName.getText() + ", Password: " + password.getText());
					UserQueryController.registration(userName.getText(), pass);
				} else if (name.equals("RETURN TO LOGIN")) {
					try {
						game.setScreen(new LoginScreen());
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		return button;

	}

	public TextField TextBox(Skin skin, final String type, final String message) {

		final TextField field = new TextField(message, skin);
		field.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				field.setText("");

				if (type.equals("password") || type.equals("confirm_password")) {
					field.setPasswordMode(true);
					field.setPasswordCharacter('*');
				}
			}
		});
		return field;
	}
}