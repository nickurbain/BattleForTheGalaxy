package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import data.PlayerData;

public class EnemyPlayer extends Actor{
	
	Texture texture = new Texture(Gdx.files.internal("main-ship.png"));
	TextureRegion textureRegion = new TextureRegion(texture);
	
	private Vector2 position;
	private Vector2 direction;
	private float rotation;
	private int id;
	
	private int timeSinceLastUpdate;
	
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
	
	public EnemyPlayer(PlayerData ed) {
		this.position = new Vector2(ed.getPosition());
		this.direction = new Vector2(ed.getDirection());
		this.rotation = ed.getRotation();
		this.id = ed.getId();
		
		this.setX(position.x);
		this.setY(position.y);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		setSize(80, 64);
		setOrigin(getWidth()/2, getHeight()/2);
	}

	public void act(float delta) {
		float velocity = 800;
		
		float dirL = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
		//direction.x = direction.x/dirL * velocity;
		//direction.y = direction.y/dirL * velocity;
		
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
		moveBy(direction.x*delta, direction.y*delta);
		
	}
	
	public void updateEnemy(Vector2 position, Vector2 direction, float rotation) {
		if(direction != null) {
			this.direction.set(direction);
		}
		if(position != null) {
			Vector2 dist = new Vector2();
			dist.x = (float) Math.pow(position.x - this.position.x, 2);
			dist.y = (float) Math.pow(position.y - this.position.y,2);
			if(Math.sqrt(dist.x + dist.y) > 50) {
				this.position.set(position);
				setX(position.x);
				setY(position.y);
				
				timeSinceLastUpdate += 1;
			}
		}
		if(rotation != 0) {
			this.rotation = rotation;
		}
	}
	
	public void updateEnemy(PlayerData ed) {
		if(ed.getDirection() != null) {
			this.direction.set(ed.getDirection());
		}
		if(ed.getPosition() != null) {
			Vector2 dist = new Vector2();
			dist.x = (float) Math.pow(ed.getPosition().x - this.position.x, 2);
			dist.y = (float) Math.pow(ed.getPosition().y - this.position.y,2);
			if(Math.sqrt(dist.x + dist.y) > 50) {
				this.position.set(ed.getPosition());
				setX(ed.getPosition().x);
				setY(ed.getPosition().y);
				
				timeSinceLastUpdate += 1;
			}
		}
		if(ed.getRotation() != 0) {
			this.rotation = ed.getRotation();
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(textureRegion, getX() - getWidth()/2, getY() - getHeight()/2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		
	}
	
	public boolean isConnected() {
		if(timeSinceLastUpdate > 90) {
			return false;
		}
		return true;
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

	public void kill() {
		this.getPosition().x = this.getPosition().x + 150;
		this.getPosition().y = this.getPosition().y + 150;
		this.setX(this.getPosition().x);
		this.setY(this.getPosition().y);
	}
	
}
