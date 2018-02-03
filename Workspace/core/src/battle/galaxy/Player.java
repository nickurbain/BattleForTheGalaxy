package battle.galaxy;

import com.badlogic.gdx.Gdx;
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
	boolean thrusterOn[] = {false,false,false,false};	// North,South,West,East
	
	public Player() {
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother rendering
//		setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
		setSize(100, 80);
		setOrigin(getWidth()/2, getHeight()/2);
	}
	
	@Override
	public void act(float delta) {
		if(Gdx.input.isKeyPressed(Keys.W)) {	// North
//			moveBy(0, velocity[0] * delta);
			thrusterOn[0] = true;
			velocity[0] += 20;
		}
		else if(Gdx.input.isKeyPressed(Keys.S)) {	// South
//			moveBy(0, -velocity[0] * delta);
			thrusterOn[1] = true;
			velocity[0] -= 20;
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {	// West
//			moveBy(-velocity[0] * delta, 0);
			thrusterOn[2] = true;
			velocity[1] -= 20;
		}
		else if(Gdx.input.isKeyPressed(Keys.D)) {	// East
//			moveBy(velocity[0] * delta, 0);
			thrusterOn[3] = true;
			velocity[1] += 20;
		}
		
		moveBy(velocity[1] * delta, velocity[0] * delta);
		
		// Reset thrusters before next calculation
		for(int i = 0; i < 3; i++) {
			thrusterOn[i] = false;
		}
		
	}
	
	public void updateRotation(float delta, Reticle ret) {
		degrees = (float) ((Math.atan2 (ret.getY() - getY() + ret.getHeight()/2, 	// offset by half-reticle 
				ret.getX() - getX() + ret.getWidth()/2 ) * 180.0 / Math.PI));		// to center ship with reticle
		setRotation(degrees);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture_region, getX() - getWidth()/2, getY() - getHeight()/2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
}
