package battle.galaxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/*
 * TODO:
 */

public class EnemyPlayer extends Actor{
	
	Texture texture = new Texture(Gdx.files.internal("main-ship.png"));
	TextureRegion textureRegion = new TextureRegion(texture);
	
	public EnemyPlayer(float x, float y, float degrees) {
		
	}
	
}
