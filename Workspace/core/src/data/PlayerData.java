package data;

import com.badlogic.gdx.math.Vector2;

public class PlayerData extends EntityData{
	
	public PlayerData(int id, Vector2 pos, Vector2 direction, float rotation) {
		super(id, pos, direction, rotation);
	}

	public void updateData(Vector2 position, Vector2 direction, float rotation) {
		this.setPosition(position);
		this.setDirection(direction);
		this.setRotation(rotation);
	}
	
}
