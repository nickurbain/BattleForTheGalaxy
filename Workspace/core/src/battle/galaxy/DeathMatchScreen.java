package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.math.Vector2;

import master.classes.MasterGameScreen;

/**
 * The screen used for a DeathMatch. Extends the MasterGameScreen.
 */
public class DeathMatchScreen extends MasterGameScreen{

	public final static int MAP_SIZE = 20480;
	
	private static Vector2[] respawnPoints = {
			new Vector2(MAP_SIZE/4, MAP_SIZE/4), 						//BOTTOM-LEFT
			new Vector2(MAP_SIZE - MAP_SIZE/4, MAP_SIZE/4), 			//BOTTOM-RIGHT
			new Vector2(MAP_SIZE/4, MAP_SIZE - MAP_SIZE/4), 			//TOP-LEFT
			new Vector2(MAP_SIZE - MAP_SIZE/4, MAP_SIZE - MAP_SIZE/4) 	//TOP-RIGHT
	};
	
	/**
	 * Constructor
	 * @throws UnknownHostException
	 */
	public DeathMatchScreen() throws UnknownHostException {
		super(0, MAP_SIZE, respawnPoints);
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
		updateFromServer();
	}

}
