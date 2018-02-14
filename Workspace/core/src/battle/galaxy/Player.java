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
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor {
	
	Texture texture = new Texture(Gdx.files.internal("main-ship.png"));
	TextureRegion texture_region = new TextureRegion(texture);
	float degrees = 0;
	float dx;
	float dy;
	boolean spaceBrakesOn = true;
	ArrayList<Projectile> projectiles = new ArrayList<Projectile>(); //Array for projectiles
	float fireDelay; // Projectile fire rate
	Reticle ret;
	
	int health = 100;
	
	public Player() {
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother rendering
		//setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
		setSize(100, 80);
		setOrigin(getWidth()/2, getHeight()/2);
		fireDelay= 0.3f;
	}
	
	@Override
	public void act(float delta) {
		float velocity = 500;
		
		if(Gdx.input.isKeyPressed(Keys.W)) {	// North
			dx = (getX() - ret.getX() - ret.getWidth()/2);
			dy = (getY() - ret.getY() - ret.getHeight()/2);
			float dirL = (float) Math.sqrt(dx * dx + dy * dy);
			dx = dx/dirL;
			dy = dy/dirL;
			
			dy = -dy*velocity;
			dx = -dx*velocity;
		}
		else if(Gdx.input.isKeyPressed(Keys.S)) {	// South
			dx = (getX() - ret.getX() - ret.getWidth()/2);
			dy = (getY() - ret.getY() - ret.getHeight()/2);
			float dirL = (float) Math.sqrt(dx * dx + dy * dy);
			dx = dx/dirL;
			dy = dy/dirL;
			
			dy = dy*velocity;
			dx = dx*velocity;
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {	// West
			if(dx > -300) {
				dx -= 50;
			}
		}
		else if(Gdx.input.isKeyPressed(Keys.D)) {	// East
			if(dx < 300) {
				dx += 50;
			}
		}
		
		// Shoot projectiles
		fireDelay -= delta;
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && fireDelay <= 0) {
				Projectile p = new Projectile(getX(), getY(), degrees, ret);
				projectiles.add(p);
				fireDelay = 0.3f;
				
		}
		
		// Update Projectiles and remove if necessary
		moveBy(dx*delta, dy*delta);
		
		if(dx > 0) {
			dx = dx *.98f;
		}
		if(dy > 0) {
			dy = dy * .98f;
		}
		if(dx < 0) {
			dx = dx/1.02f;
		}
		if(dy < 0) {
			dy = dy/1.02f;
		}
		for(Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext();) {
			Projectile p = iter.next();
			if(p.remove()) {
				iter.remove();
			}else {
				p.act(delta);
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
		for(Projectile p: projectiles) {
			p.draw(batch, parentAlpha);
		}
	}
	
}
