package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import controllers.MainMenuController;
import master.classes.MasterScreen;

public class MainMenu2 extends MasterScreen {

	private MainMenuController mmc = new MainMenuController();
	private Label title;
	private Table mainMenu, options, gameModes, chat;
	private TextButton logout;

	public MainMenu2() throws UnknownHostException {
		super(game, "Login.jpg", "clean-crispy-ui.json");

		mainMenu = new Table();
		mainMenu.setWidth(stage.getWidth());
		mainMenu.align(Align.top);
		mainMenu.setPosition(0, Gdx.graphics.getHeight());

		options = new Table();
		options.align(Align.right);
		String[] optionNames = { "ACCOUNT", "GALACTIC SHOP", "HANGER", "FACTION", "ALLIANCE", "CREW", "EVENTS" };

		gameModes = new Table();
		gameModes.align(Align.left | Align.top);
		String[] modeNames = { "ALL OUT\nDEATH MATCH", "ALLIANCE\nDEATH MATCH", "FACTION\nBATTLE", "TEAM\nDEATH MATCH",
				"CONSTRUCTION", "MINING" };

		chat = new Table();
		chat.align(Align.left);
		String[] chatNames = { "Global", "Team", "Private" };

		TextArea chatWindow = new TextArea("Hello World", skin);

		// Shows table lines for debugging
		// mainMenu.setDebug(true);

		logout = button("LOGOUT", skin);
		mmc.setOption(logout, 7);

		// Labels
		title = new Label("BATTLE FOR THE GALAXY", skin);
		title.setFontScale(4f);

		// Add all functions to the main menu
		mainMenu.add(title).pad(15).expandX();
		mainMenu.add(logout).pad(15).fillX().padLeft(10).padRight(10);
		mainMenu.row();
		mainMenu.add(modeButtons(gameModes, skin, modeNames)).padTop(50).left().top();
		mainMenu.add(menuButtons(options, skin, optionNames)).padTop(50).right().top();
		mainMenu.row();
		mainMenu.add(chatButtons(chat, skin, chatNames)).left().bottom();
		mainMenu.row();
		mainMenu.add(chatWindow).left().fill().height(150);

		stage.addActor(mainMenu);

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
	}

	public TextButton button(String name, Skin skin) {
		return new TextButton(name, skin);
	}

	public Table menuButtons(Table table, Skin skin, String[] names) {

		for (int i = 0; i < names.length; i++) {
			TextButton button = new TextButton(names[i], skin);
			mmc.setOption(button, i);
			table.add(button).fill().padBottom(10).padLeft(10).padRight(10).row();
		}
		return table;
	}

	public Table modeButtons(Table table, Skin skin, final String[] names) {

		for (int i = 0; i < names.length; i++) {
			TextButton button = new TextButton(names[i], skin);
			mmc.setMode(button, i);
			table.add(button).fill().padBottom(10).padLeft(10).padRight(10);

			if (i % 2 == 1) {
				table.row();
			}
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
	public void dispose() {

	}
}