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
	
	public Player() {
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother rendering
//		setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
		setSize(100, 80);
		setOrigin(getWidth()/2, getHeight()/2);
	}
	
	@Override
	public void act(float delta) {
		if(Gdx.input.isKeyPressed(Keys.W)) {
			moveBy(0, 500 * delta);
		}
		else if(Gdx.input.isKeyPressed(Keys.S)) {
			moveBy(0, -500 * delta);
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			moveBy(-500 * delta, 0);
		}
		else if(Gdx.input.isKeyPressed(Keys.D)) {
			moveBy(500 * delta, 0);
		}
		
	}
	
	public void updateRotation(float delta, Reticle ret) {
		degrees = (float) ((Math.atan2 (ret.sprite.getY() - getY() + ret.sprite.getHeight()/2, 	// offset by half-reticle 
				ret.sprite.getX() - getX() + ret.sprite.getWidth()/2 ) * 180.0 / Math.PI));		// to center ship with reticle
		setRotation(degrees);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture_region, getX() - getWidth()/2, getY() - getHeight()/2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
}
