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
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class MainMenu implements Screen {

	private BattleForTheGalaxy game;
	private OrthographicCamera camera;
	private Texture bg_texture;
	private Sprite bg_sprite;
	private Stage stage;

	private Label title;

	private Table mainMenu, options, gameModes, chat; // Main table
	private Skin skin;
	private TextButton logout;

	/**
	 * 
	 * @param incomingGame
	 * @throws UnknownHostException
	 */
	public MainMenu(BattleForTheGalaxy incomingGame) throws UnknownHostException {
		this.game = incomingGame;
		stage = new Stage();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1600, 900);  // false => y-axis 0 is bottom-left
		
		skin = incomingGame.skin;
		
		bg_texture = new Texture(Gdx.files.internal("Login.jpg"));
		bg_texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother textures
		bg_sprite = new Sprite(bg_texture);
		
		mainMenu = new Table();
		mainMenu.setWidth(stage.getWidth());
		mainMenu.align(Align.top);
		mainMenu.setPosition(0, Gdx.graphics.getHeight());

		options = new Table();
		options.align(Align.right);
		String[] optionNames = { "ACCOUNT", "GALACTIC SHOP", "HANGER", "FACTION", "ALLIANCE", "CREW", "EVENTS" };

		gameModes = new Table();
		gameModes.align(Align.left|Align.top);
		String[] modeNames = {"ALL OUT\nDEATH MATCH", "ALLIANCE\nDEATH MATCH", "FACTION\nBATTLE", "TEAM\nDEATH MATCH",
				"CONSTRUCTION", "MINING"};

		chat = new Table();
		chat.align(Align.left);
		String[] chatNames = {"Global", "Team", "Private"};
		
		TextArea chatWindow = new TextArea("Hello World", skin);
		
		// Shows table lines for debugging
		//mainMenu.setDebug(true);
		
		logout = button("LOGOUT", skin);
		logout.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					try {
						game.setScreen(new LoginScreen(game));
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		});
		
		// Labels
		title = new Label("BATTLE FOR THE GALAXY", skin);
		title.setFontScale(4f);
		
		
		// Add all functions to the main menu
		mainMenu.add(title).pad(15).expandX();
		mainMenu.add(logout).pad(15);
		mainMenu.row();
		mainMenu.add(modeButtons(gameModes, skin, modeNames)).padTop(50).left().top();
		mainMenu.add(menuButtons(options, skin, optionNames)).padTop(50).right().top();
		mainMenu.row();
		mainMenu.add(chatButtons(chat, skin, chatNames)).left().bottom();
		mainMenu.row();
		mainMenu.add(chatWindow).left().fill().height(150);

		stage.addActor(mainMenu);

		Gdx.input.setInputProcessor(stage);
		//Set cursor back to default
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.05F, 0.05F, 0.05F, 0.05F);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.batch.draw(bg_texture, 0, 0);
		game.batch.end();

		// Stage
		stage.draw();
	}

	public TextButton button(String name, Skin skin) {
		return new TextButton(name, skin);
	}

	public Table menuButtons(Table table, Skin skin, String[] names) {

		for (int i = 0; i < names.length; i++) {
			TextButton button = new TextButton(names[i], skin);
			final int index = i;
			final String bName = names[index];
			button.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					if (bName.equals("ACCOUNT")) {
						// System.out.println("ACCOUNT has been pressed");
						try {
							game.dataController.joinMatch();
							game.setScreen(new MatchStatsScreen(game));
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
					} else if (bName.equals("GALACTIC SHOP")) {
						System.out.println("GALACTIC SHOP button pushed");
					} else if (bName.equals("HANGER")) {
						System.out.println("HANGER button pushed");
						game.setScreen(new HangerScreen(game));
					} else if (bName.equals("FACTION")) {
						System.out.println("FACTION button pushed");
					} else if (bName.equals("ALLIANCE")) {
						System.out.println("ALLIANCE button pushed");
					} else if (bName.equals("CREW")) {
						System.out.println("CREW button pushed");
					} else if (bName.equals("EVENTS")) {
						System.out.println("EVENTS button pushed");
					}
				}
			});
			table.add(button).fill().padBottom(10).padLeft(10).padRight(10).row();
		}

		return table;
	}

	public Table modeButtons(Table table, Skin skin, final String[] names) {

		for (int i = 0; i < names.length; i += 2) {
			TextButton button = new TextButton(names[i], skin);
			final int index = i;
			button.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					String bName = names[index];
					if (bName.equals("ALL OUT\nDEATH MATCH")) {

						// HARDCODED LOGIN AUTHENTICATION //
						// game.dataController.login("finn1", "bork1");

						game.setScreen(new GameScreen(game));
						dispose();
					} else if (bName.equals("FACTION\nBATTLE")) {
						System.out.println("FACTION BATTLE button pushed");
					} else if (bName.equals("CONSTRUCTION")) {
						System.out.println("CONSTRUCTION button pushed");
					}
				}
			});
			table.add(button).fill().padBottom(10).padLeft(10).padRight(10);

			TextButton button2 = new TextButton(names[i + 1], skin);
			final int index2 = i + 1;
			button2.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					String b2Name = names[index2];
					if (b2Name.equals("ALLIANCE\nDEATH MATCH")) {
						System.out.println("ALLIANCE DEATH MATCH button pushed");
					} else if (b2Name.equals("TEAM\nDEATH MATCH")) {
						System.out.println("TEAM DEATH MATCH button pushed");
					} else if (b2Name.equals("MINING")) {
						System.out.println("MINING button pushed");
					}
				}
			});
			table.add(button2).fill().padBottom(10).padLeft(10).padRight(10).row();
		}

		return table;
	}

	public Table chatButtons(Table table, Skin skin, String[] names) {

		for (int i = 0; i < names.length; i++) {
			table.add(new TextButton(names[i], skin)).width(150);
		}

		return table;
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