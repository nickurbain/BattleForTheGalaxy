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

/**
 * The screen that allows you to register for the game. Extends the MasterScreen
 */
public class RegistrationScreen extends MasterScreen {

	private Label title;
	private TextField userName, password, confirm_password;
	private Table RegistrationMenu, buttons;

	/**
	 * Sets up the registration screen
	 * 
	 * @throws UnknownHostException
	 */
	public RegistrationScreen() throws UnknownHostException {

		super("Login.jpg", "clean-crispy-ui.json");

		// Table for registration menu
		RegistrationMenu = new Table();
		RegistrationMenu.setWidth(stage.getWidth());
		RegistrationMenu.align(Align.top);
		RegistrationMenu.setPosition(0, stage.getHeight());

		// Buttons to add to table
		buttons = new Table();
		buttons.add(Button(skin, "REGISTER USER")).fill();
		buttons.add(Button(skin, "RETURN TO LOGIN")).fill();

		// Title for the screen
		title = new Label("Battle for the Galaxy\nRegistration", skin);
		title.setFontScale(4f);

		// Textboxes for the registration screen
		userName = TextBox(skin, "userName", "User Name");
		password = TextBox(skin, "password", "Password");
		confirm_password = TextBox(skin, "confirm_password", "Confirm Password");

		// Add all pieces to the registration table
		RegistrationMenu.add(title).padTop((stage.getHeight() / 2) - 150);
		RegistrationMenu.row();
		RegistrationMenu.add(userName).padTop(20).width(270).height(40);
		RegistrationMenu.row();
		RegistrationMenu.add(password).padTop(20).width(270).height(40);
		RegistrationMenu.row();
		RegistrationMenu.add(confirm_password).padTop(20).width(270).height(40);
		RegistrationMenu.row();
		RegistrationMenu.add(buttons).padTop(10);

		stage.addActor(RegistrationMenu);
		Gdx.input.setInputProcessor(stage);
	}

	/**
	 * Renders the registration screen
	 */
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

	/**
	 * A generic method to create text buttons for the registration screen
	 * 
	 * @param skin
	 *            The skin to use for the registration buttons
	 * @param name
	 *            The name of the buttons
	 * @return the text button
	 */
	public TextButton Button(Skin skin, final String name) {

		TextButton button = new TextButton(name, skin);
		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				String pass = password.getText();
				String c_pass = confirm_password.getText();

				if (pass.equals(c_pass) && name.equals("REGISTER USER")) {
					System.out.println("User Name: " + userName.getText() + ", Password: " + password.getText());
					if (!userName.getText().contains(" ") || !password.getText().contains(" ")) {
						UserQueryController.registration(userName.getText(), pass);
					}
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

	/**
	 * A generic method to generate a text field used by the registration screen
	 * 
	 * @param skin
	 *            The skin used by the text fields
	 * @param type
	 *            The type of text field created
	 * @param message
	 *            The message displayed in the field. Disappears once box is clicked
	 *            in.
	 * @return the text field for the registration screen.
	 */
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