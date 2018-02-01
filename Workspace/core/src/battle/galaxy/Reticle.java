package battle.galaxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class Reticle {
	
	public Texture texture;
	public Sprite sprite;
	
	public Reticle() {
		texture = new Texture(Gdx.files.internal("reticle.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother rendering
		sprite = new Sprite(texture);
		
		sprite.setSize(32, 32);
		sprite.setOriginCenter();
		
	}

	public void update(Vector3 mouse) {
		sprite.setPosition(mouse.x - sprite.getWidth()/2, mouse.y - sprite.getHeight()/2);
	}
}
