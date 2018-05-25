package battle.galaxy;

import java.net.UnknownHostException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import master.classes.MasterScreen;

public class MatchStatsScreen extends MasterScreen {

	private Label userName, kills, deaths, damage;
	private Table matchStats;
	private String matchStatsJson;
	private int numPlayers;
	private HashMap<Integer, String> playerNames = new HashMap<Integer, String>();
	private JsonReader jsonReader = game.getDataController().getJsonController().getJsonReader();

	/**
	 * Constructor that sets up a Table with the UI elements
	 * @throws UnknownHostException
	 */
	public MatchStatsScreen(String matchStatsJson, HashMap<Integer, String> playerNames) throws UnknownHostException {
		super("Login.jpg", "clean-crispy-ui.json");
		
		this.matchStatsJson = matchStatsJson;
		numPlayers = jsonReader.parse(matchStatsJson).get("matchStats").size;
		this.playerNames.putAll(playerNames);
		
		matchStats = new Table();
		matchStats.setWidth(stage.getWidth());
		matchStats.align(Align.top);
		matchStats.setPosition(0, stage.getHeight());
		
		matchStats.add(header("Match Statistics", skin, 2f)).padTop((stage.getHeight() / 2) - 150).colspan(4);
		matchStats.row();
		matchStats.add(header("PLAYER", skin, 1.3f)).pad(15);
		matchStats.add(header("KILLS", skin, 1.3f)).pad(15);
		matchStats.add(header("DEATHS", skin, 1.3f)).pad(15);
		matchStats.add(header("DAMAGE DEALT", skin, 1.3f)).pad(15);
		matchStats.row();
		
		//Populate table with stats from json
		parseMatchStats();
		
		matchStats.add(Button(skin, "MAIN MENU")).padTop(10).right().colspan(4);
		//matchStats.debug();
		
		stage.addActor(matchStats);
		Gdx.input.setInputProcessor(stage);
	}

	/**
	 * Calls super.render() and listens for input to return to MainMenu if escape is pressed.
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
	 * Parse through the matchStatsJson string and fill the table
	 */
	public void parseMatchStats() {
		for(int i = 0; i < numPlayers; i++) {
			JsonValue base = jsonReader.parse(matchStatsJson).get("matchStats").get(i);
			//Pull data from Json
			userName = new Label(playerNames.get(base.getInt("playerId")), skin);
			kills = new Label(Integer.toString(base.getInt("kills")), skin);
			deaths = new Label(Integer.toString(base.getInt("deaths")), skin);
			damage = new Label(Integer.toString(base.getInt("damageDealt")), skin);
			//Add to table
			matchStats.add(userName);
			matchStats.add(kills);
			matchStats.add(deaths);
			matchStats.add(damage);
			matchStats.row();
		}
	}
	
	/**
	 * Custom button with a listener to exit this screen to the Main Menu.
	 * @param skin the skin to use on the button.
	 * @param name the name of the button
	 * @return button
	 */
	public ImageTextButton Button(Skin skin, final String name) {

		ImageTextButton button = new ImageTextButton(name, style_default);
		//TextButton button = new TextButton(name, skin);
		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				try {
					game.setScreen(new MainMenu());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		});
		return button;
	}
	
	/**
	 * Custom Label for the title of the screen
	 * @param name the name of the screen
	 * @param skin the skin to use on the Label
	 * @param scale the scale of the Label
	 * @return the Label
	 */
	public Label header(final String name, Skin skin, Float scale) {
		Label header = new Label(name, skin);
		header.setFontScale(scale);
		return header;
	}
}