package entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;


import data.Ship;

/**
 * Entity which represents the player in the game.
 */
public class Player extends Actor {
	private Ship ship;
	private Texture texture = new Texture(Gdx.files.internal("Blue/spaceship_enemy.png"));
	private TextureRegion texture_region = new TextureRegion(texture);
	private BitmapFont bmf = new BitmapFont();
	private float degrees = 0;
	private Vector2 direction = new Vector2();
	boolean spaceBrakesOn = true;
	private Reticle ret;
	private int id;
	private int team;
	private boolean isJug = false;
	//Projectiles
	private ArrayList<Projectile> newProjectile = new ArrayList<Projectile>();
	private float fireDelay;	//Fire rate
	
	// Trying to fix acceleration
	private float acelX = 0, acelY = 0;
	
	/**
	 * Constructor which takes in a matchid and creates the player at pos)
	 * @param id the match id of the player
	 */
	public Player(int id, int team, Vector2 pos, String name) {
		setPosition(pos.x, pos.y);
		ship = new Ship();
		ship.calcStats();
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother rendering
		setSize(80, 64);
		scaleBy(0.5f);
		setOrigin(getWidth()/2, getHeight()/2);
		fireDelay= 0.3f;
		this.id = id;
		this.team = team;
		if(name != null) {
			setName(name);
		}else {
			setName("Player");
		}
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
		
		//Check for out of bounds
		
		// Shoot projectiles
		fireDelay -= delta;
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && fireDelay <= 0) {
			if(isJug) {
				Projectile p = new Projectile(getPosition(), degrees + 90, ret, id, team, ship.getDamage() + 20, ship.getRange());
				//Left
				p.setDirection(new Vector2 (p.getDirection().x, p.getDirection().y + 90));
				newProjectile.add(p);
				//Center
				p = new Projectile(getPosition(), degrees, ret, id, team, ship.getDamage() + 20, ship.getRange());
				p.setDirection(new Vector2 (p.getDirection().x, p.getDirection().y));
				newProjectile.add(p);
				//Right
				p = new Projectile(getPosition(), degrees, ret, id, team, ship.getDamage() + 20, ship.getRange());
				p.setDirection(new Vector2 (p.getDirection().x - 90, p.getDirection().y - 90));
				newProjectile.add(p);
			}else {
				newProjectile.add(new Projectile(getPosition(), degrees, ret, id, team, ship.getDamage(), ship.getRange()));
			}
			fireDelay = 0.3f;
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
	 * Update the player to be the Juggernaut
	 */
	public void makeJuggernaut() {
		setSize(160,128);
		ship.setHealth(Ship.JUGGERNAUT);
		ship.setShield(Ship.JUGGERNAUT);
		setOrigin(getWidth()/2, getHeight()/2);
		team = 0;
		isJug = true;
	}
	
	/**
	 * Update the player to not be the Juggernaut
	 */
	public void removeJuggernaut() {
		setSize(80,64);
		setOrigin(getWidth()/2, getHeight()/2);
		ship.setHealth(100);
		ship.setShield(100);
		team = 1;
		isJug = false;
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
		ship.setHealth(100);
		ship.setShield(100);
	}
	
	/**
	 * Draw the player
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture_region, getX() - getWidth()/2, getY() - getHeight()/2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		bmf.draw(batch, getName(), getX() - getName().length()*3, getY() + 100);
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
	public ArrayList<Projectile> getNewProjectile() {
		return newProjectile;
	}
	
	/**
	 * Set the recently fired projectile to null
	 */
	public void resetNewProjectile() {
		newProjectile.clear();;
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

	/**
	 * @return the players team number
	 */
	public int getTeam() {
		return team;
	}
	
}
