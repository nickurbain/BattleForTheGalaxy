package data;

/**
 * Class to encapsulate flag pickup/drop information sent over the server
 */
public class CoreData extends JsonHeader{
	
	private int flagNum;
	private int playerId;
	
	public CoreData(int flagNum, int playerId) {
		this.flagNum = flagNum;
		this.playerId = playerId;
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
	
}
