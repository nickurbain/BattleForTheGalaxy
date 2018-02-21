package battle.galaxy;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import data.DataController;
import data.JsonHeader;
import data.PlayerData;

public class BattleForTheGalaxy extends Game {
	SpriteBatch batch;
	SplashScreen splashscreen;
	GameScreen gamescreen;
	Reticle reticle;
	
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
		dataController.close();
	}
	
	public Json getJson() {
		return json;
	}
}
