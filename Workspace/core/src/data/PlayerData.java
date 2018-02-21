package data;

import com.badlogic.gdx.math.Vector2;

public class PlayerData extends EntityData{
	
	public PlayerData(int id, Vector2 pos, Vector2 delta, float rotation) {
		super(id, pos, delta, rotation);
	}

	public void updateData(Vector2 position, Vector2 delta, float rotation) {
		this.setPosition(position);
		this.setDelta(delta);
		this.setRotation(rotation);
	}
	
}
