package battle.galaxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/*
 * TODO:
 */

public class EnemyPlayer extends Actor{
	
	Texture texture = new Texture(Gdx.files.internal("main-ship.png"));
	TextureRegion textureRegion = new TextureRegion(texture);
	
	private Vector2 position;
	private Vector2 direction;
	private float rotation;
	
	public EnemyPlayer(Vector2 position, Vector2 direction, float rotation) {
		this.position.set(position);
		this.direction.set(direction);
		this.rotation = rotation;
	}
	
	public void act(float delta) {
		float velocity = 500;
		
		moveBy(direction.x*delta, direction.y*delta);
		//Slow down ship
		if(direction.x > 0) {
			direction.x = direction.x *.98f;
			}
			if(direction.y > 0) {
				direction.y = direction.y * .98f;
			}
			if(direction.x < 0) {
				direction.x = direction.x/1.02f;
			}
			if(direction.y < 0) {
				direction.y = direction.y/1.02f;
			}
	}
	
	public void updateEnemy(Vector2 position, Vector2 direction, float rotation) {
		if(position != null) {
			this.position.set(position);
		}
		if(direction != null) {
			this.direction.set(direction);
		}
		if(rotation != 0) {
			this.rotation = rotation;
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(textureRegion, getX() - getWidth()/2, getY() - getHeight()/2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public Vector2 getDirection() {
		return direction;
	}
	
	public float getRotation() {
		return rotation;
	}
	
}
