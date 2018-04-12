package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Entity which is shot in MiningMode to earn points. There are 6 types of asteroids,
 * copper, iron, gold, platinum, unobtanium, and explosive. UNFINISHED
 */
public class Asteroid extends Actor{
	
	private final float MAP_SIZE = 20480;
	private final int BASE_VALUE = 50;
	private final int BASE_HEALTH = 100;
	private final float BASE_VELOCITY = 400;
	
	public enum asteroidType{
		COPPER,IRON,GOLD,PLATINUM,UNOBTANIUM,EXPLOSIVE;
	}
	
	private asteroidType type;
	private int value;
	private int health;
	private float velocity;
	private TextureRegion textureRegion;
	private Vector2 direction;
	private Vector2 size;
	private int id;
	
	/**
	 * Constructor which takes in the type of asteroid to create
	 * @param type the type of asteroid
	 * @param id the id of this asteroid
	 */
	public Asteroid(asteroidType type, int id, Vector2 pos) {
		this.setType(type);
		this.id = id;
		generateProperties();
		size = new Vector2((int) Math.random() * 400 + 100, 0);
		size.y = size.x;
		setSize(size.x, size.y);
		scaleBy((float)Math.random() * 3);
		setOrigin(getWidth()/2, getHeight()/2);
		setPosition(pos.x, pos.y);
		generateDirection();
	}
	
	private void generateProperties() {
		switch(type){
		case COPPER:
			health = BASE_HEALTH;
			velocity = BASE_VELOCITY;
			setValue(BASE_VALUE);
			textureRegion = new TextureRegion(new Texture(Gdx.files.internal("asteroid_brown.png")));
			break;
		case IRON:
			health = (int) (BASE_HEALTH * 1.5f);
			velocity = BASE_VELOCITY * 1.5f;
			setValue(BASE_VALUE * 2);
			break;
		case GOLD:
			health = BASE_HEALTH * 3;
			velocity = BASE_VELOCITY * .75f;
			setValue(BASE_VALUE * 3);
			break;
		case PLATINUM:
			health = BASE_HEALTH * 5;
			velocity = BASE_VELOCITY * .5f;
			setValue(BASE_VALUE * 4);
			break;
		case UNOBTANIUM:
			health = BASE_HEALTH * 2;
			velocity = BASE_VELOCITY * 2;
			setValue(BASE_VALUE * 10);
			break;
		case EXPLOSIVE:
			health = (int) (BASE_HEALTH * 1.5f);
			velocity = BASE_VELOCITY * 1.75f;
			setValue(0);
			break;
		}
		
		if(type != asteroidType.COPPER) {
			int color = (int) (Math.random() * 3);
			switch(color) {
				case 0:
					textureRegion = new TextureRegion(new Texture(Gdx.files.internal("asteroid_brown.png")));
					break;
				case 1:
					textureRegion = new TextureRegion(new Texture(Gdx.files.internal("asteroid_dark.png")));
					break;
				case 2:
					textureRegion = new TextureRegion(new Texture(Gdx.files.internal("asteroid_gray_2.png")));
					break;
				case 3:
					textureRegion = new TextureRegion(new Texture(Gdx.files.internal("asteroid_gray.png")));
					break;
			}
		}
	}
	
	private void generateDirection() {
		direction = new Vector2();
		direction.x = (float) (getX() - Math.random() * MAP_SIZE);
		direction.y = (float) (getY() - Math.random() * MAP_SIZE);
		float dirL = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
		direction.x = direction.x/dirL;
		direction.y = direction.y/dirL;
		direction.x = -direction.x*velocity;
		direction.y = -direction.y*velocity;
	}
	
	@Override
	public void act(float delta) {
		moveBy(direction.x * delta, direction.y * delta);
	}
	
	/**
	 * Remake the asteroid and move its position
	 */
	public void kill() {
		setPosition(0,0);
	}
	
	/**
	 * Draw the asteroid
	 */
	public void draw(Batch batch, float parentAlpha){
		batch.draw(textureRegion, getX() - getWidth()/2, getY() - getHeight()/2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	/**
	 * Damage the asteroid and return its health
	 * @param damage the amount of damage to deal
	 * @return the remaining health
	 */
	public int damage(int damage) {
		health -= damage;
		return health;
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

	/**
	 * @return the type
	 */
	public asteroidType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(asteroidType type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param health the health to set
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * @return the size
	 */
	public Vector2 getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Vector2 size) {
		this.size = size;
	}
}
