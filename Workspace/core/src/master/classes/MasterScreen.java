package master.classes;

import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import battle.galaxy.BattleForTheGalaxy;
import controllers.DataController;
import controllers.UserQueryController;


/**
 * The master screen class contains the elements that all screens in Battle For
 * The Galaxy share.
 */
public class MasterScreen implements Screen {

	protected static BattleForTheGalaxy game;
	protected OrthographicCamera camera;
	protected Texture background;
	protected Stage stage;
	protected Skin skin;
	protected static String user, alliance;
	protected static Table master, chatWindow;
	private String[] chatNames = { "Global", "Team", "Private" };
	
	/**
	 * An empty constructor
	 */
	public MasterScreen() {

	}

	/**
	 * The master screen constructor that all game screens share
	 * 
	 * @param picture
	 *            The background picture the screen will have
	 * @param skin
	 *            The file containing default values for various items such as font,
	 *            text buttons, labels, etc...
	 * @throws UnknownHostException
	 */
	public MasterScreen(String picture, String skin) throws UnknownHostException {
		game = DataController.getGame();
		user = UserQueryController.getUser();
		//alliance = 
		stage = new Stage();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1600, 900); // false => y-axis 0 is bottom-left
		//stage.setViewport(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
		
		this.setSkin(skin);

		background = new Texture(Gdx.files.internal(picture));
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		//master = new Table();
		
		chatWindow = new Table();
		chatWindow.align(Align.bottomLeft);
		chatWindow.setHeight(300);
		chatWindow.setWidth(100);
		chatWindow();
		
		//stage.addActor(chatWindow);
	}

	/**
	 * The render options that are shared between all screens
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.05F, 0.05F, 0.05F, 0.05F);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);

		game.getBatch().begin();
		game.getBatch().draw(background, 0, 0);
		game.getBatch().end();

		// Stage
		stage.act();
		stage.draw();
	}

	/**
	 * Sets the skin that the screen calling master screen will use
	 * 
	 * @param skin
	 *            The skin file name as a string that will be used by the screen
	 *            calling master screen.
	 */
	public void setSkin(String skin) {
		this.skin = new Skin(Gdx.files.internal(skin));
	}
	
	/**
	 * Generates the options to select when entering a chat
	 * 
	 * @param table
	 *            The table used to generate the chat menu
	 * @param skin
	 *            The skin used to define defaults
	 * @param names
	 *            The names of the buttons
	 * @return The chat table populated with buttons
	 */
	public Table chatWindow() {
		TextArea chatBox = new TextArea("Hello World", skin);
		Table chatOptions = new Table();
		
		for (int i = 0; i < chatNames.length; i++) {
			chatOptions.add(new TextButton(chatNames[i], skin)).width(150);
		}
		
		chatWindow.add(chatOptions).left();
		chatWindow.row();
		chatWindow.add(chatBox).width(700).height(150);
		return chatWindow;
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
		game.getDataController().getRxFromServer().clear();
		game.getDataController().getRawData().clear();
	}
}