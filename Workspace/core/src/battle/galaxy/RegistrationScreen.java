package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
//import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import controllers.UserQueryController;
import master.classes.MasterButtons;
import master.classes.MasterScreen;

/**
 * The screen that allows you to register for the game. Extends the MasterScreen
 */
public class RegistrationScreen extends MasterScreen {

	private Label userLabel, passLabel, cPassLabel;
	private TextField userName, password, confirm_password;
	private Table RegistrationMenu, buttons;
	private Texture bigTitleTexture;

	/**
	 * Sets up the registration screen
	 * 
	 * @throws UnknownHostException
	 */
	public RegistrationScreen() throws UnknownHostException {

		super("Login.jpg", "clean-crispy-ui.json");

		bigTitleTexture = new Texture(Gdx.files.internal("BFTG_title.png"));
		bigTitleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// Table for registration menu
		RegistrationMenu = new Table();
		RegistrationMenu.setWidth(stage.getWidth());
		RegistrationMenu.align(Align.top);
		RegistrationMenu.setPosition(0, stage.getHeight());

		// Buttons to add to table
		buttons = new Table();
		buttons.add(Button(skin, "REGISTER USER")).fill();
		buttons.add(Button(skin, "RETURN TO LOGIN")).fill();

		// Labels for this LoginScreen
		userLabel = new Label("User Name", skin);
		userLabel.setFontScale(1.5f);
		userLabel.setColor(Color.BLACK);
		passLabel = new Label("Password", skin);
		passLabel.setFontScale(1.5f);
		passLabel.setColor(Color.BLACK);
		cPassLabel = new Label("Confirm Password", skin);
		cPassLabel.setFontScale(1.5f);
		cPassLabel.setColor(Color.BLACK);

		// Textboxes for the registration screen
		userName = TextBox(skin, "userName", null);
		password = TextBox(skin, "password", null);
		confirm_password = TextBox(skin, "confirm_password", null);

		// Add all pieces to the registration table
		RegistrationMenu.add(userLabel).padTop(70);
		RegistrationMenu.row();
		RegistrationMenu.add(userName).fill();
		RegistrationMenu.row();
		RegistrationMenu.add(passLabel);
		RegistrationMenu.row();
		RegistrationMenu.add(password).fill();
		RegistrationMenu.row();
		RegistrationMenu.add(cPassLabel);
		RegistrationMenu.row();
		RegistrationMenu.add(confirm_password).fill();
		RegistrationMenu.row();
		RegistrationMenu.add(buttons).padTop(10);

		stage.addActor(RegistrationMenu);
		Gdx.input.setInputProcessor(stage);
	}

	/**
	 * Renders the registration screen
	 */
	public void render(float delta) {
		// super.render(delta);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);

		game.getBatch().begin();
		game.getBatch().draw(background, 0, 0);
		game.getBatch().draw(bigTitleTexture, Gdx.graphics.getWidth() / 2 - bigTitleTexture.getWidth() / 2,
				Gdx.graphics.getHeight() / 10);
		game.getBatch().end();

		stage.act();
		stage.draw();

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
	public ImageTextButton Button(Skin skin, final String name) {

		ImageTextButtonStyle style = MasterButtons.setButtonStyle("SansSerif.fnt", "button.png", 0.7f);
		ImageTextButton button = new ImageTextButton(name, style);
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

		TextFieldStyle style = MasterButtons.setTextFieldStyle("SansSerif.fnt", "text_field.png", 1.5f, Color.BLACK);
		final TextField field = new TextField(message, style);
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