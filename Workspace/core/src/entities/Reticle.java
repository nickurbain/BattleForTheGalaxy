package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Reticle extends Actor {
	
	Texture texture = new Texture(Gdx.files.internal("reticle.png"));
	TextureRegion texture_region = new TextureRegion(texture);
	Sprite sprite;
	
	public Reticle() {
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother rendering
		setSize(32,32);
		setOrigin(getWidth()/2, getHeight()/2);
		
	}
	
	@Override
	public void act(float delta) {
		
	}

	public void update(Vector3 mouse) {
		setPosition(mouse.x - getWidth()/2, mouse.y - getHeight()/2);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture_region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
}
