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
	int velocity[] = {0,0};			// +North/-South, -West/+East
	boolean spaceBrakesOn = true;
	ArrayList<Projectile> projectiles = new ArrayList<Projectile>(); //Array for projectiles
	float fireDelay; // Projectile fire rate
	Reticle ret;
	
	public Player() {
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother rendering
//		setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
		setSize(100, 80);
		setOrigin(getWidth()/2, getHeight()/2);
		fireDelay= 0.3f;
	}
	
	@Override
	public void act(float delta) {
		if(Gdx.input.isKeyPressed(Keys.W)) {	// North
			if(velocity[0] < 800)
				velocity[0] += 30;
		}
		else if(Gdx.input.isKeyPressed(Keys.S)) {	// South
			if(velocity[0] > -800)
				velocity[0] -= 30;
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {	// West
			if(velocity[1] > -800)
				velocity[1] -= 30;
		}
		else if(Gdx.input.isKeyPressed(Keys.D)) {	// East
			if(velocity[1] < 800)
				velocity[1] += 30;
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.C)) { // Space brakes
			if(spaceBrakesOn)
				spaceBrakesOn = false;
			else
				spaceBrakesOn = true;
		}
		
		// Shoot projectiles
		fireDelay -= delta;
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && fireDelay <= 0) {
				Projectile p = new Projectile(getX(), getY(), degrees, ret);
				projectiles.add(p);
				fireDelay = 0.3f;
				
		}
		
		// "Air brakes" for space (user toggles it with "C")
		if(spaceBrakesOn) {
			for(int i = 0; i < 2; i++) {
				if(velocity[i] > 0)
					velocity[i] -= 5;
				else if(velocity[i] < 0)
					velocity[i] += 5;
			}
		}
		
		// Update Projectiles and remove if neccessary
		moveBy(velocity[1] * delta, velocity[0] * delta);
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
		this.ret = ret;
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
