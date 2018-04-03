package master.classes;

import java.net.UnknownHostException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.FitViewport;

import battle.galaxy.HUDElements;
import data.GameData;
import entities.EnemyPlayer;
import entities.Player;
import entities.Projectile;
import entities.Reticle;

public class MasterGameScreen extends MasterScreen{
	
	public final static int SCREEN_WIDTH = 1600;
	public final static int SCREEN_HEIGHT = 900;
	public final static int BG_WIDTH = 2560;
	public final static int BG_HEIGHT = 1600;
	
	//Graphical
	private HUDElements hud;
	private Reticle reticle;
	Vector3 mouse = new Vector3();
	//Map Stuff
	private int mapSize;
	private Vector2[][] backgroundTiles;
	private Vector2[] respawnPoints;
	//Entities
	private Player player;
	private HashMap<Integer, EnemyPlayer> otherPlayers = new HashMap<Integer, EnemyPlayer>();
	private HashMap<Integer, Projectile> projectiles = new HashMap<Integer, Projectile>();
	//Data
	private GameData gameData;
	private int gameType;
	
	public MasterGameScreen(int gameType, int mapSize) throws UnknownHostException {
		super(game, "space-tile.jpg", "clean-crispy-ui.json");
		this.gameType = gameType;
		this.setMapSize(mapSize);
		//Setup the background
		backgroundTiles = new Vector2[mapSize][mapSize];
		for(int i = 0; i < mapSize; i++) {
			for(int j = 0; j < mapSize; j++) {
				backgroundTiles[i][j] = new Vector2(BG_WIDTH*i, BG_HEIGHT*j);
			}
		}
		//Setup stage with player and reticle
		stage.setViewport(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
		player = new Player(game.getDataController());
		reticle = new Reticle();
		stage.addActor(player);
		stage.addActor(reticle);
		player.setPosition(mapSize*BG_WIDTH/2, mapSize*BG_HEIGHT/2);
		reticle.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		
		//Cursor/input
		Cursor customCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("transparent-1px.png")), 0, 0);
		Gdx.graphics.setCursor(customCursor);
		Gdx.input.setCursorCatched(false);
		Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		Gdx.input.setInputProcessor(stage);
		
		gameData = new GameData(player.getId(), player.getPosition(), player.getRotation());
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.05F, 0.05F, 0.05F, 0.05F);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);
		
		mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(mouse);

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
		reticle.update(mouse);
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
