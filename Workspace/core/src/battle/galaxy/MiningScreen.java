package battle.galaxy;

import java.net.UnknownHostException;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import entities.Asteroid;
import master.classes.MasterGameScreen;

public class MiningScreen extends MasterGameScreen{

	public final static int MAP_SIZE = 20480;
	private static Vector2[] respawnPoints = {new Vector2(MAP_SIZE/2, MAP_SIZE/2)}; //CENTER
	
	private HashMap<Integer, Asteroid> asteroids = new HashMap<Integer, Asteroid>();
	
	public MiningScreen() throws UnknownHostException {
		super(0, MAP_SIZE, respawnPoints);
		generateAsteroids();
	}
	
	
	private void generateAsteroids() {
		
	}



	@Override
	public void update(float delta) {
		getReticle().update(getMouse());
		getPlayer().updateRotation(delta, getReticle());
		updateProjectiles(delta);
		checkCollision();
	}
	
	@Override
	public void checkCollision() {
		
	}

}
