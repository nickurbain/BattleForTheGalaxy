package battle.galaxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Projectile extends Actor{
	
	Texture texture = new Texture(Gdx.files.internal("bullet.png"));
	TextureRegion textureRegion = new TextureRegion(texture);
	
	float destX;
	float destY;
	float dx;
	float dy;
	float velocity;
	
	float lifeTime;
	float lifeTimer;
	
	public Projectile(float x, float y, float degrees) {
		this.setPosition(x, y);
		
		setSize(50,50);
		setOrigin(getWidth()/2, getHeight()/2);
		setRotation(degrees - 90);
		
		velocity = 1;
		lifeTimer = 0;
		lifeTime = 2;
		
		float mouseX = Gdx.input.getX();
		float mouseY = Gdx.input.getY();
		
		dx = (mouseX - x - getWidth()/2) * velocity;
		dy = -(mouseY - y - getHeight()/2) * velocity;
	}

	public boolean remove() {
		if(lifeTimer > lifeTime) {
			return true;
		}
		return false;
	}
	
	public void act(float delta) {
		lifeTimer += delta;
		
		moveBy(dx*delta, dy*delta);
	}

	public void draw(Batch batch, float parentAlpha){
		batch.draw(textureRegion, getX() - getWidth()/2, getY() - getHeight()/2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	

}
