package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.math.Vector2;

import master.classes.MasterGameScreen;

public class MiningScreen extends MasterGameScreen{

	public final static int MAP_SIZE = 20480;
	
	private static Vector2[] respawnPoints = {new Vector2(MAP_SIZE/2, MAP_SIZE/2)};
	
	public MiningScreen() throws UnknownHostException {
		super(0, MAP_SIZE, respawnPoints);
	}

}
