package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import data.DataController;

public class BattleForTheGalaxy extends Game {
	SpriteBatch batch;
	SplashScreen splashscreen;
	GameScreen gamescreen;
	Reticle reticle;
	
	DataController dataController;
	
	JsonWriter jsonWriter;
	JsonReader jsonReader;
	JsonValue jsonValue;
	Json json;
	
	// PlayerInfo class used for user credentials and location on the map
		public class PlayerInfo {
			String id, password;
			float x, y, degrees;
			
			public void setCreds(String givenID, String givenPassword) {
				id = givenID;
				password = givenPassword;
			}
			
			public void updateLocation(float givenX, float givenY, float givenDegrees) {
				x = givenX;
				y = givenY;
				degrees = givenDegrees;
			}
		}
		
	PlayerInfo playerInfo = new PlayerInfo();
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		reticle = new Reticle();
		
		jsonReader = new JsonReader();
		json = new Json();
		
		dataController = new DataController(this);
		
		try {
			splashscreen = new SplashScreen(this);
		} catch (UnknownHostException e) {
			System.out.println("I DONT KNOW WHAT IM DOING");
			e.printStackTrace();
			System.exit(0);
		}
		gamescreen = new GameScreen(this);
		setScreen(splashscreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		splashscreen.dispose();
		gamescreen.dispose();
	}
	
	public Json getJson() {
		return json;
	}
}
