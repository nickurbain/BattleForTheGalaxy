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
	private float dx;
	private float dy;
	boolean spaceBrakesOn = true;
	ArrayList<Projectile> projectiles = new ArrayList<Projectile>(); //ArrayList for players projectiles
	private Projectile newProjectile;
	float fireDelay; // Projectile fire rate
	Reticle ret;
	
	int health = 100;
	
	// Trying to fix acceleration
	float acelX = 0, acelY = 0;
	
	public Player() {
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother rendering
		//setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
		setSize(80, 64);
		setOrigin(getWidth()/2, getHeight()/2);
		fireDelay= 0.3f;
	}
	
	@Override
	public void act(float delta) {
		float maxspeed = 800;
		
		if(Gdx.input.isKeyPressed(Keys.W)) {	// Towards reticle
			dx = (ret.getX() + ret.getWidth()/2 - getX());
			dy = (ret.getY() + ret.getHeight()/2 - getY());
			float dirL = (float) Math.sqrt(dx * dx + dy * dy);
			
			if(acelX < 1)
				acelX += .01;
			if(acelY < 1)
				acelY += .01;
			
			dx = dx/dirL*maxspeed * acelX;
			dy = dy/dirL*maxspeed * acelY;
			
		}
		else if(Gdx.input.isKeyPressed(Keys.S)) {	// Away from reticle
			dx = (getX() - ret.getX() - ret.getWidth()/2);
			dy = (getY() - ret.getY() - ret.getHeight()/2);
			float dirL = (float) Math.sqrt(dx * dx + dy * dy);
			
			if(acelX < 1)
				acelX += .01;
			if(acelY < 1)
				acelY += .01;
			
			dx = dx/dirL*maxspeed * acelX;
			dy = dy/dirL*maxspeed * acelY;
			
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {	// Rotate clockwise of reticle
			dx = (getX() - ret.getX() - ret.getWidth()/2);
			dy = (getY() - ret.getY() - ret.getHeight()/2);
			float dirL = (float) Math.sqrt(dx * dx + dy * dy);
			dx = dx/dirL;
			dy = dy/dirL;
			
			dy = dy*maxspeed;
			dx = -dx*maxspeed;
			
		}
		else if(Gdx.input.isKeyPressed(Keys.D)) {	// Rotate counterclockwise of reticle
			if(dx < 300) {
				dx += 50;
			}
			
		}
		//Actually move the ship
		moveBy(dx*delta, dy*delta);
		
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
		if(dx > 0) {
			dx *= .99f;
		}
		if(dy > 0) {
			dy *= .99f;
		}
		if(dx < 0) {
			dx /= 1.01f;
		}
		if(dy < 0) {
			dy /= 1.01f;
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
		return new Vector2(dx, dy);
	}
	
	public float getDx() {
		return dx;
	}
	public float getDy() {
		return dy;
	}
	
	public Projectile getNewProjectile() {
		return newProjectile;
	}
	
	public void setNewProjectile() {
		newProjectile = null;
	}
	
}
