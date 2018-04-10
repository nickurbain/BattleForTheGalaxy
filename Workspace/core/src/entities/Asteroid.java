package entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Asteroid extends Actor{
	
	public enum asteroidTypes{
		COPPER,IRON,GOLD,PLATINUM,UNOBTANIUM,EXP;
	}
	
	private int type;
	private int value;
	private TextureRegion textureRegion;
	private Vector2 direction;
	private float velocity;
	private int id;
	
	public Asteroid(int type) {
		this.type = type;
		
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
}
