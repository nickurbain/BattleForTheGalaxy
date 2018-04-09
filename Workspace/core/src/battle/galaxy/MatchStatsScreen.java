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
				try {
					game.setScreen(new MainMenu());
				} catch (UnknownHostException e) {
					e.printStackTrace();
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

	public Label header(final String name, Skin skin, Float scale) {
		Label header = new Label(name, skin);
		header.setFontScale(scale);
		return header;
	}
}