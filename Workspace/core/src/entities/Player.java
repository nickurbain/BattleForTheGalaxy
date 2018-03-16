package entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor {
	
	private Texture texture = new Texture(Gdx.files.internal("main-ship.png"));
	private TextureRegion texture_region = new TextureRegion(texture);
	float degrees = 0;
	private Vector2 direction = new Vector2();
	boolean spaceBrakesOn = true;
	private Reticle ret;
	
	//Projectiles
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>(); //ArrayList for players projectiles
	private Projectile newProjectile;
	private float fireDelay;	//Fire rate
	
	private int id;
	
	private int health = 100;
	private int shield = 100;
	private int hull = 100;
	
	// Trying to fix acceleration
	private float acelX = 0, acelY = 0;
	
	public Player() {
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother rendering
		//setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
		setSize(80, 64);
		setOrigin(getWidth()/2, getHeight()/2);
		fireDelay= 0.3f;
		//id = this.hashCode();
		id = 1;
	}
	
	@Override
	public void act(float delta) {
		float maxspeed = 800;
		
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
		
		if(Gdx.input.isKeyJustPressed(Keys.F)) {
			System.out.println(health);
			health = health - 10;
		}
		
		//Actually move the ship
		moveBy(direction.x*delta, direction.y*delta);
		
		// "Air brakes" for space (user toggles it with "C")
		if(spaceBrakesOn) {
			if(acelX > 0)
				acelX *= .99;
//			else if(acelX < 1)
//				acelX += .1;
			if(acelY > 0)
				acelY *= .99;
//			else if(acelY < 1)
//				acelY += .1;
		}
		
		
		//Slow down ship
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
		
		
		// Shoot projectiles
		fireDelay -= delta;
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && fireDelay <= 0) {
			newProjectile = new Projectile(getX(), getY(), degrees, ret);
			System.out.println(newProjectile.getId());
			projectiles.add(newProjectile);
			fireDelay = 0.3f;			
		}
		
	}
	
	/**
	 * Check whether the player is out of game bounds and damage them if they are
	 */
	public void outOfBounds() {
		if(getX() > 40960 || getY() > 25600 || getX() < 0 || getY() < 0) {
			health -= 10;
			if(health <= 0) {
				System.exit(0);
			}
		}
	}
	
	/**
	 * Update the rotation of the player based on the position of the reticle
	 */
	public void updateRotation(float delta, Reticle ret) {
		degrees = (float) ((Math.atan2 (ret.getY() - getY() + ret.getHeight()/2, 	// offset by half-reticle 
				ret.getX() - getX() + ret.getWidth()/2 ) * 180.0 / Math.PI));		// to center ship with reticle
		setRotation(degrees);
		
		if(this.ret == null) {
			this.ret = ret;
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture_region, getX() - getWidth()/2, getY() - getHeight()/2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	public Vector2 getPosition() {
		return new Vector2(getX(), getY());
	}
	
	public Vector2 getDirection() {
		return direction;
	}
	
	public Projectile getNewProjectile() {
		return newProjectile;
	}
	
	public void setNewProjectile() {
		newProjectile = null;
	}

	public int getId() {
		return id;
	}

	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public int getShield() {
		return shield;
	}

	public void setShield(int shield) {
		this.shield = shield;
	}

	public int getHull() {
		return hull;
	}

	public void setHull(int hull) {
		this.hull = hull;
	}
	
}
