package battle.galaxy;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import data.GameData;
import entities.Player;

public class HUDElements {
	BattleForTheGalaxy game;
	//Time
	private BitmapFont bmf;
	//Status bars
	private ShapeRenderer shapeRenderer;
	private Rectangle health;
	private Rectangle shield;
	private Rectangle hull;
	private Rectangle bg;
	//Chat
	private TextField chatInput;
	
	public HUDElements(BattleForTheGalaxy game) {
		this.game = game;
		
		bmf = new BitmapFont();
		bmf.setColor(Color.WHITE);
		//Status bars
		shapeRenderer = new ShapeRenderer();
		health = new Rectangle(GameScreen.SCREEN_WIDTH/2 - 100, 50, 15, 200);
		shield = new Rectangle(GameScreen.SCREEN_WIDTH/2 - 100, 30, 15, 200);
		hull = new Rectangle(GameScreen.SCREEN_WIDTH/2 - 100, 10, 15, 200);
		bg = new Rectangle(GameScreen.SCREEN_WIDTH/2 - 110, 0, 70, 220);
		
		//Chat
		chatInput = new TextField("", game.skin);
		chatInput.setPosition(0, 0);
		chatInput.setWidth(500);
		
	}
	
	/**
	 * Update the health, shield, and hull of the player for display
	 * @param gameData
	 */
	public void updateHUD(GameData gameData) {
		updateHealth(gameData.getPlayerData().getHealth());
		updateShield(gameData.getPlayerData().getShield());
	}
	
	/**
	 * Draw the HUD
	 * @param gameData
	 */
	public void drawHUD(GameData gameData) {
		updateHUD(gameData);
		bmf.draw(game.batch, convertTime(gameData.getGameTime()), gameData.getPlayerData().getPosition().x - 20, 
				gameData.getPlayerData().getPosition().y + GameScreen.SCREEN_HEIGHT/2 - 20);
		bmf.draw(game.batch, "X: " + (int)gameData.getPlayerData().getPosition().x/100 + " | Y: " + (int)gameData.getPlayerData().getPosition().y/100, 
				gameData.getPlayerData().getPosition().x + 20, gameData.getPlayerData().getPosition().y + GameScreen.SCREEN_HEIGHT/2 - 20);
		chatInput.draw(game.batch, 1);
		game.batch.end();
		
		shapeRenderer.begin(ShapeType.Filled);
		
		shapeRenderer.setColor(Color.GRAY);
		shapeRenderer.rect(bg.x, bg.y, bg.getHeight(), bg.getWidth());
		
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(health.x, health.y, health.getHeight(), health.getWidth());
		
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(shield.x, shield.y, shield.getHeight(), shield.getWidth());
		
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(hull.x, hull.y, hull.getHeight(), hull.getWidth());
		
		shapeRenderer.end();
	}
	
	/**
	 * Convert time in milliseconds to a min:sec format string
	 * @param millis
	 * @return
	 */
	private String convertTime(long millis) {
		return String.format("%d : %d", 
			    TimeUnit.MILLISECONDS.toMinutes(millis),
			    TimeUnit.MILLISECONDS.toSeconds(millis) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
			);
	}
	
	public void updateHealth(int health) {
		this.health.setHeight(this.health.height - (this.health.height - (health*2)));
	}
	
	public void updateShield(int shield) {
		this.shield.height = this.shield.height - (this.shield.height - (shield*2));
	}
}
