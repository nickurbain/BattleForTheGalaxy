package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import master.classes.MasterScreen;

public class RegistrationScreen extends MasterScreen {

	private Label title;
	private TextField userName, password, confirm_password;
	private Table RegistrationMenu, buttons;

	public RegistrationScreen() throws UnknownHostException {
		super(game, "Login.jpg", "clean-crispy-ui.json");
		
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
				game.setScreen(new LoginScreen(game));
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
				if (name.equals("REGISTER USER")) {
					String id = userName.getText();
					String pass = password.getText();
					String c_pass = confirm_password.getText();
					System.out.println("Passwords equal?: " + pass.equals(c_pass));
					if (pass.equals(c_pass) && game.dataController.registration(id, pass)) {
						try {
							game.setScreen(new LoginScreen(game));
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
					} else if(!pass.equals(c_pass)) {
						System.out.println("Dude! Your passwords do not match");
						Dialog dialog = new Dialog("Password error", game.skin) {
							public void result(Object obj) {
								remove();
							}
						};
						dialog.text("Dude! Your passwords do not match");
						dialog.button("OK", true);
						dialog.key(Keys.ENTER, true);
						dialog.show(stage);
					} else {
						System.out.println("Registration Screen - ERROR: Connection Failed");
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

				} else if (name.equals("RETURN TO LOGIN")) {
					try {
						game.setScreen(new LoginScreen(game));
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

				if (type.equals("password")) {
					field.setPasswordMode(true);
					field.setPasswordCharacter('*');
				}
			}
		});
		return field;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}