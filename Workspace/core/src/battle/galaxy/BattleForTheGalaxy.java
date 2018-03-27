package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
		
		/*
		PlayerData pd = new PlayerData(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_PLAYER, 10, new Vector2(1,2), new Vector2(3,4), 0);
		System.out.println(json.toJson(pd));
		String data = json.toJson(pd);
		PlayerData pe = json.fromJson(PlayerData.class, data);
		System.out.println(pe.getId());
		
		JsonValue base = jsonReader.parse(data);
		JsonValue component = base.child;
		System.out.println(component.name + component.asByte());
		component = component.next();
		System.out.println(component.name);
		*/
		
		
		//LoginData ld = new LoginData((byte)1, (byte)0, "nick", "potato");
		//System.out.println(json.toJson(ld));
		
		dataController = new DataController(this);
		
		/*try {
			splashscreen = new SplashScreen(this);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(0);
		}*/
		//gamescreen = new GameScreen(this);
		try {
			setScreen(new SplashScreen(this));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		//setScreen(splashscreen);
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
