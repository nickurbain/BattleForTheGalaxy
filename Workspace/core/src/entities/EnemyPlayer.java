package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import battle.galaxy.GameScreen;
import data.PlayerData;
import data.Ship;

public class EnemyPlayer extends Actor{
	
	Texture texture = new Texture(Gdx.files.internal("Red/spaceship_enemy_red.png"));
	TextureRegion textureRegion = new TextureRegion(texture);
	
	private Vector2 direction;
	private float rotation;
	private int id;
	
	private Ship ship;
	
	public EnemyPlayer(int id, Vector2 position, Vector2 direction, float rotation) {
		setPosition(position.x, position.y);
		this.direction = new Vector2(direction);
		this.rotation = rotation;
		this.id = id;
		
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		setSize(80, 64);
		setOrigin(getWidth()/2, getHeight()/2);
	}
	
	public EnemyPlayer(PlayerData ed) {
		setPosition(ed.getPosition().x, ed.getPosition().y);
		this.direction = new Vector2(ed.getDirection());
		this.rotation = ed.getRotation();
		this.id = ed.getId();
		
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		setSize(80, 64);
		setOrigin(getWidth()/2, getHeight()/2);
	}

	public void act(float delta) {
		float velocity = 800;
		
		//float dirL = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
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
			dist.x = (float) Math.pow(position.x - getX(), 2);
			dist.y = (float) Math.pow(position.y - getY(),2);
			if(Math.sqrt(dist.x + dist.y) > 50) {
				setPosition(position.x, position.y);
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
			dist.x = (float) Math.pow(ed.getPosition().x - getX(), 2);
			dist.y = (float) Math.pow(ed.getPosition().y - getY(),2);
			if(Math.sqrt(dist.x + dist.y) > 50) {
				setPosition(ed.getPosition().x, ed.getPosition().y);
			}
		}
		if(ed.getRotation() != 0) {
			this.rotation = ed.getRotation();
		}
	}
	
	public void reset() {
		direction = new Vector2(0,0);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(textureRegion, getX() - getWidth()/2, getY() - getHeight()/2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		
	}
	
	public Vector2 getPosition() {
		return getPosition();
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

	/**
	 * @return the ship
	 */
	public Ship getShip() {
		return ship;
	}

	/**
	 * @param ship the ship to set
	 */
	public void setShip(Ship ship) {
		this.ship = ship;
	}
	
}
