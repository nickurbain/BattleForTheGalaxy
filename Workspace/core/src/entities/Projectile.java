package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import data.ProjectileData;

/**
 * Entity which represents a projectile fired in game.
 */
public class Projectile extends Actor{
	
	private TextureRegion textureRegion;
	private Vector2 direction;
	private float velocity = 1500;
	private float lifeTime;
	private int source;
	private int sourceTeam;
	private int id;
	private int damage;
	
	/**
	 * Constructor for a projectile fired from the local player
	 * @param pos The x and y coordinates of the projectile
	 * @param degrees The rotation of the projectile
	 * @param ret The position of the cursor
	 * @param source The id of the player who fired the projectile
	 * @param damage The damage the projectile will do
	 * @param lifeTime The range of the projectile in time
	 */
	public Projectile(Vector2 pos, float degrees, Reticle ret, int source, int sourceTeam, int damage, float lifeTime) {
		this.setPosition(pos.x, pos.y);
		this.direction = new Vector2(0,0);
		this.lifeTime = lifeTime;
		this.setSource(source);
		this.setSourceTeam(sourceTeam);
		this.setDamage(damage);
		
		setSize(50,50);
		setOrigin(getWidth()/2, getHeight()/2);
		setRotation(degrees - 90);
		setId(this.hashCode());
		//Cacluate the direction the projectile will move in
		direction.x = (pos.x - ret.getX() - ret.getWidth()/2);
		direction.y = (pos.y - ret.getY() - ret.getHeight()/2);
		float dirL = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
		direction.x = direction.x/dirL;
		direction.y = direction.y/dirL;
		direction.x = -direction.x*velocity;
		direction.y = -direction.y*velocity;
		//Texture
		textureRegion = new TextureRegion(new Texture("bullet.png"));
	}
	
	/**
	 * Constructor for a projectile fired from another client
	 * @param projectileData The projectile data to build the projectile out of
	 */
	public Projectile(ProjectileData projectileData, int playerTeam) {
		this.setPosition(projectileData.getPosition().x, projectileData.getPosition().y);
		this.direction = projectileData.getDirection();
		this.lifeTime = projectileData.getLifeTime();
		setRotation(projectileData.getRotation());
		this.setId(projectileData.getId());
		this.setSource(projectileData.getSource());
		this.setSourceTeam(projectileData.getSourceTeam());
		this.setDamage(projectileData.getDamage());
		setSize(50,50);

		if(playerTeam != sourceTeam) {
			textureRegion = new TextureRegion(new Texture("bullet_red.png"));
		}else {
			textureRegion = new TextureRegion(new Texture("bullet.png"));
		}
	}

	/**
	 * Checks if the projectile has reached its max range
	 * @return isDead Whether or not the projectile is out of range
	 */
	public boolean isDead() {
		if(lifeTime <= 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Reduce the projectile's lifeTime by the change in time and move it
	 */
	public void act(float delta) {
		lifeTime -= delta;
		moveBy(direction.x*delta, direction.y*delta);
	}

	/**
	 * Draw the projectile
	 */
	public void draw(Batch batch, float parentAlpha){
		batch.draw(textureRegion, getX() - getWidth()/2, getY() - getHeight()/2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	/**
	 * Gets the direction of the projectile
	 * @return the Direction
	 */
	public Vector2 getDirection() {
		return direction;
	}
	
	/**
	 * Set the lifeTime to 0
	 */
	public void kill() {
		lifeTime = 0;
	}
	
	/**
	 * Gets the lifeTime
	 * @return the lifeTime
	 */
	public float getLifeTime() {
		return lifeTime;
	}
	
	/**
	 * Gets the position
	 * @return the position
	 */
	public Vector2 getPosition() {
		return new Vector2(getX(), getY());
	}

	/**
	 * @return the source
	 */
	public int getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(int source) {
		this.source = source;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the damage
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * @param damage the damage to set
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * @return the sourceTeam
	 */
	public int getSourceTeam() {
		return sourceTeam;
	}

	/**
	 * @param sourceTeam the sourceTeam to set
	 */
	public void setSourceTeam(int sourceTeam) {
		this.sourceTeam = sourceTeam;
	}

}
