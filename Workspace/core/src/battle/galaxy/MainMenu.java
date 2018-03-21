package battle.galaxy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenu implements Screen {

	private BattleForTheGalaxy game;
	private Stage stage;
	/**
	 * Skin is the information contained in the uiskin.json file
	 */
	private Skin skin;

	private Table mainMenu, options, gameModes, chat; // Main table

	//private SpriteBatch batch;
	private Sprite sprite;

	//@Override
	public MainMenu(BattleForTheGalaxy game) {

		Label L_title;
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage(new ScreenViewport());

		mainMenu = new Table();
		mainMenu.setWidth(stage.getWidth());
		mainMenu.align(Align.top);
		mainMenu.setPosition(0, Gdx.graphics.getHeight());

		options = new Table();
		options.align(Align.right);
		String[] optionNames = { "ACCOUNT", "GALACTIC SHOP", "HANGER", "FACTION", "ALLIANCE", "CREW", "EVENTS" };

		gameModes = new Table();
		gameModes.align(Align.left|Align.top);
		String[] modeNames = { "DEATH MATCH", "ALLIANCE\nDEATH MATCH", "FACTION BATTLE", "FREE_FOR_ALL",
				"CONSTRUCTION", "MINING" };

		chat = new Table();
		chat.align(Align.left);
		String[] chatNames = {"Global", "Team", "Private"};
		
		TextArea chatWindow = new TextArea("Hello World", skin);
		// Shows table lines
		mainMenu.setDebug(true);
		
		// Labels
		L_title = new Label("BATTLE FOR THE GALAXY", skin);

		// Add all functions to the main menu
		mainMenu.add(L_title).pad(15).expandX();
		mainMenu.add(button("LOGOUT", skin)).pad(15);
		mainMenu.row();
		mainMenu.add(modeButtons(gameModes, skin, modeNames)).padTop(50).left().top();
		mainMenu.add(menuButtons(options, skin, optionNames)).padTop(50).right().top();
		mainMenu.row();
		mainMenu.add(chatButtons(chat, skin, chatNames)).left().bottom();
		mainMenu.row();
		mainMenu.add(chatWindow).left().fill().height(300);

		stage.addActor(mainMenu);

		//batch = new SpriteBatch();
		sprite = new Sprite(new Texture(Gdx.files.internal("Login.jpg")));
		sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//Gdx.input.setInputProcessor(stage);
	}

	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/*
		 * The batch code needs to be before the stage otherwise the sprite
		 * (background picture) will be drawn over the buttons
		 */
		game.batch.begin();
		sprite.draw(game.batch);
		game.batch.end();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	
	public TextButton button(String name, Skin skin) {
		return new TextButton(name, skin);
	}

	public Table menuButtons(Table table, Skin skin, String[] names) {

		for (int i = 0; i < names.length; i++) {
			table.add(new TextButton(names[i], skin)).fill().padBottom(10).padLeft(10).padRight(10).row();
		}

		return table;
	}

	public Table modeButtons(Table table, Skin skin, String[] names) {

		for (int i = 0; i < names.length; i += 2) {
			table.add(new TextButton(names[i], skin)).fill().padBottom(10).padLeft(10).padRight(10);
			table.add(new TextButton(names[i+1], skin)).fill().padBottom(10).padLeft(10).padRight(10).row();	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
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