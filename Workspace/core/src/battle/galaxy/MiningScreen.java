package battle.galaxy;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;

import data.NewMatchData;
import entities.Asteroid;
import entities.Asteroid.asteroidType;
import entities.Projectile;
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
		reticle.update(mouse);
		player.updateRotation(delta, reticle);
		addProjectile(player.getNewProjectile());
		updateProjectiles(delta);
		checkCollision();
	}
	
	/**
	 * Check for collisions between the projectiles and asteroids.
	 */
	@Override
	public void checkCollision() {
		for(Iterator<Map.Entry<Integer, Asteroid>> astIter = asteroids.entrySet().iterator(); astIter.hasNext();) {
			Asteroid a = astIter.next().getValue();
			for(Iterator<Map.Entry<Integer, Projectile>> projIter = projectiles.entrySet().iterator(); projIter.hasNext();) {
				Projectile p = projIter.next().getValue();
				Vector2 diff = new Vector2();
				diff.x = (float) Math.pow(a.getX() - p.getX(), 2);
				diff.y = (float) Math.pow(a.getY() - p.getY(), 2);
				if(Math.sqrt(diff.x + diff.y) > a.getSize().x + 50) {
					p.kill();
					if(a.damage(p.getDamage()) <= 0) {
						gameData.addScore(a.getValue());
						a.kill();
					}
				}
			}
			Vector2 diff = new Vector2();
			diff.x = (float) Math.pow(a.getX() - player.getX(), 2);
			diff.y = (float) Math.pow(a.getY() - player.getY(), 2);
			if(Math.sqrt(diff.x + diff.y) > a.getSize().x + 50) {
				player.reset(pickRespawnPoint());
			}
		}
	}
	
	@Override
	protected NewMatchData joinMatch() {
		return new NewMatchData(0,0);
	}

}
