package data;

import com.badlogic.gdx.math.Vector2;

/**
 * Abstract class that acts as a model for the actors in the game. Every
 * Entity has an id, a position, a direction, and a rotation.
 *
 */
public abstract class EntityData extends JsonHeader{

	private int id;
	private Vector2 position;
	private Vector2 direction;
	private float rotation;
	
	public EntityData(byte jsonOrigin, byte jsonType, int id, Vector2 position, Vector2 direction, float rotation) {
		super(jsonOrigin, jsonType);
		this.id = id;
		this.position = new Vector2(position);
		this.direction = new Vector2(direction);
		this.rotation = rotation;
	}
	
	public EntityData() {
		super();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position.set(position);
	}
	
	public Vector2 getDirection() {
		return direction;
	}
	
	public void setDirection(Vector2 delta) {
		this.direction.set(delta);
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
}
