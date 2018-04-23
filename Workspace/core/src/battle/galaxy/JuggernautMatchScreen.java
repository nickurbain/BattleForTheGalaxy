package battle.galaxy;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.gdx.math.Vector2;

import data.PlayerData;
import entities.EnemyPlayer;
import master.classes.MasterGameScreen;

public class JuggernautMatchScreen extends MasterGameScreen {

	public final static int MAP_SIZE = 20480;
	
	private static Vector2[] respawnPoints = {
			new Vector2(MAP_SIZE/4, MAP_SIZE/4), 						//BOTTOM-LEFT
			new Vector2(MAP_SIZE - MAP_SIZE/4, MAP_SIZE/4), 			//BOTTOM-RIGHT
			new Vector2(MAP_SIZE/4, MAP_SIZE - MAP_SIZE/4), 			//TOP-LEFT
			new Vector2(MAP_SIZE - MAP_SIZE/4, MAP_SIZE - MAP_SIZE/4) 	//TOP-RIGHT
	};
	
	public JuggernautMatchScreen() throws UnknownHostException {
		super(4, MAP_SIZE, respawnPoints);
	}

	@Override
	public void update(float delta) {
		reticle.update(mouse);
		updatePlayerData(delta);
		updateProjectiles(delta);
		updateEnemies(delta);
		checkCollision();
		updateServer();
		updateFromServer();
	}
	
	/**
	 * Scan through gameData and check if there are new enemies and add them as an EnemyPlayer.
	 * Also check for updated enemy data in gameData and update the enemies. Override to check for
	 * Juggernaut.
	 * @param delta
	 */
	protected void updateEnemies(float delta) {
		for(Iterator<Entry<Integer, PlayerData>> iter = gameData.getEnemies().entrySet().iterator(); iter.hasNext();) {
			PlayerData ed = iter.next().getValue();
			if(!otherPlayers.containsKey(ed.getId())) {
				EnemyPlayer e = new EnemyPlayer(ed, player.getTeam());
				System.out.println("Enemy: " + e.getTeamNum() + " Player: " + player.getTeam());
				//e.setPosition(e.getX(), e.getY() + 150);	//ECHO SERVER TESTING
				otherPlayers.put(e.getId(), e);	
				stage.addActor(e);
			}else{
				otherPlayers.get(ed.getId()).updateEnemy(ed);
			}
			if(ed.isJuggernaut() && !otherPlayers.get(ed.getId()).isJuggernaut()) {
				otherPlayers.get(ed.getId()).makeJuggernaut();
			}else if (!ed.isJuggernaut() && otherPlayers.get(ed.getId()).isJuggernaut()) {
				otherPlayers.get(ed.getId()).removeJuggernaut();
			}
		}
		
		if(gameData.getPlayerData().isJuggernaut() && !player.isJuggernaut()) {
			player.makeJuggernaut();
		}else if (!gameData.getPlayerData().isJuggernaut() && player.isJuggernaut()) {
			player.removeJuggernaut();
		}
	}
	
	

}
