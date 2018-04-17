package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Core extends Actor{
	
	private TextureRegion textureRegion;
	private int team;
	private Vector2 spawnPoint;
	private boolean pickedUp;
	private int playerId;
	
	public Core(int team, Vector2 pos) {
		setPosition(pos.x, pos.y);
		setSpawnPoint(new Vector2(pos));
		pickedUp = false;
		setPlayerId(-1);
		if(team == 0) {
			textureRegion = new TextureRegion(new Texture(Gdx.files.internal("Blue/space_bomb_blue.png")));
		}else {
			textureRegion = new TextureRegion(new Texture(Gdx.files.internal("Red/space_bomb.png")));
		}
	}
	
	public void pickUp(int id) {
		pickedUp = true;
		setPlayerId(id);
	}
	
	public void drop() {
		pickedUp = false;
		setPosition(spawnPoint.x, spawnPoint.y);
		setPlayerId(-1);
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

	/**
	 * @return the pickedUp
	 */
	public boolean isPickedUp() {
		return pickedUp;
	}

	/**
	 * @param pickedUp the pickedUp to set
	 */
	public void setPickedUp(boolean pickedUp) {
		this.pickedUp = pickedUp;
	}

	/**
	 * @return the spawnPoint
	 */
	public Vector2 getSpawnPoint() {
		return spawnPoint;
	}

	/**
	 * @param spawnPoint the spawnPoint to set
	 */
	public void setSpawnPoint(Vector2 spawnPoint) {
		this.spawnPoint = spawnPoint;
	}

	/**
	 * @return the playerId
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * @param playerId the playerId to set
	 */
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

}
