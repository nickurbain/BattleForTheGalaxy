package data;

import com.badlogic.gdx.math.Vector2;

public class PlayerData extends EntityData{
	
	boolean friendly;
	
	public PlayerData(Vector2 pos, Vector2 delta, float rot, boolean friendly) {
		super(pos, delta, rot);
	}

	public void updateData(Vector2 position, Vector2 delta, float rotation) {
		this.setPosition(position);
		this.setDelta(delta);
		this.setRotation(rotation);
	}
	
}
