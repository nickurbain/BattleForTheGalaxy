package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.math.Vector2;

import data.NewMatchData;
import master.classes.MasterGameScreen;
import master.classes.MasterScreen;

public class AllianceDeathMatchScreen extends MasterGameScreen{
	
	public final static int MAP_SIZE = 20480;
	
	private static Vector2[] respawnPoints = {
			new Vector2(MAP_SIZE/4, MAP_SIZE/2),
			new Vector2(MAP_SIZE - MAP_SIZE/4, MAP_SIZE/2)
	};

	public AllianceDeathMatchScreen() throws UnknownHostException {
		super(2, MAP_SIZE, respawnPoints);
	}

	@Override
	public NewMatchData joinMatch() {
		NewMatchData matchData = (NewMatchData) game.getDataController().sendToServerWaitForResponse(
				"{jsonOrigin:1,jsonType:12,matchType:" + gameType +  "alliance:" + MasterScreen.alliance + "}", false);
		return matchData;
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

}
