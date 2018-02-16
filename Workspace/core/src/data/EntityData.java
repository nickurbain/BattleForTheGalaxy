package data;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public abstract class EntityData {

	private Vector2 position;
	private Vector2 delta;
	private float rotation;
	
	public EntityData(Vector2 position, Vector2 delta, float rotation) {
		this.position = new Vector2(position);
		this.delta = new Vector2(delta);
		this.rotation = rotation;
		
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position.set(position);
	}
	
	public Vector2 getDelta() {
		return delta;
	}
	
	public void setDelta(Vector2 delta) {
		this.delta.set(delta);
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
}
