package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import data.PlayerData;
import data.Ship;
 
/**
 * Entity which represents another client in the game.
 */
public class EnemyPlayer extends Actor{
	
	Texture textureEnemy = new Texture(Gdx.files.internal("Red/spaceship_enemy_red.png"));
	Texture textureFriendly = new Texture(Gdx.files.internal("Blue/spaceship_enemy.png"));
	TextureRegion textureRegion;
	BitmapFont bmf = new BitmapFont();
	
	private Vector2 direction;
	private float rotation;
	private int id;
	private int teamNum;
	private boolean isJuggernaut = false;
	
	private Ship ship;
	
	/**
	 * Constructor which takes in the neccessary parameters.
	 * @param id the id of the player
	 * @param position the position of the player
	 * @param direction the direction of the player
	 * @param rotation the rotation of the player
	 */
	public EnemyPlayer(int id, Vector2 position, Vector2 direction, float rotation, int teamNum, int playerTeam) {
		setPosition(position.x, position.y);
		this.direction = new Vector2(direction);
		this.rotation = rotation;
		this.id = id;
		this.teamNum = teamNum;
		if(playerTeam == teamNum && playerTeam != -1) {
			textureRegion = new TextureRegion(textureFriendly);
		}else {
			textureRegion = new TextureRegion(textureEnemy);
		}
		setSize(80, 64);
		scaleBy(0.5f);
		setOrigin(getWidth()/2, getHeight()/2);
	}

	
	/**
	 * Constructor which takes in a PlayerData object
	 * @param ed the PlayerData object
	 */
	public EnemyPlayer(PlayerData ed, int playerTeam) {
		setPosition(ed.getPosition().x, ed.getPosition().y);
		this.direction = new Vector2(ed.getDirection());
		this.rotation = ed.getRotation();
		this.id = ed.getId();
		this.teamNum = ed.getTeamNum();
		setName(ed.getPlayerName());
		if(playerTeam == teamNum && teamNum != -1) {
			textureRegion = new TextureRegion(textureFriendly);
			bmf.setColor(Color.GREEN);
		}else {
			textureRegion = new TextureRegion(textureEnemy);
			bmf.setColor(Color.RED);
		}
		setSize(80, 64);
		scaleBy(0.5f);
		setOrigin(getWidth()/2, getHeight()/2);
	}
	
	/**
	 * Slow down and move the enemyplayer. Called by the stage.
	 */
	public void act(float delta) {
		float velocity = 800;
		
		//float dirL = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
		//direction.x = direction.x/dirL * velocity;
		//direction.y = direction.y/dirL * velocity;
		
		//Slow down ship
		if(direction.x > 0) {
			direction.x = direction.x *.98f;
		}
		if(direction.y > 0) {
			direction.y = direction.y * .98f;
		}
		if(direction.x < 0) {
			direction.x = direction.x/1.02f;
		}
		if(direction.y < 0) {
			direction.y = direction.y/1.02f;
		}
			
		//move the ship
		moveBy(direction.x*delta, direction.y*delta);
		
	}
	
	/**
	 * Update the player with data from server.
	 * @param position the position of the player
	 * @param direction the direction of the player
	 * @param rotation the rotation of the player
	 */
	public void updateEnemy(Vector2 position, Vector2 direction, float rotation) {
		if(direction != null) {
			this.direction.set(direction);
		}
		if(position != null) {
			Vector2 dist = new Vector2();
			dist.x = (float) Math.pow(position.x - getX(), 2);
			dist.y = (float) Math.pow(position.y - getY(),2);
			if(Math.sqrt(dist.x + dist.y) > 50) {
				setPosition(position.x, position.y);
			}
		}
		if(rotation != 0) {
			this.rotation = rotation;
		}
	}
	
	/**
	 * Update the player with data from the server.
	 * @param ed the PlayerData object.
	 */
	public void updateEnemy(PlayerData ed) {
		if(ed.getDirection() != null) {
			this.direction.set(ed.getDirection());
		}
		if(ed.getPosition() != null) {
			Vector2 dist = new Vector2();
			dist.x = (float) Math.pow(ed.getPosition().x - getX(), 2);
			dist.y = (float) Math.pow(ed.getPosition().y - getY(),2);
			if(Math.sqrt(dist.x + dist.y) > 50) {
				setPosition(ed.getPosition().x, ed.getPosition().y);
			}
		}
		if(ed.getRotation() != 0) {
			this.rotation = ed.getRotation();
		}
		
		teamNum = ed.getTeamNum();
	}
	
	/**
	 * Update the player to be the Juggernaut
	 */
	public void makeJuggernaut() {
		isJuggernaut = true;
		setScale(0.75f);
		ship.setHealth(Ship.JUGGERNAUT);
		ship.setShield(Ship.JUGGERNAUT);
	}
	
	public void removeJuggernaut() {
		isJuggernaut = false;
		setScale(0.5f);
		ship.setHealth(100);
		ship.setShield(100);
	}
	
	/**
	 * Reset the player's direciton to 0
	 */
	public void reset() {
		direction = new Vector2(0,0);
		setScale(0.5f);
		ship.setHealth(100);
		ship.setShield(100);
		isJuggernaut = false;
	}
	
	/**
	 * Draw the player
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(textureRegion, getX() - getWidth()/2, getY() - getHeight()/2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		bmf.draw(batch, getName(), getX() - getWidth()/2, getY() + 100);
	}
	
	/**
	 * @return the position of the player
	 */
	public Vector2 getPosition() {
		return getPosition();
	}
	
	/**
	 * @return the direction of the player
	 */
	public Vector2 getDirection() {
		return direction;
	}
	
	/**
	 * @return the rotation of the player
	 */
	public float getRotation() {
		return rotation;
	}
	
	/**
	 * @return the id of the player
	 */
	public int getId() {
		return id; 
	}

	/**
	 * @return the ship
	 */
	public Ship getShip() {
		return ship;
	}

	/**
	 * @param ship the ship to set
	 */
	public void setShip(Ship ship) {
		this.ship = ship;
	}

	/**
	 * @return the teamNum
	 */
	public int getTeamNum() {
		return teamNum;
	}

	/**
	 * @param teamNum the teamNum to set
	 */
	public void setTeamNum(int teamNum) {
		this.teamNum = teamNum;
	}

	/**
	 * @return the isJuggernaut
	 */
	public boolean isJuggernaut() {
		return isJuggernaut;
	}

	/**
	 * @param isJuggernaut the isJuggernaut to set
	 */
	public void setJuggernaut(boolean isJuggernaut) {
		this.isJuggernaut = isJuggernaut;
	}
	
}
