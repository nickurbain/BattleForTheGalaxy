package entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Entity which is shot in MiningMode to earn points. There are 6 types of asteroids,
 * copper, iron, gold, platinum, unobtanium, and explosive. UNFINISHED
 */
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
	
	/**
	 * Constructor which takes in the type of asteroid to create
	 * @param type the type of asteroid
	 */
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
