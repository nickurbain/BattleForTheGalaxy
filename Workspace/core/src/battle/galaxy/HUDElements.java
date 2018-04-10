package battle.galaxy;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
	private Rectangle bg;
	//Chat
	private TextField chatInput;
	private CustomLabel killFeed;
	
	public HUDElements(BattleForTheGalaxy game) {
		this.game = game;
		
		bmf = new BitmapFont();
		bmf.setColor(Color.WHITE);
		//Status bars
		shapeRenderer = new ShapeRenderer();
		health = new Rectangle(GameScreen.SCREEN_WIDTH/2 - 100, 30, 15, 200);
		shield = new Rectangle(GameScreen.SCREEN_WIDTH/2 - 100, 10, 15, 200);
		bg = new Rectangle(GameScreen.SCREEN_WIDTH/2 - 110, 0, 50, 220);
		
		//Chat
		chatInput = new TextField("", game.skin);
		chatInput.setPosition(0, 0);
		chatInput.setWidth(500);
		
		//Kill Feed
		killFeed = new CustomLabel("", game.skin);
	}
	
	/**
	 * Update the health, shield, and hull of the player for display
	 * @param player The current player object passed to the HUD
	 */
	public void updateHUD(Player player) {
		updateHealth(player.getShip().getHealth());
		updateShield(player.getShip().getShield());
	}
	
	/**
	 * Draw the HUD
	 * @param gameData The GameData object holding all the game's data/stats
	 * @param player The current player object passed to the HUD
	 */
	public void drawHUD(GameData gameData, Player player) {
		updateHUD(player);
		killFeed.updateText(gameData.getRecentKill());
		bmf.draw(game.batch, convertTime(gameData.getGameTime()), gameData.getPlayerData().getPosition().x - 20, 
				gameData.getPlayerData().getPosition().y + GameScreen.SCREEN_HEIGHT/2 - 20);
		bmf.draw(game.batch, "X: " + (int)gameData.getPlayerData().getPosition().x/100 + " | Y: " + (int)gameData.getPlayerData().getPosition().y/100, 
				gameData.getPlayerData().getPosition().x + 20, gameData.getPlayerData().getPosition().y + GameScreen.SCREEN_HEIGHT/2 - 20);
		bmf.draw(game.batch, gameData.getRecentKill(), GameScreen.SCREEN_WIDTH - 50, 10);
		chatInput.draw(game.batch, 1);
		game.batch.end();
		
		shapeRenderer.begin(ShapeType.Filled);
		
		shapeRenderer.setColor(Color.GRAY);
		shapeRenderer.rect(bg.x, bg.y, bg.getHeight(), bg.getWidth());
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(health.x, health.y, health.getHeight(), health.getWidth());
		
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(shield.x, shield.y, shield.getHeight(), shield.getWidth());
		
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
	
	public Label getKillFeed() {
		return killFeed;
	}
	
	public class CustomLabel extends Label{
	    private String text;

	    public CustomLabel(final CharSequence text, final Skin skin) {
	        super(text, skin);
	        this.text = text.toString();
	    }

	    @Override
	    public void act(final float delta) {
	        this.setText(text);
	        super.act(delta);
	    }

	    public void updateText(final String text) {
	        this.text = text;
	    }
	}
}
