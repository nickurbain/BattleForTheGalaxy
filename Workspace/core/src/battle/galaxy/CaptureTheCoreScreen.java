package battle.galaxy;

import java.net.UnknownHostException;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;

import data.CoreData;
import entities.Core;
import master.classes.MasterGameScreen;

public class CaptureTheCoreScreen extends MasterGameScreen{

	public final static int MAP_SIZE = 20480;
	private static Vector2[] respawnPoints = {
			new Vector2(MAP_SIZE/4, MAP_SIZE/2),
			new Vector2(MAP_SIZE - MAP_SIZE/4, MAP_SIZE/2)
	};
	
	Core[] cores = new Core[2];
	
	public CaptureTheCoreScreen() throws UnknownHostException {
		super(3, MAP_SIZE, respawnPoints);
		//Create the flags
		cores[0] = new Core(0, gameData.getTeamNum(), respawnPoints[0]);
		cores[1] = new Core(1, gameData.getTeamNum(), respawnPoints[1]);
		stage.addActor(cores[0]);
		stage.addActor(cores[1]);
	}

	@Override
	public void update(float delta) {
		updateFromServer();
		reticle.update(mouse);
		updatePlayerData(delta);
		updateProjectiles(delta);
		updateEnemies(delta);
		checkCollision();
		checkCores();
		updateServer();
	}
	
	public void checkCores() {
		//Check for updates from the server regarding cores
		for(Iterator<CoreData> iter = gameData.getCoreUpdates().iterator(); iter.hasNext();) {
			CoreData coreData = iter.next();
			cores[coreData.getTeamNum()].update(coreData);
		}
		//Check if the player picks up or captures the core
		Core core = player.getTeam() == 0 ? cores[1] : cores[0];
		if(!core.isPickedUp()) {
			Vector2 dist = new Vector2();
			dist.x = (float) Math.pow(player.getX() - core.getX(), 2);
			dist.y = (float) Math.pow(player.getY() - core.getY(), 2);
			
			if(Math.sqrt(dist.x + dist.y) < 50) {
				core.pickUp(player.getId());
				System.out.println("Core " + core.getTeam() + " Picked up by " + core.getHolderId() + " On team " + player.getTeam());
				game.getDataController().sendToServer(new CoreData(core.getTeam(), player.getId(), false));
			}
		}else {
			Vector2 dist = new Vector2();
			dist.x = (float) Math.pow(respawnPoints[player.getTeam()].x - core.getX(), 2);
			dist.y = (float) Math.pow(respawnPoints[player.getTeam()].y - core.getY(), 2);
			if(Math.sqrt(dist.x + dist.y) < 50) {
				core.drop();
				game.getDataController().sendToServer(new CoreData(core.getTeam(), player.getId(), true));
				System.out.println("Core " + core.getTeam() + " Dropped by " + core.getHolderId());
			}
		}
		//Move the core with the player who is holding it
		for(Core c: cores) {
			if(c.isPickedUp()) {
				if(c.getHolderId() == player.getId()) {
					c.setPosition(player.getX(), player.getY());
				}else {
					c.setPosition(otherPlayers.get(c.getHolderId()).getX(), otherPlayers.get(c.getHolderId()).getY());
				}
			}
		}
	}
	
	@Override
	public void killPlayer() {
		player.getShip().calcStats();
		player.reset(pickRespawnPoint());
		gameData.getPlayerData().reset();
		Core core = cores[player.getTeam()];
		if(core.getHolderId() == player.getId()) {
			core.drop();
			game.getDataController().sendToServer(new CoreData(core.getTeam(), -1, false));
		}
	}
	
	@Override
	public Vector2 pickRespawnPoint() {
		if(gameData.getTeamNum() == 0) {
			return respawnPoints[0];
		}else {
			return respawnPoints[1];
		}
	}
	
	

}
