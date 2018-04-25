package battle.galaxy;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import controllers.MainMenuController;
import master.classes.MasterButtons;
import master.classes.MasterScreen;

/**
 * Main menu the player encounters after logging into game. Main menu contains
 * various options for player to choose from.
 */
public class MainMenu extends MasterScreen {

	private MainMenuController mmc = new MainMenuController();
	private Label welcome;
	private Table mainMenu, options, gameModes;
	private ImageTextButton logout;
	private Texture bigTitleTexture;
	private Sprite bigTitleSprite;
	private ImageTextButtonStyle style;
	
	/**
	 * Constructor for the main menu that makes a call to the master screen and
	 * renders a menu for the player to use.
	 * 
	 * @throws UnknownHostException
	 *             Button listener could throw an exception
	 */
	public MainMenu() throws UnknownHostException {
		// Calls master screen
		super("main-menu.jpg", "clean-crispy-ui.json");

		bigTitleTexture = new Texture(Gdx.files.internal("BFTG_title.png"));
		bigTitleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bigTitleSprite = new Sprite(bigTitleTexture);
		bigTitleSprite.setSize(bigTitleSprite.getWidth() / 2, bigTitleSprite.getHeight() / 2);
		bigTitleSprite.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 10);

		// Setup for the main menu table
		mainMenu = new Table();
		mainMenu.setWidth(stage.getWidth());
		mainMenu.setHeight(stage.getHeight());
		mainMenu.align(Align.top);
		mainMenu.setPosition(0, 0);

		// Setup for game options menu table
		options = new Table();
		options.align(Align.right);
		String[] optionNames = { "ACCOUNT", "GALACTIC SHOP", "HANGER", "ALLIANCE", "CREW", "EVENTS" };

		// Setup for game modes menu table
		gameModes = new Table();
		gameModes.align(Align.left | Align.top);
		String[] modeNames = { "ALL OUT\nDEATH MATCH", "ALLIANCE\nDEATH MATCH", "JUGGERNAUGHT", "TEAM\nDEATH MATCH",
				"CAPTURE\nTHE CORE", "MINING" };

		// Shows table lines for debugging, uncomment to outline table
		// mainMenu.setDebug(true);

		// Logout button
		style = MasterButtons.setButtonStyle("SansSerif.fnt", "opt_button.png", 0.7f);
		logout = new ImageTextButton("LOGOUT", style);

		// Controller to connect click listener
		mmc.setOption(logout, 6);

		welcome = new Label("Welcome " + user, skin);
		welcome.setFontScale(2f);

		//stats = new Label("Alliance " + alliance)
		// Add all table menus to the main menu
		mainMenu.add(welcome).pad(15).colspan(2).center();
		mainMenu.add(logout).right();
		mainMenu.row().expand();
		mainMenu.add(modeButtons(gameModes, modeNames)).padTop(50).padLeft(50).left().top();
		mainMenu.add().center();
		mainMenu.add(menuButtons(options, optionNames)).padTop(50).right().top();
		mainMenu.row();
//		mainMenu.row().expandY();
		mainMenu.add(chatWindow).bottom().left().padTop(10).fill();

		stage.addActor(mainMenu);

		Gdx.input.setInputProcessor(stage);
	}

	/**
	 * Makes a call to render in the master screen
	 */
	@Override
	public void render(float delta) {
		super.render(delta);

		camera.update();

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		bigTitleSprite.draw(game.batch);
		game.batch.end();

		stage.draw();
	}

	/**
	 * Adds menu option buttons to the options table and makes a call to the main
	 * menu controller to connect listeners.
	 * 
	 * @param table
	 *            The table used to generate the options menu
	 * @param skin
	 *            The skin used to define defaults
	 * @param names
	 *            The names of the buttons
	 * @return The options table populated with buttons
	 */
	public Table menuButtons(Table table, String[] names) {

		style = MasterButtons.setButtonStyle("SansSerif.fnt", "opt_button.png", 0.7f);

		for (int i = 0; i < names.length; i++) {
			ImageTextButton myButton = new ImageTextButton(names[i], style);
			mmc.setOption(myButton, i);
			table.add(myButton).fill().padBottom(20).padLeft(10).row();
		}
		return table;
	}

	/**
	 * Generates the various buttons needed to enter the various games modes and
	 * calls to the main menu controller connect listeners.
	 * 
	 * @param table
	 *            The table used to generate the modes menu
	 * @param skin
	 *            The skin used to define defaults
	 * @param names
	 *            The names of the buttons
	 * @return The modes table populated with buttons
	 */
	public Table modeButtons(Table table, final String[] names) {

		style = MasterButtons.setButtonStyle("SansSerif.fnt", "mode_button.png", 0.7f);

		for (int i = 0; i < names.length; i++) {
			ImageTextButton myButton = new ImageTextButton(names[i], style);
			mmc.setMode(myButton, i);
			table.add(myButton).fill().pad(15);
			
			if (i % 2 == 1) {
				table.row();
			}
		}
		return table;
	}

	@Override
	public void dispose() {

	}
}