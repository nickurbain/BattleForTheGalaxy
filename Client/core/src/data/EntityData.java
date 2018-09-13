package data;

import com.badlogic.gdx.math.Vector2;

/**
 * Abstract class that acts as a model for the entities/actors in the game. Every
 * Entity has an id, a position, a direction, and a rotation. Extends JsonHeader.
 */
public abstract class EntityData extends JsonHeader{

	private int id;
	private Vector2 position;
	private Vector2 direction;
	private float rotation;
	
	/**
	 * Constructor that takes in all neccessary arguments
	 * @param jsonOrigin
	 * @param jsonType
	 * @param id
	 * @param position
	 * @param direction
	 * @param rotation
	 */
	public EntityData(int jsonOrigin, int jsonType, int id, Vector2 position, Vector2 direction, float rotation) {
		super(jsonOrigin, jsonType);
		this.setId(id);
		this.setPosition(new Vector2(position));
		this.setDirection(new Vector2(direction));
		this.setRotation(rotation);
	}
	
	/**
	 * Empy constructor that calls super()
	 */
	public EntityData() {
		super();
	}
	
	/**
	 * Constructor for testing purposes that creates an empty entity
	 * @param jsonOrigin
	 * @param jsonType
	 * @param id 
	 */
	public EntityData(int jsonOrigin, int jsonType, int id) {
		super(jsonOrigin, jsonType);
		setId(id);
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
	 * @return the position
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}

	/**
	 * @return the direction
	 */
	public Vector2 getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Vector2 direction) {
		this.direction = direction;
	}

	/**
	 * @return the rotation
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
}
