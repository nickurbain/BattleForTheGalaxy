package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import controllers.DataController;
import data.JsonHeader;
import data.UserQueryData;

/**
 * This is the main class which contains the game and is called by the DesktopLauncher
 */
public class BattleForTheGalaxy extends Game {

	SpriteBatch batch;
	Skin skin;
	private DataController dataController;
	Music music;
	
	/**
	 * Runs when the game is created, sets up the skin, SpriteBatch, DataController and sets the first screen (SpashScreen)
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		skin = new Skin(Gdx.files.internal("clean-crispy-ui.json"));
		
		setDataController(new DataController(this));
		music = Gdx.audio.newMusic(Gdx.files.internal("the-buccaneers-haul.mp3"));
		music.setLooping(true);
		music.play();
		
		//Testing stuff
		//testStuff();
		
		try {
			setScreen(new SplashScreen(this));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		Gdx.graphics.setTitle("BATTLE FOR THE GALAXY");
	}
	
	public void testStuff() {
		System.out.println(dataController.getJsonController().getJsonElement("{jsonOrigin:1,jsonType:12,matchId:12}", "matchId", Double.class));
		UserQueryData ld = new UserQueryData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_LOGIN, "hi", "yo");
		String s = dataController.getJsonController().dataToJson(ld);
		System.out.println(s);
		//ld = (UserQueryData)dataController.getJsonController().convertFromJson(s, NewMatchData.class);
		System.out.println(ld.getId());
	}

	/**
	 * Gets the game's SpriteBatch
	 * @return batch
	 */
	public SpriteBatch getBatch() {
		return batch;
	}
	
	/**
	 * Gets the game's Skin
	 * @return the skin
	 */
	public Skin getSkin() {
		return skin;
	}

	/**
	 * Calls the super render
	 */
	@Override
	public void render () {
		super.render();
	}
	
	/**
	 * Closes the websocket and disposes the SpriteBatch
	 */
	@Override
	public void dispose () {
		batch.dispose();
		music.dispose();
		getDataController().close();
	}

	/**
	 * @return the dataController
	 */
	public DataController getDataController() {
		return dataController;
	}

	/**
	 * @param dataController the dataController to set
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
	}
}
