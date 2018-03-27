package battle.galaxy;

import java.net.UnknownHostException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

public class LoginScreen implements Screen {

	private BattleForTheGalaxy game;
	private OrthographicCamera camera;
	private Texture bg_texture;
	private Sprite bg_sprite;
	private Stage stage;

	private Label title;
	private TextField userName, password;
	private Table loginMenu, buttons;
	private Skin skin;

	public LoginScreen(BattleForTheGalaxy incomingGame) throws UnknownHostException {
		this.game = incomingGame;
		stage = new Stage();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1600, 900); // false => y-axis 0 is bottom-left

		skin = incomingGame.skin;

		bg_texture = new Texture(Gdx.files.internal("Login.jpg"));
		bg_texture.setFilter(TextureFilter.Linear, TextureFilter.Linear); // smoother textures
		bg_sprite = new Sprite(bg_texture);

		loginMenu = new Table();
		loginMenu.setWidth(stage.getWidth());
		loginMenu.align(Align.top);
		loginMenu.setPosition(0, stage.getHeight());

		buttons = new Table();
		buttons.add(Button(skin, "LOGIN")).fill();
		buttons.add(Button(skin, "REGISTER")).fill();
		
		title = new Label("Battle for the Galaxy", skin);
		title.setFontScale(4f);

		loginMenu.add(title).padTop((stage.getHeight()/2) - 150);
		loginMenu.row();
		loginMenu.add(TextBox(skin, "userName", "User Name")).padTop(20);
		loginMenu.row();
		loginMenu.add(TextBox(skin, "password", "Password")).padTop(20);
		loginMenu.row();
		loginMenu.add(buttons).padTop(10);

		stage.addActor(loginMenu);
		Gdx.input.setInputProcessor(stage);

	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0.05F, 0.05F, 0.05F, 0.05F);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.draw(bg_texture, 0, 0);
		game.batch.end();

		stage.draw();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			try {
				game.setScreen(new MainMenu(game));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

	public TextButton Button(Skin skin, final String name) {

		TextButton button = new TextButton(name, skin);
		button.setWidth(200);
		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (name.equals("LOGIN")) {
					String id = userName.getText();
					String pass = password.getText();

					// Try to make client-server connection when Login button is clicked
					if (game.dataController.login(id, pass)) {
						try {
							game.setScreen(new MainMenu(game));
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
						game.setScreen(new RegistrationScreen(game));
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

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void hide() {

	}

}