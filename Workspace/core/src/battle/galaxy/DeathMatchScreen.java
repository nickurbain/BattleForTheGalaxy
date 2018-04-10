package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.math.Vector2;

import data.JsonHeader;
import master.classes.MasterGameScreen;

public class DeathMatchScreen extends MasterGameScreen{

	public final static int MAP_SIZE = 20480;
	
	private static Vector2[] respawnPoints = {
			new Vector2(MAP_SIZE/4, MAP_SIZE/4), 						//BOTTOM-LEFT
			new Vector2(MAP_SIZE - MAP_SIZE/4, MAP_SIZE/4), 			//BOTTOM-RIGHT
			new Vector2(MAP_SIZE/4, MAP_SIZE - MAP_SIZE/4), 			//TOP-LEFT
			new Vector2(MAP_SIZE - MAP_SIZE/4, MAP_SIZE - MAP_SIZE/4) 	//TOP-RIGHT
	};
	
	public DeathMatchScreen() throws UnknownHostException {
		super(JsonHeader.TYPE_DEATHMATCH, MAP_SIZE, respawnPoints);
	}

	@Override
	public void update(float delta) {
		getReticle().update(getMouse());
		getPlayer().updateRotation(delta, getReticle());
		updateProjectiles(delta);
		updateEnemies(delta);
		checkCollision();
	}

}
