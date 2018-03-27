package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import data.DataController;
import data.LoginData;
import entities.Reticle;

public class BattleForTheGalaxy extends Game {
	SpriteBatch batch;
	//SplashScreen splashscreen;
	//MainMenu2 mainMenuScreen;
	//GameScreen gamescreen;
	Reticle reticle;
	Skin skin;
	
	DataController dataController;
	
	public JsonWriter jsonWriter;
	public JsonReader jsonReader;
	public JsonValue jsonValue;
	public Json json;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		reticle = new Reticle();
		
		jsonReader = new JsonReader();
		json = new Json();
		
		skin = new Skin(Gdx.files.internal("clean-crispy-ui.json"));
		
		dataController = new DataController(this);
		
		try {
			setScreen(new SplashScreen(this));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		dataController.close();
		//splashscreen.dispose();
		//mainMenuScreen.dispose();
		/*if(gamescreen != null) {
			gamescreen.dispose();
		}*/
	}
	
	public Json getJson() {
		return json;
	}
}
