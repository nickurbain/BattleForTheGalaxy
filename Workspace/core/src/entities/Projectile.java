package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import battle.galaxy.Reticle;

public class Projectile extends Actor{
	
	private Texture texture = new Texture(Gdx.files.internal("bullet.png"));
	private TextureRegion textureRegion = new TextureRegion(texture);
	Circle boundingCircle;
	
	private Vector2 direction;
	private float velocity;
	
	private float lifeTime;
	private float lifeTimer;
	
	public Projectile(float x, float y, float degrees, Reticle ret) {
		this.setPosition(x, y);
		this.direction = new Vector2(0,0);
		
		setSize(50,50);
		setOrigin(getWidth()/2, getHeight()/2);
		setRotation(degrees - 90);
		
		boundingCircle = new Circle();
		boundingCircle.set(getWidth()/2, getHeight()/2, getWidth()/2);
		
		velocity = 1500;
		lifeTimer = 0;
		lifeTime = 2;
		
		direction.x = (x - ret.getX() - ret.getWidth()/2);
		direction.y = (y - ret.getY() - ret.getHeight()/2);
		float dirL = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
		direction.x = direction.x/dirL;
		direction.y = direction.y/dirL;
		
		direction.x = -direction.x*velocity;
		direction.y = -direction.y*velocity;
	}

	public boolean remove() {
		if(lifeTimer > lifeTime) {
			return true;
		}
		return false;
	}
	
	public void act(float delta) {
		lifeTimer += delta;
		moveBy(direction.x*delta, direction.y*delta);
	}

	public void draw(Batch batch, float parentAlpha){
		batch.draw(textureRegion, getX() - getWidth()/2, getY() - getHeight()/2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		
	}
	
	public Vector2 getDirection() {
		return direction;
	}
	
	public float getLifeTime() {
		return lifeTime;
	}
	
	public Vector2 getPosition() {
		return new Vector2(getX(), getY());
	}

}
