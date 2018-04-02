package battle.galaxy;

import java.net.UnknownHostException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import master.classes.MasterScreen;

public class LoginScreen extends MasterScreen {

	private Label title;
	private TextField userName, password;
	private Table loginMenu, buttons;

	public LoginScreen(BattleForTheGalaxy game) throws UnknownHostException {
		super(game, "Login.jpg", "clean-crispy-ui.json");
			
		loginMenu = new Table();
		loginMenu.setWidth(stage.getWidth());
		loginMenu.align(Align.top);
		loginMenu.setPosition(0, stage.getHeight());

		buttons = new Table();
		buttons.add(Button(skin, "LOGIN")).width(100).height(30);
		buttons.add(Button(skin, "REGISTER")).width(100).height(30);
		
		title = new Label("Battle for the Galaxy", skin);
		title.setFontScale(4f);

		userName = TextBox(skin, "userName", "User Name");
		password = TextBox(skin, "password", "Password");
		
		loginMenu.add(title).padTop((stage.getHeight()/2) - 150);
		loginMenu.row();
		loginMenu.add(userName).padTop(20).width(200).height(40);
		loginMenu.row();
		loginMenu.add(password).padTop(20).width(200).height(40);
		loginMenu.row();
		loginMenu.add(buttons).padTop(10);

		stage.addActor(loginMenu);
		Gdx.input.setInputProcessor(stage);
		
	}

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

	public TextButton Button(Skin skin, final String name) {

		TextButton button = new TextButton(name, skin);
		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (name.equals("LOGIN")) {
					System.out.println("User Name: " + userName.getText() + ", Password: " + password.getText());
					String id = userName.getText();
					String pass = password.getText();

					// Try to make client-server connection when Login button is clicked
					if (game.dataController.login(id, pass)) {
						try {
							game.setScreen(new MainMenu());
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("SplashScreen - ERROR: Connection Failed");
						Dialog dialog = new Dialog("Connection Failed", game.skin) {
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
		return field;
	}
}