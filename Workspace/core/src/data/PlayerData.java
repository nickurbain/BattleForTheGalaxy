package data;

import com.badlogic.gdx.math.Vector2;

public class PlayerData extends EntityData{
	
	private boolean friendly;
	private int id;
	
	public PlayerData(Vector2 pos, Vector2 delta, float rotation, int id, boolean friendly) {
		super(pos, delta, rotation);
		this.friendly = friendly;
		this.id = id;
	}

	public void updateData(Vector2 position, Vector2 delta, float rotation) {
		this.setPosition(position);
		this.setDelta(delta);
		this.setRotation(rotation);
	}
	
	public int getId() {
		return id;
	}
	
}
