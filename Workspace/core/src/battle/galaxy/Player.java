package battle.galaxy;

import java.util.ArrayList;
import java.util.Iterator;

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
	
	Texture texture = new Texture(Gdx.files.internal("main-ship.png"));
	TextureRegion texture_region = new TextureRegion(texture);
	float degrees = 0;
	private Vector2 direction = new Vector2();
	boolean spaceBrakesOn = true;
	private Reticle ret;
	
	//Projectiles
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>(); //ArrayList for players projectiles
	private Projectile newProjectile;
	private float fireDelay;	//Fire rate
	
	private int id;
	
	int health = 100;
	
	public Player() {
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother rendering
		//setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
		setSize(100, 80);
		setOrigin(getWidth()/2, getHeight()/2);
		fireDelay= 0.3f;
		id = this.hashCode();
	}
	
	@Override
	public void act(float delta) {
		float velocity = 500;
		
		if(Gdx.input.isKeyPressed(Keys.W)) {	// North
			direction.x = (getX() - ret.getX() - ret.getWidth()/2);
			direction.y = (getY() - ret.getY() - ret.getHeight()/2);
			float dirL = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
			direction.x = direction.x/dirL;
			direction.y = direction.y/dirL;
			
			direction.y = -direction.y*velocity;
			direction.x = -direction.x*velocity;
		}
		else if(Gdx.input.isKeyPressed(Keys.S)) {	// South
			direction.x = (getX() - ret.getX() - ret.getWidth()/2);
			direction.y = (getY() - ret.getY() - ret.getHeight()/2);
			float dirL = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
			direction.x = direction.x/dirL;
			direction.y = direction.y/dirL;
			
			direction.y = direction.y*velocity;
			direction.x = direction.x*velocity;
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {	// West
			direction.x = (getX() - ret.getX() - ret.getWidth()/2);
			direction.y = (getY() - ret.getY() - ret.getHeight()/2);
			float dirL = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
			direction.x = direction.x/dirL;
			direction.y = direction.y/dirL;
			
			direction.y = direction.y*velocity;
			direction.x = -direction.x*velocity;
		}
		else if(Gdx.input.isKeyPressed(Keys.D)) {	// East
			if(direction.x < 300) {
				direction.x += 50;
			}
		}
		//Actually move the ship
		moveBy(direction.x*delta, direction.y*delta);
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
		
		
		// Shoot projectiles
		fireDelay -= delta;
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && fireDelay <= 0) {
			newProjectile = new Projectile(getX(), getY(), degrees, ret);
			projectiles.add(newProjectile);
			fireDelay = 0.3f;			
		}
		
	}
	
	public void outOfBounds() {
		if(getX() > 40960 || getY() > 25600 || getX() < 0 || getY() < 0) {
			health -= 10;
			if(health <= 0) {
				System.exit(0);
			}
		}
	}
	
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
		// Draw player projectiles
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
	
}
