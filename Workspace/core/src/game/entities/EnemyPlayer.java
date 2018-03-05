package game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class EnemyPlayer extends Actor{
	
	Texture texture = new Texture(Gdx.files.internal("main-ship.png"));
	TextureRegion textureRegion = new TextureRegion(texture);
	
	private Vector2 position;
	private Vector2 direction;
	private float rotation;
	private int id;
	
	public EnemyPlayer(int id, Vector2 position, Vector2 direction, float rotation) {
		this.position = new Vector2(position);
		this.direction = new Vector2(direction);
		this.rotation = rotation;
		this.id = id;
		
		this.setX(position.x);
		this.setY(position.y);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		setSize(80, 64);
		setOrigin(getWidth()/2, getHeight()/2);
	}
	
	public void act(float delta) {
		float velocity = 800;
		
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
			
		//move the ship
		moveBy(direction.x*velocity*delta, direction.y*velocity*delta);
	}
	
	public void updateEnemy(Vector2 position, Vector2 direction, float rotation) {
		if(direction != null) {
			this.direction.set(direction);
		}
		if(position != null) {
			this.position.set(position);
			setX(position.x);
			setY(position.y);
		}
		if(rotation != 0) {
			this.rotation = rotation;
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(textureRegion, getX() - getWidth()/2, getY() - getHeight()/2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public Vector2 getDirection() {
		return direction;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public int getId() {
		return id;
	}
	
}
