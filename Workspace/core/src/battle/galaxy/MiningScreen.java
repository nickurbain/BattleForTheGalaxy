package battle.galaxy;

import java.net.UnknownHostException;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import entities.Asteroid;
import entities.Asteroid.asteroidType;
import master.classes.MasterGameScreen;

/**
 * Screen for single player Mining Mode. Extends MasterGameScreen.
 */
public class MiningScreen extends MasterGameScreen{

	public final static int MAP_SIZE = 20480;
	private static Vector2[] respawnPoints = {new Vector2(MAP_SIZE/2, MAP_SIZE/2)}; //CENTER
	private final int TOTAL_ASTEROIDS = 500;
	
	private HashMap<Integer, Asteroid> asteroids = new HashMap<Integer, Asteroid>(100);
	
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
		for(int i = 0; i < TOTAL_ASTEROIDS; i++) {
			asteroidType type = randomType();
			Vector2 pos = new Vector2((float) (Math.random() * MAP_SIZE) - 100, (float) (Math.random() * MAP_SIZE) - 100);
			Asteroid asteroid = new Asteroid(type, i, pos);
			stage.addActor(asteroid);
			asteroids.put(i, asteroid);
		}
	}
	
	/**
	 * Generates a random asteroidType
	 * @return the random asteroidType
	 */
	public asteroidType randomType() {
		asteroidType type;
		int num = (int) (Math.random() * 100);
		if(num < 5) {
			type = asteroidType.EXPLOSIVE;
		}else if (num > 5 && num < 15) {
			type = asteroidType.UNOBTANIUM;
		}else if (num > 15 && num < 40) {
			type = asteroidType.GOLD;
		}else if (num > 40 && num < 70) {
			type = asteroidType.IRON;
		}else {
			type = asteroidType.COPPER;
		}
		return type;
	}
	
	/**
	 * Update the player, projectiles, asteroids, and check for collisions.
	 */
	@Override
	public void update(float delta) {
		getReticle().update(getMouse());
		getPlayer().updateRotation(delta, getReticle());
		addProjectile(getPlayer().getNewProjectile());
		updateProjectiles(delta);
		checkCollision();
	}
	
	/**
	 * Check for collisions between the projectiles and asteroids.
	 */
	@Override
	public void checkCollision() {
		
	}
	
	@Override
	protected int joinMatch() {
		return 0;
	}

}
