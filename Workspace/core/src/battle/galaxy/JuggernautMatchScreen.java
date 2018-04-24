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
				if(ed.getTeamNum() == 0) {
					ed.makeJuggernaut();
					e.makeJuggernaut();
				}
				otherPlayers.put(e.getId(), e);	
				stage.addActor(e);
			}else {
				EnemyPlayer e = otherPlayers.get(ed.getId());
				if(e.getTeamNum() != 0 && ed.getTeamNum() == 0) {
					e.updateEnemy(ed);
					e.makeJuggernaut();
				}else if(e.getTeamNum() == 0 && ed.getTeamNum() != 0) {
					e.updateEnemy(ed);
					e.removeJuggernaut();
				}
				
			}
		}
		//Check if you should be juggernaut
		if(gameData.getPlayerData().getTeamNum() == 0 && player.getTeam() != 0) {
			player.makeJuggernaut();
		//Check if you shouldn't be juggernaut
		}else if (gameData.getPlayerData().getTeamNum() != 0 && player.getTeam() == 0) {
			player.removeJuggernaut();
		}
	}
	
	
	

}
