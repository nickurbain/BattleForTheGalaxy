package battle.galaxy;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

import data.GameData;

public class HUDElements {
	//Time
	private BitmapFont bmf;
	//Health, Shield, Hull
	private ShapeRenderer shapeRenderer;
	private Rectangle health;
	private Rectangle shield;
	private Rectangle hull;
	private Rectangle bg;
	
	public HUDElements() {
		bmf = new BitmapFont();
		bmf.setColor(Color.WHITE);
		
		shapeRenderer = new ShapeRenderer();
		
		health = new Rectangle(GameScreen.SCREEN_WIDTH/2 - 100, 50, 15, 200);
		shield = new Rectangle(GameScreen.SCREEN_WIDTH/2 - 100, 30, 15, 200);
		hull = new Rectangle(GameScreen.SCREEN_WIDTH/2 - 100, 10, 15, 200);
		bg = new Rectangle(GameScreen.SCREEN_WIDTH/2 - 110, 0, 70, 220);
	}
	
	public void updateHUD(GameData gameData) {
		updateHealth(gameData.getPlayerData().getHealth());
		updateShield(gameData.getPlayerData().getShield());
		updateHull(gameData.getPlayerData().getHull());
	}
	
	public void drawHUD(Batch batch, GameData gameData) {
		bmf.draw(batch, convertTime(gameData.getGameTime()), gameData.getPlayerData().getPosition().x, 
				gameData.getPlayerData().getPosition().y + GameScreen.SCREEN_HEIGHT/2 - 20);
		batch.end();
		
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
	
	public void updateHull(int hull) {
		this.hull.height = this.hull.height - (this.hull.height - (hull*2));
	}
	
}
