package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Base extends Actor{
	
	private TextureRegion textureRegion;
	private int team;
	
	public Base(int team, int playerTeam, Vector2 pos) {
		setTeam(team);
		setSize(100,100);
		setScale(10);
		setOrigin(getWidth()/2, getHeight()/2);
		setPosition(pos.x + 200, pos.y);
		if(team == playerTeam) {
			textureRegion = new TextureRegion(new Texture(Gdx.files.internal("Blue/Communicationship_blue.png")));
		}else {
			textureRegion = new TextureRegion(new Texture(Gdx.files.internal("Red/Communicationship2.png")));
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}

	/**
	 * @return the team
	 */
	public int getTeam() {
		return team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(int team) {
		this.team = team;
	}
	
}
