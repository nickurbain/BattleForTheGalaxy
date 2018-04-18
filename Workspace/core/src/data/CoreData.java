package data;

/**
 * Class to encapsulate flag pickup/drop information sent over the server
 */
public class CoreData extends JsonHeader{
	
	private int flagNum;
	private int playerId;
	private boolean captured;
	
	public CoreData(int flagNum, int playerId, boolean captured) {
		super(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_CORE_UPDATE);
		this.flagNum = flagNum;
		this.playerId = playerId;
		this.captured = captured;
	}

	/**
	 * @return the flagNum
	 */
	public int getFlagNum() {
		return flagNum;
	}

	/**
	 * @param flagNum the flagNum to set
	 */
	public void setFlagNum(int flagNum) {
		this.flagNum = flagNum;
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

	/**
	 * @return the captured
	 */
	public boolean isCaptured() {
		return captured;
	}

	/**
	 * @param captured the captured to set
	 */
	public void setCaptured(boolean captured) {
		this.captured = captured;
	}
	
}
