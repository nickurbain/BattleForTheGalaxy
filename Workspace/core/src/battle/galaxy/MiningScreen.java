package battle.galaxy;

import java.net.UnknownHostException;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import entities.Asteroid;
import master.classes.MasterGameScreen;

/**
 * Screen for single player Mining Mode. Extends MasterGameScreen.
 */
public class MiningScreen extends MasterGameScreen{

	public final static int MAP_SIZE = 20480;
	private static Vector2[] respawnPoints = {new Vector2(MAP_SIZE/2, MAP_SIZE/2)}; //CENTER
	
	private HashMap<Integer, Asteroid> asteroids = new HashMap<Integer, Asteroid>();
	
	/**
	 * Constructor the calls super() and sets up the asteroids for mining.
	 * @throws UnknownHostException
	 */
	public MiningScreen() throws UnknownHostException {
		super(0, MAP_SIZE, respawnPoints);
		generateAsteroids();
	}
	
	/**
	 * Generates the initial set of asteroids
	 */
	private void generateAsteroids() {
		
	}
	
	/**
	 * Update the player, projectiles, asteroids, and check for collisions.
	 */
	@Override
	public void update(float delta) {
		getReticle().update(getMouse());
		getPlayer().updateRotation(delta, getReticle());
		updateProjectiles(delta);
		checkCollision();
	}
	
	/**
	 * Check for collisions between the projectiles and asteroids.
	 */
	@Override
	public void checkCollision() {
		
	}

}
