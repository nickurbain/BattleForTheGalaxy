package battle.galaxy;

import java.net.UnknownHostException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
 * Creates a Login screen used to authenticate the user. Extends the
 * MasterScreen
 */
public class LoginScreen extends MasterScreen {

	private Label title;
	private TextField userName, password;
	private Table loginMenu, buttons;

	/**
	 * Creates a login screen for the user to interact with
	 * 
	 * @throws UnknownHostException
	 */
	public LoginScreen() throws UnknownHostException {
		// Calls the MasterScreen
		super("Login.jpg", "clean-crispy-ui.json");

		// A table for this LoginScreen
		loginMenu = new Table();
		loginMenu.setWidth(stage.getWidth());
		loginMenu.align(Align.top);
		loginMenu.setPosition(0, stage.getHeight());

		// Buttons for this LoginScreen
		buttons = new Table();
		buttons.add(Button(skin, "LOGIN")).width(100).height(30);
		buttons.add(Button(skin, "REGISTER")).width(100).height(30);

		// The tilte for this LoginScreen
		title = new Label("Battle for the Galaxy", skin);
		title.setFontScale(4f);

		// TextBoxes for this LoginScreen
		userName = TextBox(skin, "userName", "Username");
		password = TextBox(skin, "password", "Password");

		// Add all elements to this LoginScreen
		loginMenu.add(title).padTop((stage.getHeight() / 2) - 150);
		loginMenu.row();
		loginMenu.add(userName).padTop(20).width(200).height(40);
		loginMenu.row();
		loginMenu.add(password).padTop(20).width(200).height(40);
		loginMenu.row();
		loginMenu.add(buttons).padTop(10);

		stage.addActor(loginMenu);
		Gdx.input.setInputProcessor(stage);
	}

	/**
	 * Renders the LoginScreen
	 */
	public void render(float delta) {
		super.render(delta);

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			try {
				game.setScreen(new MainMenu());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A generic method that creates a button for this LoginScreen
	 * 
	 * @param skin
	 *            The skin used by the buttons
	 * @param name
	 *            The name for this button
	 * @return the TextButton
	 */
	public TextButton Button(Skin skin, final String name) {

		TextButton button = new TextButton(name, skin);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				if (name.equals("LOGIN") && (userName.getText() != "" && userName.getMessageText() != "Username") && password.getMessageText() != "Password") {

					System.out.println("User Name: " + userName.getText() + ", Password: " + password.getText());
					if (!userName.getText().contains(" ") && !password.getText().contains(" ")) {
						UserQueryController.login(userName.getText(), password.getText());
					}

				} else if (name.equals("REGISTER")) {

					try {
						game.setScreen(new RegistrationScreen());
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}

					System.out.println("Register button pushed");
				}
			}
		});

		return button;

	}

	/**
	 * A generic method that generates a text field for this LoginScreen
	 * 
	 * @param skin
	 *            The skin used by this text field
	 * @param type
	 *            The type of text field to be generated
	 * @param message
	 *            The message displayed in this text field. Disappears when clicked
	 *            on
	 * @return the TextField
	 */
	public TextField TextBox(Skin skin, final String type, final String message) {

		final TextField field = new TextField(message, skin);
		field.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				field.setText("");

				if (type.equals("password")) {
					field.setPasswordMode(true);
					field.setPasswordCharacter('*');
				}
			}
		});
		field.setFocusTraversal(true);
		field.addListener(new InputListener() {
			public boolean keyDown(InputEvent event, int keycode) {
				if(keycode == Keys.ENTER) {
					// login normally
					System.out.println("User Name: " + userName.getText() + ", Password: " + password.getText());
					if (!userName.getText().contains(" ") && !password.getText().contains(" ")) {
						UserQueryController.login(userName.getText(), password.getText());
					}
					return true;
				} else {
					return false;
				}

			}
		});
		return field;
	}
}