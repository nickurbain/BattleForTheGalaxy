package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.math.Vector2;

import data.JsonHeader;
import master.classes.MasterGameScreen;

public class TeamDeathMatchScreen extends MasterGameScreen{
	
public final static int MAP_SIZE = 20480;
	
	private static Vector2[] respawnPoints = {
			new Vector2(MAP_SIZE/4, MAP_SIZE/4), 						//BOTTOM-LEFT
			new Vector2(MAP_SIZE - MAP_SIZE/4, MAP_SIZE/4), 			//BOTTOM-RIGHT
			new Vector2(MAP_SIZE/4, MAP_SIZE - MAP_SIZE/4), 			//TOP-LEFT
			new Vector2(MAP_SIZE - MAP_SIZE/4, MAP_SIZE - MAP_SIZE/4) 	//TOP-RIGHT
	};

	public TeamDeathMatchScreen() throws UnknownHostException {
		super(1, MAP_SIZE, respawnPoints);
	}
	
	/**
	 * Implementation of the abstract method in MasterGameScreen. Updates the game.
	 */
	@Override
	public void update(float delta) {
		updateFromServer();
		reticle.update(mouse);
		updatePlayerData(delta);
		updateProjectiles(delta);
		updateEnemies(delta);
		checkCollision();
		updateServer();
	}

}
