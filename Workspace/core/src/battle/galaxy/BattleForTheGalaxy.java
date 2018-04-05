package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import data.DataController;
import data.JsonHeader;
import data.LoginData;
import entities.Reticle;

public class BattleForTheGalaxy extends Game {

	SpriteBatch batch;
	Skin skin;
	private DataController dataController;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		skin = new Skin(Gdx.files.internal("clean-crispy-ui.json"));
		
		setDataController(new DataController(this));
		
		//Testing stuff
		//testStuff();
		
		try {
			setScreen(new LoginScreen(this));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		Gdx.graphics.setTitle("BATTLE FOR THE GALAXY");
	}
	
	public void testStuff() {
		System.out.println(dataController.getJsonController().getJsonElement("{jsonOrigin:1,jsonType:12,matchId:12}", "matchId", Double.class));
		LoginData ld = new LoginData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_LOGIN, "hi", "yo");
		String s = dataController.getJsonController().dataToJson(ld);
		System.out.println(s);
		//ld = (LoginData)dataController.getJsonController().convertFromJson(s, LoginData.class);
		System.out.println(ld.getId());
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
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
