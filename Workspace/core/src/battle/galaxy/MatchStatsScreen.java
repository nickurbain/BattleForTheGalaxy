package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.utils.JsonValue;

import data.PlayerMatchStat;

public class MatchStatsScreen implements Screen {
	private BattleForTheGalaxy game;
	private OrthographicCamera camera;
	private Texture bg_texture;
	private Sprite bg_sprite;
	private Stage stage;

	private Label title, user_id, kills, deaths;
	private Table matchStats, headers;
	private Skin skin;
	
	private String statsJson;
	PlayerMatchStat ms1, ms2, ms3;

	public MatchStatsScreen(BattleForTheGalaxy incomingGame, String statsJson) throws UnknownHostException {
		this.game = incomingGame;
		stage = new Stage();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1600, 900); // false => y-axis 0 is bottom-left

		this.statsJson = statsJson;
		parseStatsJson();
		
		skin = incomingGame.skin;

		bg_texture = new Texture(Gdx.files.internal("supernova-background.jpg"));
		bg_texture.setFilter(TextureFilter.Linear, TextureFilter.Linear); // smoother textures
		bg_sprite = new Sprite(bg_texture);

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
		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				try {
					game.setScreen(new MainMenu(game));
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
	
	private void parseStatsJson() {
		JsonValue base = game.jsonReader.parse(statsJson);
		//System.out.println(base);
		JsonValue component = base.child();
		component = component.next();
		System.out.println(component);
		component = component.next();
		System.out.println(component);
		component = component.child();
		System.out.println(component);
		JsonValue stat = component.child();
		ms1 = new PlayerMatchStat(stat.asInt(), stat.next().asInt(), stat.next().next().asInt(), stat.next().next().next().asInt(), 
				stat.next().next().next().next().asInt());
		System.out.println(ms1.getId() + "|" + ms1.getDamageDealt() + "|" + ms1.getDeaths() + "|" + ms1.getHP() + "|" + ms1.getKills());
		component = component.next();
		stat = component.child();
		ms2 = new PlayerMatchStat(stat.asInt(), stat.next().asInt(), stat.next().next().asInt(), stat.next().next().next().asInt(), 
				stat.next().next().next().next().asInt());
		System.out.println(ms2.getId() + "|" + ms2.getDamageDealt() + "|" + ms2.getDeaths() + "|" + ms2.getHP() + "|" + ms2.getKills());
		component = component.next();
		stat = component.child();
		ms3 = new PlayerMatchStat(stat.asInt(), stat.next().asInt(), stat.next().next().asInt(), stat.next().next().next().asInt(), 
				stat.next().next().next().next().asInt());
		System.out.println(ms3.getId() + "|" + ms3.getDamageDealt() + "|" + ms3.getDeaths() + "|" + ms3.getHP() + "|" + ms3.getKills());
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
