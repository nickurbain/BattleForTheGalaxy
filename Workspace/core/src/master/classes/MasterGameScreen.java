package master.classes;

import java.net.UnknownHostException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import battle.galaxy.HUDElements;
import data.GameData;
import entities.Player;
import entities.Projectile;

public class MasterGameScreen extends MasterScreen{
	
	public final static int SCREEN_WIDTH = 1600;
	public final static int SCREEN_HEIGHT = 900;
	public final static int BG_WIDTH = 2560;
	public final static int BG_HEIGHT = 1600;
	
	//Graphical
	private HUDElements hud;
	//Map Stuff
	private int mapSize;
	private Vector2[][] backgroundTiles;
	private Vector2[] respawnPoints;
	//Entities
	private Player player;
	private HashMap<Integer, Actor> entities = new HashMap<Integer, Actor>();
	private HashMap<Integer, Projectile> projectiles = new HashMap<Integer, Projectile>();
	//Data
	private GameData gameData;
	
	public MasterGameScreen(int gameType, int mapSize) throws UnknownHostException {
		super(game, "space-tile.jpg", "clean-crispy-ui.json");
		this.setMapSize(mapSize);
		
		backgroundTiles = new Vector2[mapSize][mapSize];
		for(int i = 0; i < mapSize; i++) {
			for(int j = 0; j < mapSize; j++) {
				backgroundTiles[i][j] = new Vector2(BG_WIDTH*i, BG_HEIGHT*j);
			}
		}
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.05F, 0.05F, 0.05F, 0.05F);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);

		game.getBatch().begin();
		for(int i = 0; i < mapSize; i++) {
			for(int j = 0; j < mapSize; j++) {
				game.getBatch().draw(background, backgroundTiles[i][j].x, backgroundTiles[i][j].y);
			}
		}
		game.getBatch().end();

		// Stage
		stage.act();
		stage.draw();
		
		update();
	}
	
	/**
	 * Update all entities and data in the game
	 */
	private void update() {
		//TODO
	}

	/**
	 * @return the mapSize
	 */
	public int getMapSize() {
		return mapSize;
	}

	/**
	 * @param mapSize the mapSize to set
	 */
	public void setMapSize(int mapSize) {
		this.mapSize = mapSize;
	}

}
