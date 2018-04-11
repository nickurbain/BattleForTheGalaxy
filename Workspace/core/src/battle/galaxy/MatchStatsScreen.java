package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import master.classes.MasterScreen;

public class MatchStatsScreen extends MasterScreen {

	private Label title, user_id, kills, deaths;
	private Table matchStats, headers;
	private Skin skin;

	/**
	 * Constructor that sets up a Table with the UI elements
	 * @throws UnknownHostException
	 */
	public MatchStatsScreen() throws UnknownHostException {

		super("Login.jpg", "clean-crispy-ui.json");
		
		matchStats = new Table();
		matchStats.setWidth(stage.getWidth());
		matchStats.align(Align.top);
		matchStats.setPosition(0, stage.getHeight());

		headers = new Table();
		headers.add(header("PLAYER", skin, 2f)).width(150).height(30);
		headers.add(header("KILLS", skin, 2f)).width(150).height(30);
		headers.add(header("DEATHS", skin, 2f)).width(150).height(30);
		//headers.setDebug(true);
		
		matchStats.add(header("Match Statistics", skin, 4f)).padTop((stage.getHeight() / 2) - 150);
		matchStats.row();
		matchStats.add(headers);
		matchStats.row();
		matchStats.add(Button(skin, "MAIN MENU")).padTop(10).align(Align.right);

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
	 * Custom button with a listener to exit this screen to the Main Menu.
	 * @param skin the skin to use on the button.
	 * @param name the name of the button
	 * @return button
	 */
	public TextButton Button(Skin skin, final String name) {

		TextButton button = new TextButton(name, skin);
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