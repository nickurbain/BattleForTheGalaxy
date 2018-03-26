package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import data.ProjectileData;

public class Projectile extends Actor{
	
	private Texture texture = new Texture(Gdx.files.internal("bullet.png"));
	private TextureRegion textureRegion = new TextureRegion(texture);
	Circle boundingCircle;
	
	private Vector2 direction;
	private float velocity;
	
	private float lifeTime;
	private int friendly;
	private int id;
	private int damage;
	
	public Projectile(float x, float y, float degrees, Reticle ret, int id, int friendly, int damage, float lifeTime) {
		this.setPosition(x, y);
		this.direction = new Vector2(0,0);
		
		setSize(50,50);
		setOrigin(getWidth()/2, getHeight()/2);
		setRotation(degrees - 90);
		
		boundingCircle = new Circle();
		boundingCircle.set(getWidth()/2, getHeight()/2, getWidth()/2);
		
		velocity = 1500;
		this.lifeTime = lifeTime;
		this.friendly = friendly;
		this.damage = damage;
		
		direction.x = (x - ret.getX() - ret.getWidth()/2);
		direction.y = (y - ret.getY() - ret.getHeight()/2);
		float dirL = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
		direction.x = direction.x/dirL;
		direction.y = direction.y/dirL;
		
		direction.x = -direction.x*velocity;
		direction.y = -direction.y*velocity;
		
		setId(this.hashCode());
	}
	
	public Projectile(ProjectileData projectileData) {
		this.setPosition(projectileData.getPosition().x, projectileData.getPosition().y);
		this.direction = projectileData.getDirection();
		this.lifeTime = projectileData.getlifeTime();
		setRotation(projectileData.getRotation());
		this.id = projectileData.getId();
		this.friendly = projectileData.getFriendly();
		this.damage = projectileData.getDamage();
		
		setSize(50,50);
		velocity = 1500;
	}

	public boolean isDead() {
		if(lifeTime <= 0) {
			return true;
		}
		return false;
	}
	
	public void act(float delta) {
		lifeTime -= delta;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void kill() {
		lifeTime = 0;
	}

	public int getFriendly() {
		return friendly;
	}

	public void setFriendly(int friendly) {
		this.friendly = friendly;
	}

	public int getDamage() {
		return damage;
	}

}
