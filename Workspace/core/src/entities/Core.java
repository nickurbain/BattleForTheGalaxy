package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import data.CoreData;

public class Core extends Actor{
	
	private TextureRegion textureRegion;
	private int team;
	private Vector2 spawnPoint;
	private boolean pickedUp;
	private int holderId;
	
	public Core(int team, int playerTeam, Vector2 pos) {
		setPosition(pos.x, pos.y);
		setSize(200,200);
		setOrigin(getWidth()/2, getHeight()/2);
		setSpawnPoint(new Vector2(pos));
		pickedUp = false;
		setHolderId(-1);
		if(team == playerTeam) {
			textureRegion = new TextureRegion(new Texture(Gdx.files.internal("Blue/space_bomb_blue.png")));
		}else {
			textureRegion = new TextureRegion(new Texture(Gdx.files.internal("Red/space_bomb.png")));
		}
	}
	
	/**
	 * Pickup the core by a player
	 * @param id the Id of the player who picked up the core
	 */
	public void pickUp(int id) {
		pickedUp = true;
		setHolderId(id);
	}
	
	/**
	 * Drop/reset the core when the player is killed or the core is captured
	 */
	public void drop() {
		pickedUp = false;
		setPosition(spawnPoint.x, spawnPoint.y);
		setHolderId(-1);
	}
	
	/**
	 * Update a core with CoreData sent from another client
	 * @param coreData the CoreData to update with
	 */
	public void update(CoreData coreData) {
		setHolderId(coreData.getPlayerId());
		if(holderId == -1) {
			pickedUp = false;
		}else {
			pickedUp = true;
		}
		if(coreData.isCaptured()) {
			setPosition(spawnPoint.x, spawnPoint.y);
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
	public int getHolderId() {
		return holderId;
	}

	/**
	 * @param playerId the playerId to set
	 */
	public void setHolderId(int playerId) {
		this.holderId = playerId;
	}

}
