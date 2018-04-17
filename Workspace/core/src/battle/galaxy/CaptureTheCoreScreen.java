package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.math.Vector2;

import entities.Core;
import master.classes.MasterGameScreen;

public class CaptureTheCoreScreen extends MasterGameScreen{

	public final static int MAP_SIZE = 20480;
	private static Vector2[] respawnPoints = {
			new Vector2(MAP_SIZE/4, MAP_SIZE/2),
			new Vector2(MAP_SIZE - MAP_SIZE/4, MAP_SIZE/2)
	};
	
	Core[] cores;
	
	public CaptureTheCoreScreen() throws UnknownHostException {
		super(3, MAP_SIZE, respawnPoints);
		//Create the flags
		cores[0] = new Core(0, respawnPoints[0]);
		cores[1] = new Core(1, respawnPoints[1]);
	}

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
	
	public void checkCores() {
		Core core = (player.getTeam() == 0) ? cores[0] : cores[1];
		if(!core.isPickedUp()) {
			Vector2 dist = new Vector2();
			dist.x = (float) Math.pow(player.getX() - core.getX(), 2);
			dist.y = (float) Math.pow(player.getY() - core.getY(), 2);
			
			if(Math.sqrt(dist.x + dist.y) < 50) {
				core.pickUp(player.getId());
			}
		}
	}
	
	@Override
	public Vector2 pickRespawnPoint() {
		if(player.getTeam() == 0) {
			return respawnPoints[0];
		}else {
			return respawnPoints[1];
		}
	}
	
	

}
