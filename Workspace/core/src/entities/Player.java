package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import battle.galaxy.GameScreen;
import controllers.DataController;
import data.Ship;

/**
 * Entity which represents the player in the game.
 */
public class Player extends Actor {
	private Ship ship;
	private Texture texture = new Texture(Gdx.files.internal("Blue/spaceship_enemy.png"));
	private TextureRegion texture_region = new TextureRegion(texture);
	private float degrees = 0;
	private Vector2 direction = new Vector2();
	boolean spaceBrakesOn = true;
	private Reticle ret;
	private int id;
	//Projectiles
	private Projectile newProjectile;
	private float fireDelay;	//Fire rate
	
	// Trying to fix acceleration
	private float acelX = 0, acelY = 0;
	
	/**
	 * Constructor which takes in a matchid and creates the player at (0,0)
	 * @param id the match id of the player
	 */
	public Player(int id, Vector2 pos) {
		//Load ship data from local
		ship = new Ship();
		ship.calcStats();
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother rendering
		setSize(80, 64);
		scaleBy(0.5f);
		setOrigin(getWidth()/2, getHeight()/2);
		fireDelay= 0.3f;
		//id = (int) System.currentTimeMillis(); // USE FOR BROADCAST SERVER TESTING
		this.id = id;
	}
	
	/**
	 * Move the player based on speed and direciton. Also listen for input for moving and firing projectiles.
	 */
	@Override
	public void act(float delta) {
		float maxspeed = ship.getVelocity();
		
		if(Gdx.input.isKeyPressed(Keys.W)) {	// Towards reticle
			direction.x = (ret.getX() + ret.getWidth()/2 - getX());
			direction.y = (ret.getY() + ret.getHeight()/2 - getY());
			float dirL = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
			
			if(acelX < 1)
				acelX += .01;
			if(acelY < 1)
				acelY += .01;
			
			direction.x = direction.x/dirL*maxspeed * acelX;
			direction.y = direction.y/dirL*maxspeed * acelY;
			
		}
		else if(Gdx.input.isKeyPressed(Keys.S)) {	// Away from reticle
			direction.x = (getX() - ret.getX() - ret.getWidth()/2);
			direction.y = (getY() - ret.getY() - ret.getHeight()/2);
			float dirL = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
			
			if(acelX < 1)
				acelX += .01;
			if(acelY < 1)
				acelY += .01;
			
			direction.x = direction.x/dirL*maxspeed * acelX;
			direction.y = direction.y/dirL*maxspeed * acelY;
			
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {	// Rotate clockwise of reticle
			direction.x = (getX() - ret.getX() - ret.getWidth()/2 );
			direction.y = (getY() - ret.getY() - ret.getHeight()/2);
			
			direction = new Vector2(direction.y, -direction.x);
			
			float dirL = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
			
			if(acelX < 1)
				acelX += .01;
			if(acelY < 1)
				acelY += .01;
			
			direction.x = direction.x/dirL*maxspeed * acelX;
			direction.y = direction.y/dirL*maxspeed * acelY;
			
		}
		else if(Gdx.input.isKeyPressed(Keys.D)) {	// Rotate counterclockwise of reticle
			direction.x = (getX() - ret.getX() - ret.getWidth()/2 );
			direction.y = (getY() - ret.getY() - ret.getHeight()/2);
			
			direction = new Vector2(-direction.y, direction.x);
			
			float dirL = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
			
			if(acelX < 1)
				acelX += .01;
			if(acelY < 1)
				acelY += .01;
			
			direction.x = direction.x/dirL*maxspeed * acelX;
			direction.y = direction.y/dirL*maxspeed * acelY;
			
		}
		
		//Actually move the ship
		moveBy(direction.x*delta, direction.y*delta);
		
		//Slow down ship
		if(spaceBrakesOn) {
			if(direction.x > 0) {
				direction.x *= .99f;
			}
			if(direction.y > 0) {
				direction.y *= .99f;
			}
			if(direction.x < 0) {
				direction.x /= 1.01f;
			}
			if(direction.y < 0) {
				direction.y /= 1.01f;
			}
			
			if((direction.x > 0 && direction.x < 40) || (direction.x < 0 && direction.x > -40)) {
				direction.x = 0;
			}
			if((direction.y > 0 && direction.y < 40) || (direction.y < 0 && direction.y > -40)) {
				direction.y = 0;
			}
		}
		
		
		// Shoot projectiles
		fireDelay -= delta;
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && fireDelay <= 0) {
			newProjectile = new Projectile(getPosition(), degrees, ret, id, ship.getDamage(), ship.getRange());
			fireDelay = 0.3f;
		}
	}
	
	/**
	 * Check whether the player is out of game bounds and damage them if they are
	 */
	public void outOfBounds() {
		if(getX() > 40960 || getY() > 25600 || getX() < 0 || getY() < 0) {
			ship.dealDamage(10);
			if(ship.getHealth() <= 0) {
				System.exit(0);
			}
		}
	}
	
	/**
	 * Update the rotation of the player based on the position of the reticle
	 * @param delta The libGDX delta used for drawing frames
	 * @param ret The Reticle object used
	 */
	public void updateRotation(float delta, Reticle ret) {
		degrees = (float) ((Math.atan2 (ret.getY() - getY() + ret.getHeight()/2, 	// offset by half-reticle 
				ret.getX() - getX() + ret.getWidth()/2 ) * 180.0 / Math.PI));		// to center ship with reticle
		setRotation(degrees - 90);
		
		if(this.ret == null) {
			this.ret = ret;
		}
	}
	
	/**
	 * Reset this player at a respawn point
	 * @param respawnPoint
	 */
	public void reset(Vector2 respawnPoint) {
		setPosition(respawnPoint.x, respawnPoint.y);
		direction.x = 0;
		direction.y = 0;
		spaceBrakesOn = true;
		fireDelay = 0.3f;
	}
	
	/**
	 * Draw the player
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture_region, getX() - getWidth()/2, getY() - getHeight()/2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	/**
	 * @return the position of the player
	 */
	public Vector2 getPosition() {
		return new Vector2(getX(), getY());
	}
	
	/**
	 * @return the direction of the player
	 */
	public Vector2 getDirection() {
		return direction;
	}
	
	/**
	 * @return the most recently fired projectile
	 */
	public Projectile getNewProjectile() {
		return newProjectile;
	}
	
	/**
	 * Set the recently fired projectile to null
	 */
	public void resetNewProjectile() {
		newProjectile = null;
	}

	/**
	 * @return the matchId
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Set the matchId
	 * @param i the id to set
	 */
	public void setId(int i) {
		id = i;
	}
	
	/**
	 * @return the player's ship
	 */
	public Ship getShip() {
		return ship;
	}
	
}
